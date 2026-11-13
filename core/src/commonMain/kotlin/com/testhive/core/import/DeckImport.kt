package com.testhive.core.import

import com.testhive.core.domain.ClozeQuestion
import com.testhive.core.domain.Deck
import com.testhive.core.domain.MatchQuestion
import com.testhive.core.domain.MultiQuestion
import com.testhive.core.domain.OrderQuestion
import com.testhive.core.domain.Question
import com.testhive.core.domain.RevealQuestion
import com.testhive.core.domain.SingleQuestion
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

sealed class DeckImportResult {
    data class Ok(val deck: Deck) : DeckImportResult()
    data class Err(val message: String) : DeckImportResult()
}

object DeckImporter {
    private val json = Json {
        ignoreUnknownKeys = false
        classDiscriminator = "type"
    }

    fun import(text: String): DeckImportResult {
        val deck = try {
            json.decodeFromString(Deck.serializer(), text)
        } catch (e: SerializationException) {
            return DeckImportResult.Err(e.message ?: "Invalid deck JSON")
        } catch (e: IllegalArgumentException) {
            return DeckImportResult.Err(e.message ?: "Invalid deck JSON")
        }

        validate(deck)?.let { return DeckImportResult.Err(it) }
        return DeckImportResult.Ok(deck)
    }

    private fun validate(deck: Deck): String? {
        if (deck.formatVersion != 1) {
            return "Unsupported formatVersion: ${deck.formatVersion}"
        }
        if (deck.id.isBlank()) return "Deck id must be non-blank"
        if (deck.title.isBlank()) return "Deck title must be non-blank"
        if (deck.questions.isEmpty()) return "Deck must contain at least one question"

        val questionIds = mutableSetOf<String>()
        for (q in deck.questions) {
            if (q.id.isBlank()) return "Question id must be non-blank"
            if (!questionIds.add(q.id)) return "Duplicate question id: ${q.id}"
            if (q.prompt.isBlank()) return "Question ${q.id}: prompt must be non-blank"
            validateQuestion(q)?.let { return it }
        }
        return null
    }

    private fun validateQuestion(q: Question): String? {
        return when (q) {
            is SingleQuestion -> {
                if (q.options.size < 2) return "Question ${q.id}: single needs ≥2 options"
                val ids = q.options.map { it.id }
                if (ids.toSet().size != ids.size) return "Question ${q.id}: duplicate option ids"
                if (q.answer !in ids) return "Question ${q.id}: answer not in options"
                null
            }
            is MultiQuestion -> {
                if (q.options.isEmpty()) return "Question ${q.id}: multi needs options"
                val ids = q.options.map { it.id }
                if (ids.toSet().size != ids.size) return "Question ${q.id}: duplicate option ids"
                if (q.answer.isEmpty()) return "Question ${q.id}: multi answer empty"
                if (q.answer.toSet().size != q.answer.size) return "Question ${q.id}: duplicate answer ids"
                if (!q.answer.all { it in ids }) return "Question ${q.id}: answer not subset of options"
                null
            }
            is OrderQuestion -> {
                if (q.items.size < 2) return "Question ${q.id}: order needs ≥2 items"
                val ids = q.items.map { it.id }
                if (ids.toSet().size != ids.size) return "Question ${q.id}: duplicate item ids"
                if (q.answer.toSet() != ids.toSet() || q.answer.size != ids.size) {
                    return "Question ${q.id}: answer must be a permutation of all items"
                }
                null
            }
            is MatchQuestion -> {
                if (q.left.isEmpty() || q.right.isEmpty()) {
                    return "Question ${q.id}: match needs left and right"
                }
                val leftIds = q.left.map { it.id }
                val rightIds = q.right.map { it.id }
                if (leftIds.toSet().size != leftIds.size) return "Question ${q.id}: duplicate left ids"
                if (rightIds.toSet().size != rightIds.size) return "Question ${q.id}: duplicate right ids"
                if (q.answer.keys.toSet() != leftIds.toSet()) {
                    return "Question ${q.id}: answer must cover every left id"
                }
                val usedRights = q.answer.values
                if (usedRights.toSet().size != usedRights.size) {
                    return "Question ${q.id}: right ids in answer must be unique"
                }
                if (!usedRights.all { it in rightIds }) {
                    return "Question ${q.id}: answer references unknown right id"
                }
                null
            }
            is ClozeQuestion -> {
                if (q.blanks.isEmpty()) return "Question ${q.id}: cloze needs blanks"
                for ((blankId, accepted) in q.blanks) {
                    if (accepted.isEmpty()) {
                        return "Question ${q.id}: blank $blankId has no accepted answers"
                    }
                }
                null
            }
            is RevealQuestion -> {
                if (q.answerText.isBlank()) return "Question ${q.id}: reveal needs answerText"
                null
            }
        }
    }
}

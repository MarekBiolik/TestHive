package com.testhive.android.data

import android.content.Context
import com.testhive.core.domain.Deck
import com.testhive.core.domain.LastResult
import com.testhive.core.domain.QuestionProgress
import com.testhive.core.import.DeckImportResult
import com.testhive.core.import.DeckImporter
import com.testhive.core.library.DeckLibrary
import com.testhive.core.progress.ProgressStore
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileDeckLibrary(
    private val context: Context,
) : DeckLibrary {
    private val json = Json {
        ignoreUnknownKeys = false
        classDiscriminator = "type"
        prettyPrint = true
    }
    private val dir: File = File(context.filesDir, "decks").also { it.mkdirs() }
    private val cache = linkedMapOf<String, Deck>()

    init {
        reload()
    }

    fun reload() {
        cache.clear()
        dir.listFiles()?.filter { it.extension == "json" }?.forEach { file ->
            when (val result = DeckImporter.import(file.readText())) {
                is DeckImportResult.Ok -> cache[result.deck.id] = result.deck
                is DeckImportResult.Err -> Unit
            }
        }
    }

    override fun list(): List<Deck> = cache.values.toList()

    override fun get(deckId: String): Deck? = cache[deckId]

    override fun upsert(deck: Deck) {
        cache[deck.id] = deck
        File(dir, "${deck.id}.json").writeText(json.encodeToString(Deck.serializer(), deck))
    }

    override fun remove(deckId: String) {
        cache.remove(deckId)
        File(dir, "$deckId.json").delete()
    }

    fun ensureBundledSample() {
        if (get("cpp-modern") != null) return
        val text = context.assets.open("cpp-modern.json").bufferedReader().use { it.readText() }
        when (val result = DeckImporter.import(text)) {
            is DeckImportResult.Ok -> upsert(result.deck)
            is DeckImportResult.Err -> error("Bundled sample invalid: ${result.message}")
        }
    }
}

class FileProgressStore(
    context: Context,
) : ProgressStore {
    private val json = Json { prettyPrint = true }
    private val file = File(context.filesDir, "progress.json")
    private val rows = linkedMapOf<String, QuestionProgress>()

    init {
        if (file.exists()) {
            val stored = json.decodeFromString(ProgressFile.serializer(), file.readText())
            stored.rows.forEach { row ->
                rows[key(row.deckId, row.questionId)] = QuestionProgress(
                    deckId = row.deckId,
                    questionId = row.questionId,
                    correctCount = row.correctCount,
                    wrongCount = row.wrongCount,
                    lastResult = row.lastResult?.let { LastResult.valueOf(it) },
                    lastAnsweredAtEpochMs = row.lastAnsweredAtEpochMs,
                )
            }
        }
    }

    private fun key(deckId: String, questionId: String) = "$deckId::$questionId"

    private fun persist() {
        val payload = ProgressFile(
            rows = rows.values.map {
                ProgressRow(
                    deckId = it.deckId,
                    questionId = it.questionId,
                    correctCount = it.correctCount,
                    wrongCount = it.wrongCount,
                    lastResult = it.lastResult?.name,
                    lastAnsweredAtEpochMs = it.lastAnsweredAtEpochMs,
                )
            },
        )
        file.writeText(json.encodeToString(ProgressFile.serializer(), payload))
    }

    override fun get(deckId: String, questionId: String): QuestionProgress? =
        rows[key(deckId, questionId)]

    override fun allForDecks(deckIds: Set<String>): List<QuestionProgress> =
        rows.values.filter { it.deckId in deckIds }

    override fun record(deckId: String, questionId: String, correct: Boolean, answeredAtEpochMs: Long) {
        val k = key(deckId, questionId)
        val prev = rows[k]
        rows[k] = QuestionProgress(
            deckId = deckId,
            questionId = questionId,
            correctCount = (prev?.correctCount ?: 0) + if (correct) 1 else 0,
            wrongCount = (prev?.wrongCount ?: 0) + if (correct) 0 else 1,
            lastResult = if (correct) LastResult.Correct else LastResult.Wrong,
            lastAnsweredAtEpochMs = answeredAtEpochMs,
        )
        persist()
    }

    override fun removeDeck(deckId: String) {
        rows.keys.filter { it.startsWith("$deckId::") }.forEach { rows.remove(it) }
        persist()
    }
}

@Serializable
private data class ProgressFile(val rows: List<ProgressRow> = emptyList())

@Serializable
private data class ProgressRow(
    val deckId: String,
    val questionId: String,
    val correctCount: Int,
    val wrongCount: Int,
    val lastResult: String? = null,
    val lastAnsweredAtEpochMs: Long? = null,
)

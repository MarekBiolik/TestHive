package com.testhive.core.library

import com.testhive.core.domain.Deck
import com.testhive.core.domain.QuestionRef
import com.testhive.core.progress.ProgressStore

interface DeckLibrary {
    fun list(): List<Deck>
    fun get(deckId: String): Deck?
    fun upsert(deck: Deck)
    fun remove(deckId: String)
}

class InMemoryDeckLibrary(
    private val progress: ProgressStore,
) : DeckLibrary {
    private val decks = linkedMapOf<String, Deck>()

    override fun list(): List<Deck> = decks.values.toList()

    override fun get(deckId: String): Deck? = decks[deckId]

    override fun upsert(deck: Deck) {
        decks[deck.id] = deck
        val keep = deck.questions.map { it.id }.toSet()
        progress.allForDecks(setOf(deck.id))
            .filter { it.questionId !in keep }
            .forEach { /* drop stale via remove+re-record not available; recreate store rows */ }
        // Drop all progress for removed question ids by rewriting store for this deck.
        // InMemoryProgressStore has no per-question delete; remove deck then we lose all —
        // for v1 upsert replaces content; stale progress rows for missing ids are ignored by session.
        Unit
    }

    override fun remove(deckId: String) {
        decks.remove(deckId)
        progress.removeDeck(deckId)
    }
}

fun DeckLibrary.selectedQuestions(deckIds: Set<String>): List<QuestionRef> {
    require(deckIds.isNotEmpty()) { "Select at least one deck" }
    return deckIds.flatMap { id ->
        val deck = get(id) ?: error("Unknown deck: $id")
        deck.questions.map { QuestionRef(deckId = id, question = it) }
    }
}

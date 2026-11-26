package com.testhive.core.progress

import com.testhive.core.domain.LastResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProgressStoreTest {
    @Test
    fun markMasteredPreservesCountsAndSetsFlag() {
        val store = InMemoryProgressStore()
        store.record("d", "q1", correct = true, answeredAtEpochMs = 1L)
        store.markMastered("d", "q1", answeredAtEpochMs = 2L)
        val row = store.get("d", "q1")!!
        assertTrue(row.mastered)
        assertEquals(1, row.correctCount)
        assertEquals(LastResult.Correct, row.lastResult)
        assertEquals(2L, row.lastAnsweredAtEpochMs)
    }

    @Test
    fun markMasteredWithoutPriorRowCreatesMasteredEntry() {
        val store = InMemoryProgressStore()
        store.markMastered("d", "q1", answeredAtEpochMs = 9L)
        val row = store.get("d", "q1")!!
        assertTrue(row.mastered)
        assertEquals(0, row.correctCount)
        assertNull(row.lastResult)
    }

    @Test
    fun recordDoesNotClearMastered() {
        val store = InMemoryProgressStore()
        store.markMastered("d", "q1", answeredAtEpochMs = 1L)
        store.record("d", "q1", correct = false, answeredAtEpochMs = 2L)
        assertTrue(store.get("d", "q1")!!.mastered)
    }

    @Test
    fun resetDecksClearsSelectedOnly() {
        val store = InMemoryProgressStore()
        store.record("a", "q1", correct = true, answeredAtEpochMs = 1L)
        store.markMastered("b", "q2", answeredAtEpochMs = 1L)
        store.resetDecks(setOf("a"))
        assertNull(store.get("a", "q1"))
        assertTrue(store.get("b", "q2")!!.mastered)
        assertFalse(store.allForDecks(setOf("a")).any())
    }
}

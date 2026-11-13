package com.testhive.core.import

import com.testhive.core.TestResources
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DeckImporterTest {
    @Test
    fun acceptsCppModernSample() {
        val text = TestResources.read("samples/cpp-modern.json")
        val result = DeckImporter.import(text)
        assertIs<DeckImportResult.Ok>(result)
        assertTrue(result.deck.questions.size >= 12)
        assertTrue(result.deck.id == "cpp-modern")
    }

    @Test
    fun rejectsBadFormatVersion() {
        val text = TestResources.read("samples/cpp-modern.json")
            .replace("\"formatVersion\": 1", "\"formatVersion\": 99")
        val result = DeckImporter.import(text)
        assertIs<DeckImportResult.Err>(result)
    }

    @Test
    fun rejectsUnknownTemplateType() {
        val text = """
            {
              "formatVersion": 1,
              "id": "bad",
              "title": "Bad",
              "language": "en",
              "questions": [
                {
                  "id": "q1",
                  "type": "not-a-template",
                  "prompt": "x",
                  "answer": "a"
                }
              ]
            }
        """.trimIndent()
        val result = DeckImporter.import(text)
        assertIs<DeckImportResult.Err>(result)
    }

    @Test
    fun rejectsAnswerNotInOptions() {
        val text = """
            {
              "formatVersion": 1,
              "id": "bad",
              "title": "Bad",
              "language": "en",
              "questions": [
                {
                  "id": "q1",
                  "type": "single",
                  "prompt": "x",
                  "options": [
                    { "id": "a", "text": "A" },
                    { "id": "b", "text": "B" }
                  ],
                  "answer": "z"
                }
              ]
            }
        """.trimIndent()
        val result = DeckImporter.import(text)
        assertIs<DeckImportResult.Err>(result)
    }
}

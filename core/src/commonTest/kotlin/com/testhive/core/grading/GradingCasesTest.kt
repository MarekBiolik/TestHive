package com.testhive.core.grading

import com.testhive.core.TestResources
import com.testhive.core.domain.AnswerSubmission
import com.testhive.core.domain.ClozeQuestion
import com.testhive.core.domain.LabeledItem
import com.testhive.core.domain.MatchQuestion
import com.testhive.core.domain.MultiQuestion
import com.testhive.core.domain.OrderQuestion
import com.testhive.core.domain.Question
import com.testhive.core.domain.RevealQuestion
import com.testhive.core.domain.SingleQuestion
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class GradingCasesTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun allFixturesMatchExpectedGrade() {
        val root = json.parseToJsonElement(TestResources.read("samples/grading-cases.json")).jsonObject
        val cases = root.getValue("cases").jsonArray
        for (caseEl in cases) {
            val case = caseEl.jsonObject
            val id = case.getValue("id").jsonPrimitive.content
            val type = case.getValue("type").jsonPrimitive.content
            val questionObj = case.getValue("question").jsonObject
            val submissionEl = case.getValue("submission")
            val expected = case.getValue("correct").jsonPrimitive.boolean

            val question = toQuestion(type, questionObj)
            val submission = toSubmission(type, submissionEl)
            val result = Grader.grade(question, submission)
            assertEquals(expected, result.correct, "case $id")
        }
    }

    private fun toQuestion(type: String, obj: JsonObject): Question = when (type) {
        "single" -> SingleQuestion(
            id = "q",
            prompt = "p",
            options = obj.getValue("options").jsonArray.map { it.toLabeled() },
            answer = obj.getValue("answer").jsonPrimitive.content,
        )
        "multi" -> MultiQuestion(
            id = "q",
            prompt = "p",
            options = obj.getValue("options").jsonArray.map { it.toLabeled() },
            answer = obj.getValue("answer").jsonArray.map { it.jsonPrimitive.content },
        )
        "order" -> OrderQuestion(
            id = "q",
            prompt = "p",
            items = obj.getValue("items").jsonArray.map { it.toLabeled() },
            answer = obj.getValue("answer").jsonArray.map { it.jsonPrimitive.content },
        )
        "match" -> MatchQuestion(
            id = "q",
            prompt = "p",
            left = obj.getValue("left").jsonArray.map { it.toLabeled() },
            right = obj.getValue("right").jsonArray.map { it.toLabeled() },
            answer = obj.getValue("answer").jsonObject.mapValues { it.value.jsonPrimitive.content },
        )
        "cloze" -> ClozeQuestion(
            id = "q",
            prompt = "p",
            blanks = obj.getValue("blanks").jsonObject.mapValues { (_, v) ->
                v.jsonArray.map { it.jsonPrimitive.content }
            },
        )
        "reveal" -> RevealQuestion(
            id = "q",
            prompt = "p",
            answerText = obj.getValue("answerText").jsonPrimitive.content,
        )
        else -> error("Unknown type $type")
    }

    private fun toSubmission(type: String, el: kotlinx.serialization.json.JsonElement): AnswerSubmission =
        when (type) {
            "single" -> AnswerSubmission.Single(el.jsonPrimitive.content)
            "multi" -> AnswerSubmission.Multi(el.jsonArray.map { it.jsonPrimitive.content })
            "order" -> AnswerSubmission.Order(el.jsonArray.map { it.jsonPrimitive.content })
            "match" -> AnswerSubmission.Match(el.jsonObject.mapValues { it.value.jsonPrimitive.content })
            "cloze" -> AnswerSubmission.Cloze(el.jsonObject.mapValues { it.value.jsonPrimitive.content })
            "reveal" -> AnswerSubmission.Reveal(el.jsonObject.getValue("knewIt").jsonPrimitive.boolean)
            else -> error("Unknown type $type")
        }

    private fun kotlinx.serialization.json.JsonElement.toLabeled(): LabeledItem {
        val o = jsonObject
        return LabeledItem(
            id = o.getValue("id").jsonPrimitive.content,
            text = o.getValue("text").jsonPrimitive.content,
        )
    }
}

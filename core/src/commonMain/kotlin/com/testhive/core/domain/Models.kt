package com.testhive.core.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CodeBlock(
    val language: String,
    val source: String,
)

@Serializable
data class LabeledItem(
    val id: String,
    val text: String,
)

@Serializable
data class Deck(
    val formatVersion: Int,
    val id: String,
    val title: String,
    val language: String,
    val description: String? = null,
    val questions: List<Question>,
)

@Serializable
sealed class Question {
    abstract val id: String
    abstract val prompt: String
    abstract val code: CodeBlock?
    abstract val explanation: String?
}

@Serializable
@SerialName("single")
data class SingleQuestion(
    override val id: String,
    override val prompt: String,
    override val code: CodeBlock? = null,
    override val explanation: String? = null,
    val options: List<LabeledItem>,
    val answer: String,
) : Question()

@Serializable
@SerialName("multi")
data class MultiQuestion(
    override val id: String,
    override val prompt: String,
    override val code: CodeBlock? = null,
    override val explanation: String? = null,
    val options: List<LabeledItem>,
    val answer: List<String>,
) : Question()

@Serializable
@SerialName("order")
data class OrderQuestion(
    override val id: String,
    override val prompt: String,
    override val code: CodeBlock? = null,
    override val explanation: String? = null,
    val items: List<LabeledItem>,
    val answer: List<String>,
) : Question()

@Serializable
@SerialName("match")
data class MatchQuestion(
    override val id: String,
    override val prompt: String,
    override val code: CodeBlock? = null,
    override val explanation: String? = null,
    val left: List<LabeledItem>,
    val right: List<LabeledItem>,
    val answer: Map<String, String>,
) : Question()

@Serializable
@SerialName("cloze")
data class ClozeQuestion(
    override val id: String,
    override val prompt: String,
    override val code: CodeBlock? = null,
    override val explanation: String? = null,
    val blanks: Map<String, List<String>>,
    val choices: List<String> = emptyList(),
) : Question()

@Serializable
@SerialName("reveal")
data class RevealQuestion(
    override val id: String,
    override val prompt: String,
    override val code: CodeBlock? = null,
    override val explanation: String? = null,
    val answerText: String,
) : Question()

enum class LastResult {
    Correct,
    Wrong,
}

data class QuestionProgress(
    val deckId: String,
    val questionId: String,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val lastResult: LastResult? = null,
    val lastAnsweredAtEpochMs: Long? = null,
)

data class QuestionRef(
    val deckId: String,
    val question: Question,
)

sealed class AnswerSubmission {
    data class Single(val optionId: String) : AnswerSubmission()
    data class Multi(val optionIds: List<String>) : AnswerSubmission()
    data class Order(val itemIds: List<String>) : AnswerSubmission()
    data class Match(val pairs: Map<String, String>) : AnswerSubmission()
    data class Cloze(val blanks: Map<String, String>) : AnswerSubmission()
    data class Reveal(val knewIt: Boolean) : AnswerSubmission()
}

data class GradeResult(
    val correct: Boolean,
)

package com.testhive.core.grading

import com.testhive.core.domain.AnswerSubmission
import com.testhive.core.domain.ClozeQuestion
import com.testhive.core.domain.GradeResult
import com.testhive.core.domain.MatchQuestion
import com.testhive.core.domain.MultiQuestion
import com.testhive.core.domain.OrderQuestion
import com.testhive.core.domain.Question
import com.testhive.core.domain.RevealQuestion
import com.testhive.core.domain.SingleQuestion

object Grader {
    fun grade(question: Question, submission: AnswerSubmission): GradeResult {
        val correct = when (question) {
            is SingleQuestion -> {
                submission is AnswerSubmission.Single && submission.optionId == question.answer
            }
            is MultiQuestion -> {
                submission is AnswerSubmission.Multi &&
                    submission.optionIds.toSet() == question.answer.toSet()
            }
            is OrderQuestion -> {
                submission is AnswerSubmission.Order && submission.itemIds == question.answer
            }
            is MatchQuestion -> {
                submission is AnswerSubmission.Match &&
                    question.answer.keys.all { leftId ->
                        submission.pairs[leftId] == question.answer[leftId]
                    } &&
                    submission.pairs.keys == question.answer.keys
            }
            is ClozeQuestion -> {
                submission is AnswerSubmission.Cloze &&
                    question.blanks.keys.all { blankId ->
                        val submitted = submission.blanks[blankId] ?: return@all false
                        val accepted = question.blanks.getValue(blankId).map { normalize(it) }
                        normalize(submitted) in accepted
                    } &&
                    submission.blanks.keys == question.blanks.keys
            }
            is RevealQuestion -> {
                submission is AnswerSubmission.Reveal && submission.knewIt
            }
        }
        return GradeResult(correct = correct)
    }

    fun normalize(value: String): String = value.trim().lowercase()
}

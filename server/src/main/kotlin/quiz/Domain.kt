package quiz

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import kotlinx.serialization.*

typealias AnswerID = Int

typealias QuestionID = Int

typealias InputID = Int

@Serializable
data class QuizResult(
        val totalPoints: Int,
        val points: Int,
        val answers: List<List<String>>,
)

@Serializable
data class Answer(
        val id: AnswerID,
        val questionID: QuestionID,
        val answers: List<String>,
)

@Serializable
data class Question(
        val id: QuestionID,
        val title: String,
        val desc: String,
        val inputs: List<Input>,
)

@Serializable
data class Input(
        val id: InputID,
        val type: InputType,
        val label: String,
        val name: String,
)

enum class InputType(val typ: String) {
    TEXT("text"),
    CHECKBOX("checkbox"),
    RADIO("radio"),
}

interface QuestionRepository {
    suspend fun getQuestions(): Result<List<Question>>
    suspend fun getAnswers(): Result<List<Answer>>
}

class QuestionRepositoryInMemory : QuestionRepository {
    override suspend fun getQuestions(): Result<List<Question>> {
        val questions =
                listOf(
                        Question(
                                1,
                                "Who is the current 'chairman of the board' at Bouvet?",
                                "According to the bouvet.no webpage.",
                                listOf(Input(1, InputType.TEXT, "Enter full name", "chairman")),
                        ),
                        Question(
                                2,
                                "How many office locations does Bouvet have?",
                                "Including both Norway and Sweden.",
                                listOf(
                                        Input(1, InputType.RADIO, "17", "location"),
                                        Input(2, InputType.RADIO, "15", "location"),
                                        Input(3, InputType.RADIO, "19", "location")
                                ),
                        ),
                        Question(
                                3,
                                "Which of these statements are true about Bouvet?",
                                "Select all that apply.",
                                listOf(
                                        Input(
                                                1,
                                                InputType.CHECKBOX,
                                                "Bouvet Sandefjord is the best sector.",
                                                "statement"
                                        ),
                                        Input(
                                                2,
                                                InputType.CHECKBOX,
                                                "Employees enjoy the workspace.",
                                                "statement"
                                        ),
                                        Input(
                                                3,
                                                InputType.CHECKBOX,
                                                "Customers are happy with the service.",
                                                "statement"
                                        ),
                                        Input(
                                                4,
                                                InputType.CHECKBOX,
                                                "Employees love to use vim!",
                                                "statement"
                                        ),
                                ),
                        ),
                )
        return Result.success(questions)
    }

    override suspend fun getAnswers(): Result<List<Answer>> {
        val answers =
                listOf(
                        Answer(1, 1, listOf("Pål Egil Rønn")),
                        Answer(2, 2, listOf("17")),
                        Answer(
                                3,
                                3,
                                listOf(
                                        "Bouvet Sandefjord is the best sector.",
                                        "Employees enjoy the workspace.",
                                        "Customers are happy with the service.",
                                        "Employees love to use vim!",
                                )
                        ),
                )
        return Result.success(answers)
    }
}

class QuestionService(private val repo: QuestionRepository) {
    suspend fun getQuestions(): Result<List<Question>> {
        return repo.getQuestions()
    }

    suspend fun getAnswers(entries: Set<Map.Entry<String, List<String>>>): Result<QuizResult> {
        var result = repo.getAnswers()

        result.onFailure {
            return Result.failure(Exception("failed to fetch questions"))
        }

        result.onSuccess {
            var points = 0
            var totalPoints = 0

            val iterator = entries.iterator()
            if (!iterator.hasNext()) {
                return Result.failure(Exception("bad request: missing entries"))
            }
            val questionIDs = iterator.next()
            if (questionIDs.key != "questionID") {
                return Result.failure(Exception("bad request: missing hidden questionID input(s)"))
            }

            for (id in questionIDs.value) {
                val answer = it.find { answer -> answer.questionID.toString() == id }
                if (answer == null) {
                    println("answer not found for questionID $questionIDs[idx]")
                    continue
                }

                totalPoints += answer.answers.size

                if (!iterator.hasNext()) {
                    println("entries iterator has no more entries")
                    continue
                }
                val entry = iterator.next()

                answer.answers.forEach { answerValue ->
                    entry.value.forEach { entryValue ->
                        if (entryValue.lowercase().trim() == answerValue.lowercase().trim()) {
                            points++
                        }
                    }
                }
            }

            val answerList = it.map { answer -> answer.answers }
            return Result.success(QuizResult(totalPoints, points, answerList))
        }

        return Result.failure(Exception("failed to get quiz answers"))
    }
}

class QuestionController(private val srvc: QuestionService) {
    suspend fun getQuestions(call: ApplicationCall) {
        val result = srvc.getQuestions()

        result.onFailure {
            println("failed to get questions: ${it.message}")
            call.respond(HttpStatusCode.InternalServerError, "Internal server error")
            return
        }

        result.onSuccess {
            call.respond(HttpStatusCode.OK, it)
            return
        }
    }

    suspend fun getAnswers(call: ApplicationCall) {
        val params = call.receiveParameters()
        val entries = params.entries()
        val result = srvc.getAnswers(entries)

        result.onFailure {
            println("failed to get answers: ${it.message}")
            call.respond(HttpStatusCode.InternalServerError, "Internal server error")
            return
        }

        result.onSuccess {
            call.respond(HttpStatusCode.OK, it)
            return
        }
    }
}

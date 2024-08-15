package quiz.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import quiz.QuestionController

fun Application.configureRouting(questionController: QuestionController) {
    install(ContentNegotiation) { json() }

    routing() {
        get("/ping") { call.respondText("pong") }
        get("/questions") { questionController.getQuestions(call) }
        post("/answers") { questionController.getAnswers(call) }
    }
}

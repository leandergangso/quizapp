package quiz

import io.ktor.server.application.*
import io.ktor.server.netty.*
import quiz.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val questionRepo = QuestionRepositoryInMemory()
    val questionService = QuestionService(questionRepo)
    val questionController = QuestionController(questionService)
    configureRouting(questionController)
}

package com.kylix.plugins

import com.kylix.route.AuthRoute
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.title
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val authRoute by inject<AuthRoute>()

    routing {
        initRoute()
        authRoute.apply { this@routing.initRoute() }
    }
}

fun Route.initRoute() {
    get("/") {
        call.respondHtml {
            head {
                title { +"Ktor HTML DSL" }
            }
            body() {
                +"Hello from Ktor that using HTML DSL"
            }
        }
    }
}


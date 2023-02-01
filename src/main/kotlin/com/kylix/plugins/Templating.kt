package com.kylix.plugins

import io.ktor.server.html.*
import kotlinx.html.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureTemplating() {

    routing {
        get("/html-dsl") {
            call.respondHtml {
                head {
                    title { "Ktor JWT Auth" }
                }
                body {
                    h1 { "MBKM Bangsat" }
                }
            }
        }
    }
}

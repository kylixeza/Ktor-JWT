package com.kylix.route

import com.kylix.model.BaseListResponse
import com.kylix.model.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*

object RouteResponseHelper {
	
	suspend inline infix fun<reified T> ApplicationCall.buildSuccessJson(noinline action: suspend () -> T) {
		try {
			val data = action()
			this.respond(
				HttpStatusCode.OK,
				BaseResponse(
					HttpStatusCode.OK.value.toString(),
					"Success",
					if (data is Unit) "Nothing" else data
				)
			)
		} catch (e: Exception) {
			this@buildSuccessJson.buildErrorJson(e)
		}
	}
	
	suspend inline infix fun ApplicationCall.buildErrorJson(exception: Exception) {
		when(exception) {
			is BadRequestException -> this.respond(HttpStatusCode.BadRequest, BaseResponse(HttpStatusCode.BadRequest.value.toString(), exception.message.toString(), null))
			is NotFoundException -> this.respond(HttpStatusCode.NotFound, BaseResponse(HttpStatusCode.NotFound.value.toString(), exception.message.toString(), null))
			else -> this.respond(HttpStatusCode.Conflict, BaseResponse(HttpStatusCode.Conflict.value.toString(), exception.message.toString(), null))
		}
	}

	suspend inline fun ApplicationCall.buildErrorJson(httpStatusCode: HttpStatusCode = HttpStatusCode.Conflict, message: String) {
		this.respond(httpStatusCode, BaseResponse(httpStatusCode.value.toString(), message, null))
	}
	
	suspend inline infix fun<reified T> ApplicationCall.buildSuccessJsonList(noinline action: suspend () -> T) {
		try {
			val count = count { action() as List<*> }
			this.respond(
				HttpStatusCode.OK,
				BaseListResponse(
					HttpStatusCode.OK.value.toString(),
					"Request Success",
					count,
					action()
				)
			)
			
		} catch (e: Exception) {
			this@buildSuccessJsonList.buildErrorJsonList(e)
		}
	}
	
	suspend inline infix fun ApplicationCall.buildErrorJsonList(e: Exception) {
		val listResponse = BaseListResponse(message = e.message.toString(), count = 0, data = arrayListOf<Any>())
		when(e) {
			is BadRequestException -> {
				listResponse.status = HttpStatusCode.BadRequest.value.toString()
				this.respond(
					HttpStatusCode.BadRequest,
					listResponse
				)
			}
			is NotFoundException -> {
				listResponse.status = HttpStatusCode.NotFound.value.toString()
				this.respond(
					HttpStatusCode.NotFound,
					listResponse
				)
			}
			else -> {
				listResponse.status = HttpStatusCode.Conflict.value.toString()
				this.respond(
					HttpStatusCode.NotFound,
					listResponse
				)
			}
		}
	}
	
	inline fun count(block: () -> List<*>) = block().size
}
package com.kylix.di

import com.kylix.controller.token.TokenController
import com.kylix.controller.token.TokenControllerImpl
import com.kylix.controller.user.UserController
import com.kylix.controller.user.UserControllerImpl
import com.kylix.data.DatabaseFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.module
import java.net.URI

val databaseModule = module {
	single {
		DatabaseFactory(get())
	}
	
	factory {
		val config = HikariConfig()
		config.apply {
			driverClassName = System.getenv("JDBC_DRIVER")
			isAutoCommit = false
			transactionIsolation = "TRANSACTION_REPEATABLE_READ"
			
			jdbcUrl = if(System.getenv("ENV") == "DEV") {
				System.getenv("DATABASE_URL")
			} else {
				val uri = URI(System.getenv("DATABASE_URL"))
				val username = uri.userInfo.split(":").toTypedArray()[0]
				val password = uri.userInfo.split(":").toTypedArray()[1]
				val sslLocation = "/src/main/resources/prod-ca-2021.crt"
				if(System.getenv("HAS_SSL") == "true") {
					"jdbc:postgresql://${uri.host}:${uri.port}${uri.path}?sslmode=verify-ca&sslcert=$sslLocation&user=$username&password=$password"
				} else {
					"jdbc:postgresql://${uri.host}:${uri.port}${uri.path}?sslmode=require&user=$username&password=$password"
				}
			}
			
			validate()
		}
		HikariDataSource(config)
	}
}

val controllerModule = module {
	single<UserController> {
		UserControllerImpl(get())
	}

	single<TokenController> {
		TokenControllerImpl(get())
	}
}
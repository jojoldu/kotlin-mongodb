package jojoldu.kotlin.mongodb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinMongodbApplication

fun main(args: Array<String>) {
    runApplication<KotlinMongodbApplication>(*args)
}

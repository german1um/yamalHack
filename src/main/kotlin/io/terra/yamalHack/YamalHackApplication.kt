package io.terra.yamalHack

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class YamalHackApplication

fun main(args: Array<String>) {
	SpringApplication.run(YamalHackApplication::class.java, *args)
}

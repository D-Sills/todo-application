package org.setu.todo.helpers

import mu.KotlinLogging
import java.io.File

val logger = KotlinLogging.logger {}

fun write(fileName: String, data: String) {
    val file = File(fileName)
    try {
        file.writeText(data)
    } catch (e: Exception) {
        logger.error { "Cannot write to file: $e" }
    }
}

fun read(fileName: String): String {
    val file = File(fileName)
    return try {
        file.readText()
    } catch (e: Exception) {
        logger.error { "Cannot read file: $e" }
        ""
    }
}

fun exists(fileName: String): Boolean {
    val file = File(fileName)
    return file.exists()
}

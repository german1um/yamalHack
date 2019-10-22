package io.terra.yamalHack.util

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import java.io.IOException

@Component
class CommandLineTool {

    private val logger = LoggerFactory.getLogger(CommandLineTool::class.java)

    @Throws(Exception::class)
    fun runTool(argument: String): String {
        try {

            // Run command
            val command = arrayOf("python", "src/main/resources/scripts/face_detect.py", argument)
            val cwlToolProcess = ProcessBuilder(*command)
            val process = cwlToolProcess.start()

            // Read output from the process using threads
            val inputGobbler = StreamGobbler(process.inputStream)
            val errorGobbler = StreamGobbler(process.errorStream)
            errorGobbler.start()
            inputGobbler.start()

            // Wait for process to complete
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                inputGobbler.join()
                return inputGobbler.content
            } else {
                errorGobbler.join()
                throw Exception(errorGobbler.content)
            }
        } catch (e: IOException) {
            logger.error("Error running tool", e)
            throw Exception("Error running tool")
        } catch (e: InterruptedException) {
            logger.error("Error running tool", e)
            throw Exception("Error running tool")
        }

    }
}

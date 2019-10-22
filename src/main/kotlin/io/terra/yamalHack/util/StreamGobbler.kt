package io.terra.yamalHack.util

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Background thread to consume stream and
 * collect contents
 */
class StreamGobbler(private val `is`: InputStream) : Thread() {
    private val lineSeparator = System.getProperty("line.separator")
    var content = ""
        private set

    override fun run() {
        try {
            val isr = InputStreamReader(`is`)
            val br = BufferedReader(isr)
            var line = br.readLine()
            while (line != null) {
                content += line + lineSeparator
                line = br.readLine()
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

    }
}
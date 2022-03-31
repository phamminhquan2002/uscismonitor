package com.example.uscismonitor

import java.io.BufferedReader
import java.io.InputStreamReader

object CommonFunction {

    fun runCMD(cmd:Array<String>): StringBuilder {
        val output = StringBuilder()
        val process = Runtime.getRuntime().exec(cmd)
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        var resultString = reader.readLine()
        while (resultString != null){
            output.append("$resultString\n")
            resultString = reader.readLine()
        }
        println(output)
        return output
    }
}
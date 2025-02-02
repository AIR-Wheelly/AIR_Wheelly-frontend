package com.air_wheelly.wheelly.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateFormatter {
    fun toLocalDate(date: String = ""): String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val readableFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        val parsedDate = LocalDate.parse(date, formatter)
        return parsedDate.format(readableFormatter)
    }
}
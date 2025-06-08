package com.gauravbajaj.newsapp.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Formats a date string from the API into a more readable format.
 * @param dateString The date string in "yyyy-MM-dd'T'HH:mm:ss'Z'" format
 * @return Formatted date string in "MMM d, yyyy" format (e.g., "Jun 7, 2023")
 */
fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString)
        
        val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()
        outputFormat.format(date ?: return "")
    } catch (e: Exception) {
        ""
    }
}

/**
 * Extension function to format a Date object to a string.
 * @param format The format string (e.g., "MMM d, yyyy")
 * @param locale The locale to use for formatting (defaults to system default)
 * @return Formatted date string
 */
fun Date.formatToString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

/**
 * Extension function to parse a string to a Date object.
 * @param format The format string (e.g., "yyyy-MM-dd'T'HH:mm:ss'Z'")
 * @param locale The locale to use for parsing (defaults to system default)
 * @return Parsed Date object or null if parsing fails
 */
fun String.toDate(format: String, locale: Locale = Locale.getDefault()): Date? {
    return try {
        SimpleDateFormat(format, locale).parse(this)
    } catch (e: Exception) {
        null
    }
}

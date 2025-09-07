package com.example.mechat.core.resources

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

inline fun <reified T> T.toMap(): Map<String, Any?> {
    val json = Json.encodeToJsonElement(this).jsonObject
    return json.mapValues { it.value.toString().trim('"') }
}

fun String.toCapitalise() : String {
   return this[0].uppercase() + this.substring(1)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long.formatTime(): String {
    val instant = Instant.ofEpochMilli(this)

    val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
        .withZone(ZoneId.systemDefault()) // device timezone

    return formatter.format(instant)
}
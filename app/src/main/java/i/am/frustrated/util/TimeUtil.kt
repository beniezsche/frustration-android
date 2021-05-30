package i.am.frustrated.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    fun minusToMillis(min: Int): Long {
        return (min*60*1000).toLong()
    }

    fun hoursToMillis(hours: Int): Long {
        return minusToMillis(hours * 60)
    }

    fun timestampToHours(timestamp: String): String {

        if (timestamp != null) {

            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            format.timeZone = TimeZone.getTimeZone("UTC")

            val postDate = format.parse(timestamp)
            val currentDate = Date()

            val timeDiff = currentDate.time - postDate.time

            val seconds = (timeDiff/1000).toInt()
            val minutes = (timeDiff/1000/60).toInt()
            val hours = (timeDiff/1000/60/60).toInt()

            return when {
                hours > 0 -> {
                    "$hours hrs"
                }
                minutes > 0 -> {
                    "$minutes mins"
                }
                else -> {
                    "$seconds secs"
                }
            }
        }
        else {
            return "god knows when"
        }
    }
}
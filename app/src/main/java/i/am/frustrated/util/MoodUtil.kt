package i.am.frustrated.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import i.am.frustrated.R

@SuppressLint("UseCompatLoadingForDrawables")
object MoodUtil {

    fun getPrimaryColorFromMood(context: Context, mood: Int): Int {

        when (mood) {

            context.resources.getInteger(R.integer.mood_filter_sad) -> {
                return ContextCompat.getColor(context,R.color.mood_sad_primary_color)
            }

            context.resources.getInteger(R.integer.mood_filter_ennui) -> {
                return ContextCompat.getColor(context,R.color.mood_ennui_primary_color)
            }

            context.resources.getInteger(R.integer.mood_filter_happy) -> {
                return ContextCompat.getColor(context,R.color.mood_happy_primary_color)
            }

            context.resources.getInteger(R.integer.mood_filter_raging) -> {
                return ContextCompat.getColor(context,R.color.mood_raging_primary_color)
            }

        }

        return ContextCompat.getColor(context,R.color.bg_purple)
    }

    fun getSecondaryColorFromMood(context: Context,mood: Int): Int {

        when (mood) {

            context.resources.getInteger(R.integer.mood_filter_sad) -> {
                return ContextCompat.getColor(context,R.color.mood_sad_secondary_color)
            }

            context.resources.getInteger(R.integer.mood_filter_ennui) -> {
                return ContextCompat.getColor(context,R.color.mood_ennui_secondary_color)
            }

            context.resources.getInteger(R.integer.mood_filter_happy) -> {
                return ContextCompat.getColor(context,R.color.mood_happy_secondary_color)
            }

            context.resources.getInteger(R.integer.mood_filter_raging) -> {
                return ContextCompat.getColor(context,R.color.mood_raging_secondary_color)
            }

        }

        return ContextCompat.getColor(context,R.color.bg_purple)

    }

//    fun getLikeEmojiDrawableForMoodInt(context: Context, mood: Int): Int {
//
//        when(mood) {
//
//            context.resources.getInteger(R.integer.mood_filter_sad) -> {
//                return R.drawable.mood_sad
//            }
//
//            context.resources.getInteger(R.integer.mood_filter_ennui) -> {
//                return R.drawable.mood_ennui
//            }
//
//            context.resources.getInteger(R.integer.mood_filter_happy) -> {
//                return R.drawable.mood_happy
//            }
//
//            context.resources.getInteger(R.integer.mood_filter_raging) -> {
//                return R.drawable.mood_angry
//            }
//
//        }
//
//        return 0
//
//    }

    fun getLikeEmojiDrawableForMood(context: Context, mood: Int): Drawable? {

        when(mood) {

            context.resources.getInteger(R.integer.mood_filter_sad) -> {
                return context.getDrawable(R.drawable.mood_sad)
            }

            context.resources.getInteger(R.integer.mood_filter_ennui) -> {
                return context.getDrawable(R.drawable.mood_ennui)
            }

            context.resources.getInteger(R.integer.mood_filter_happy) -> {
                return context.getDrawable(R.drawable.mood_happy)
            }

            context.resources.getInteger(R.integer.mood_filter_raging) -> {
                return context.getDrawable(R.drawable.mood_angry)
            }

        }

        return context.getDrawable(R.drawable.background_tab_layout_selected)

    }

//    fun getUnlikeEmojiDrawableForMoodInt(context: Context, mood: Int): Int {
//
//        when(mood) {
//
//            context.resources.getInteger(R.integer.mood_filter_sad) -> {
//                return R.drawable.mood_sad_unliked
//            }
//
//            context.resources.getInteger(R.integer.mood_filter_ennui) -> {
//                return R.drawable.mood_ennui_unliked
//            }
//
//            context.resources.getInteger(R.integer.mood_filter_happy) -> {
//                return R.drawable.mood_happy_unliked
//            }
//
//            context.resources.getInteger(R.integer.mood_filter_raging) -> {
//                return R.drawable.mood_angry_unliked
//            }
//
//        }
//
//        return Int.MAX_VALUE
//
//    }


    fun getUnlikeEmojiDrawableForMood(context: Context, mood: Int): Drawable? {

        when(mood) {

            context.resources.getInteger(R.integer.mood_filter_sad) -> {
                return context.getDrawable(R.drawable.mood_sad_unliked)
            }

            context.resources.getInteger(R.integer.mood_filter_ennui) -> {
                return context.getDrawable(R.drawable.mood_ennui_unliked)
            }

            context.resources.getInteger(R.integer.mood_filter_happy) -> {
                return context.getDrawable(R.drawable.mood_happy_unliked)
            }

            context.resources.getInteger(R.integer.mood_filter_raging) -> {
                return context.getDrawable(R.drawable.mood_angry_unliked)
            }

        }

        return context.getDrawable(R.drawable.background_tab_layout_selected)

    }

}
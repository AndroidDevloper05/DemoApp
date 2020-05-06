package com.demo.util
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.demo.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * An object class which contains all the common utility methods used in whole app.
 */
object Utility {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS: Int = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS: Int = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS: Int = 24 * HOUR_MILLIS

    /**
     * A method which returns the state of internet connectivity of user's phone.
     */
    @Suppress("DEPRECATION")
    fun hasInternet(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }

    /**
     * Hide key board from screen
     */
    fun hideKeyBoard(context: Context, view: View) {
        val inputMethodManager: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    /**
     * This function is used to show toast message for long time
     * @param message String message to be displayed
     */
    fun Context.showLongToast(message: CharSequence): Toast {
        return Toast.makeText(this, message, Toast.LENGTH_LONG).apply { show() }
    }

    @JvmStatic
    fun getTimeAgo(context:Context,createdAt: String): String? {
        var time: Long
        try {
            val sdf = SimpleDateFormat(Constant.DATE_TIME_SECOND)
            val date: Date = sdf.parse(createdAt)!!
            time = date.time
        } catch (e: ParseException) {
            e.printStackTrace()
            return "-"
        }
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }
        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> {
                context.getString(R.string.just_now)
            }
            diff < 2 * MINUTE_MILLIS -> {
                context.getString(R.string.a_min_ago)
            }
            diff < 50 * MINUTE_MILLIS -> {
                (diff / MINUTE_MILLIS).toString() +context.getString(R.string.mins_ago)
            }
            diff < 90 * MINUTE_MILLIS -> {
                context.getString(R.string.an_hour_ago)
            }
            diff < 24 * HOUR_MILLIS -> {
                (diff / HOUR_MILLIS).toString() +context.getString(R.string.hours_ago)
            }
            diff < 48 * HOUR_MILLIS -> {
                context.getString(R.string.yesterday)
            }
            else -> {
                (diff / DAY_MILLIS).toString()+context.getString(R.string.day_ago)
            }
        }
    }
    @JvmStatic
    fun getFormattedCount(count:Int):String {
        return when {
            abs(count / 1000000) > 1 -> {
                (count / 1000000).toString() + "m"
            }
            abs(count / 1000) > 1 -> {
                (count / 1000).toString() + "k"
            }
            else -> {
                return count.toString()
            }
        }
    }

}
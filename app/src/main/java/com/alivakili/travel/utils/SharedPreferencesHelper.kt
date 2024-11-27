package com.alivakili.travel.utils

import android.content.Context
import com.alivakili.travel.models.BookingModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesHelper {

    private const val PREF_NAME = "my_prefs"
    private const val KEY_BOOKING_LIST = "bookingList"

    private fun getSharedPreferences(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * Saves a list of BookingModel objects to SharedPreferences.
     */
    fun saveBookingList(context: Context, bookingList: ArrayList<BookingModel>) {
        val gson = Gson()
        val json = gson.toJson(bookingList)
        getSharedPreferences(context).edit().putString(KEY_BOOKING_LIST, json).apply()
    }

    /**
     * Retrieves a list of BookingModel objects from SharedPreferences.
     */
    fun getBookingList(context: Context): ArrayList<BookingModel> {
        val gson = Gson()
        val json = getSharedPreferences(context).getString(KEY_BOOKING_LIST, null)

        // Use TypeToken to handle deserialization of a list of BookingModel objects
        val type = object : TypeToken<ArrayList<BookingModel>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }

    /**
     * Clears the stored booking list from SharedPreferences.
     */
    fun clearBookingList(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_BOOKING_LIST).apply()
    }
}

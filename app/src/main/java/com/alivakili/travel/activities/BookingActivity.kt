package com.alivakili.travel.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alivakili.travel.R
import com.alivakili.travel.databinding.ActivityBookingBinding
import com.alivakili.travel.models.BookingModel
import com.alivakili.travel.utils.SharedPreferencesHelper
import java.util.Calendar

class BookingActivity : AppCompatActivity() {

    private val binding by lazy { ActivityBookingBinding.inflate(layoutInflater) }

    private lateinit var bookingList: ArrayList<BookingModel>
    private val activity = this

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize bookingList with SharedPreferences data or an empty list
        bookingList = SharedPreferencesHelper.getBookingList(activity)
        if (bookingList.isEmpty()) {
            bookingList = arrayListOf() // Ensure it starts as an empty list if no data exists
        }

        val cityNames = resources.getStringArray(R.array.city_names)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cityNames)

        binding.apply {
            actvSource.setAdapter(adapter)
            actvDestination.setAdapter(adapter)

            timePicker.setIs24HourView(true)
            var formattedTime: String = ""
            var formattedDate: String = ""

            // Set up filtering for partial matches
            setupPartialFiltering(actvSource, adapter)
            setupPartialFiltering(actvDestination, adapter)

            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                // Format the time as HH:mm and set it to etTime
                formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                etTime.setText(formattedTime)
            }

            datePicker.init(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ) { _, year, monthOfYear, dayOfMonth ->
                // Format the date as DD-MM-YYYY and set it to etDate
                formattedDate =
                    String.format("%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year)
                etDate.setText(formattedDate)
            }

            mbtnBook.setOnClickListener {

                if (isFormValid()) {
                    // Proceed with booking logic (e.g., save the data, etc.)
                    bookingList.add(
                        BookingModel(
                            formattedTime,
                            formattedDate,
                            etPassengerCount.text.toString(),
                            actvSource.text.toString(),
                            actvDestination.text.toString(),
                            etDuration.text.toString()
                        )
                    )
                    finish()

                    Toast.makeText(activity, "Booking Done Successfully", Toast.LENGTH_SHORT).show()
                    SharedPreferencesHelper.saveBookingList(activity, bookingList)
                } else {
                    // Show a Toast if any EditText is empty
                    Toast.makeText(activity, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                }




            }
        }
    }


    private fun isFormValid(): Boolean {
        return !(TextUtils.isEmpty(binding.etPassengerCount.text) ||
                TextUtils.isEmpty(binding.etTime.text) ||
                TextUtils.isEmpty(binding.etDate.text) ||
                TextUtils.isEmpty(binding.actvSource.text) ||
                TextUtils.isEmpty(binding.actvDestination.text) ||
                TextUtils.isEmpty(binding.etDuration.text))
    }

    private fun setupPartialFiltering(
        autoCompleteTextView: AutoCompleteTextView,
        adapter: ArrayAdapter<String>
    ) {
        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}

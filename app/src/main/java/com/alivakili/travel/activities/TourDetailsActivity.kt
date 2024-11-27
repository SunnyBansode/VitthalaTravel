package com.alivakili.travel.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alivakili.travel.R
import com.alivakili.travel.databinding.ActivityTourDetailsBinding
import com.alivakili.travel.models.TourModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class TourDetailsActivity : AppCompatActivity() {
    private var tour: TourModel.Tour? = null
    private lateinit var binding: ActivityTourDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTourDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureToolbar("Tour Details")
        tour = properData()
        properLayout()

        // Handle "Book Now" button click
        binding.bookButton.setOnClickListener {
            bookTour()
        }
    }

    private fun properLayout() {
        binding.apply {
            tourDetailsTV.text = tour?.details
            tourLocationTV.text = tour?.location
            tourPriceTV.text = "${tour?.price} Rs."
            tourNameTV.text = tour?.title
            tourDurationTV.text = "${tour?.days} days"
        }
        loadImage(tour?.image, this)
    }

    private fun loadImage(url: String?, context: Context) {
        Picasso.with(context).load(url).fit().centerCrop()
            .placeholder(R.drawable.tour_error)
            .error(R.drawable.tour_error)
            .into(binding.tourImageIV)
    }

    private fun properData(): TourModel.Tour? {
        return intent.extras!!.getParcelable("KEY_TOUR")
    }

    private fun configureToolbar(title: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            this.title = title
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun bookTour() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to book the tour", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance()
        val bookingsRef = database.getReference("bookings")

        // Create a BookingActivity object
        val booking = mapOf(
            "userId" to currentUser.uid,
            "userEmail" to currentUser.email,
            "tourTitle" to tour?.title,
            "tourLocation" to tour?.location,
            "tourPrice" to tour?.price,
            "tourDuration" to tour?.days,
            "bookingTime" to System.currentTimeMillis() // Save BookingActivity time
        )

        // Save BookingActivity to Firebase
        bookingsRef.push().setValue(booking).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Tour booked successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Failed to book tour: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

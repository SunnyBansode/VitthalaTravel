package com.alivakili.travel.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alivakili.travel.adapters.BookingDisplayAdapter
import com.alivakili.travel.databinding.BookingFragmentBinding
import com.alivakili.travel.models.BookingModel
import com.alivakili.travel.utils.SharedPreferencesHelper

class BookingFragment : Fragment() {
    private lateinit var bookingList: ArrayList<BookingModel>
    private lateinit var adapter: BookingDisplayAdapter
    private var _binding: BookingFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BookingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize bookingList and adapter
        bookingList = ArrayList()
        adapter = BookingDisplayAdapter(requireContext() as FragmentActivity, bookingList)

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // Retrieve and populate bookingList
        val retrievedBookingList = SharedPreferencesHelper.getBookingList(context as Activity)
        bookingList.clear()
        bookingList.addAll(retrievedBookingList.map {
            BookingModel(
                it.time,
                it.date,
                it.noOfPassangers,
                it.sourceCity,
                it.destinationCity,
                it.daysDuration
            )
        })

        // Log and notify adapter
        Log.d("DEBUG", "Booking list size: ${bookingList.size}")
        bookingList.forEach {
            Log.d(
                "VISHAY",
                "Booking: ${it.sourceCity} to ${it.destinationCity} on ${it.date} at ${it.time}"
            )
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

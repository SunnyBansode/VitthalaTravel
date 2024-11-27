package com.alivakili.travel.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.alivakili.travel.databinding.BookingItemBinding
import com.alivakili.travel.models.BookingModel


class BookingDisplayAdapter(
    private var context: FragmentActivity,
    private val bookingList: ArrayList<BookingModel> = ArrayList<BookingModel>()
) :
    RecyclerView.Adapter<BookingDisplayAdapter.ViewHolder>() {

    class ViewHolder(var binding: BookingItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: BookingModel, context: Context) {

            binding.apply {
                model.apply {

                    tvDate.text = date
                    tvDuration.text = daysDuration
                    tvPassangers.text = noOfPassangers
                    tvDestination.text = destinationCity
                    tvSourceDestination.text = sourceCity
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            BookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun getItemCount() = bookingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = bookingList[position]
        holder.bind(model, context)


    }
}
package com.example.round1_newwave.data

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.round1_newwave.databinding.ItemLocationBinding

class LocationAdapter(
    private var locations: List<Response.Item>,
    private val onLocationClick: (Response.Item.Position) -> Unit): RecyclerView.Adapter<LocationAdapter.LocationViewHolder>()
{
    class LocationViewHolder(private val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        private val titleTextView: TextView = binding.locationTitle

        fun bind(location: Response.Item, onItemClick: (Response.Item.Position) -> Unit) {
            titleTextView.text = location.title
            itemView.setOnClickListener {
                onItemClick(location.position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationAdapter.LocationViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationAdapter.LocationViewHolder, position: Int) {
        val location = locations[position]
        holder.bind(location, onLocationClick)
    }

    override fun getItemCount() = locations.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateLocations(newLocations: List<Response.Item>) {
        locations = newLocations
        notifyDataSetChanged()
    }
}
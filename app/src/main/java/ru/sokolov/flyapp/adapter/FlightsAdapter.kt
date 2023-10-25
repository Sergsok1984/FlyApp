package ru.sokolov.flyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.sokolov.flyapp.R
import ru.sokolov.flyapp.databinding.CardAirTravelBinding
import ru.sokolov.flyapp.dto.Flight
import ru.sokolov.flyapp.utils.TransformationData


class FlightsAdapter(
    private val onDetails: (flight: Flight) -> Unit
) : ListAdapter<Flight, FlightViewHolder>(
    FlightDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val binding = CardAirTravelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FlightViewHolder(
            binding = binding,
            onDetails = onDetails
        )
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class FlightViewHolder(
    private val binding: CardAirTravelBinding,
    private val onDetails: (flight: Flight) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(flight: Flight) {
        with(binding) {
            val departureStr = "${flight.startCity}\n${
                TransformationData.convertDatePublished(
                    flight.startDate
                )
            }"
            val arrivalStr =
                "${flight.endCity}\n${TransformationData.convertDatePublished(flight.endDate)}"
            val priceStr = "${flight.price}\u20BD"

            departure.text = departureStr
            arrival.text = arrivalStr
            price.text = priceStr

            favorite.setImageResource(
                if (flight.likedByMe) R.drawable.ic_like_filled_48
                else R.drawable.ic_like_outlined_48
            )

            details.setOnClickListener {
                onDetails(flight)
            }

            root.setOnLongClickListener {
                onDetails(flight)
                true
            }
        }
    }
}

class FlightDiffCallback : DiffUtil.ItemCallback<Flight>() {

    override fun areItemsTheSame(oldItem: Flight, newItem: Flight): Boolean =
        oldItem.searchToken == newItem.searchToken

    override fun areContentsTheSame(oldItem: Flight, newItem: Flight): Boolean = oldItem == newItem
}

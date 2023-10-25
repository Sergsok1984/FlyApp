package ru.sokolov.flyapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.sokolov.flyapp.R
import ru.sokolov.flyapp.databinding.AirTravelDetailsBinding
import ru.sokolov.flyapp.utils.TransformationData.convertDatePublished
import ru.sokolov.flyapp.viewmodel.AirTravelViewModel

@AndroidEntryPoint
class AirTravelDetails : Fragment() {

    private lateinit var binding: AirTravelDetailsBinding

    private val viewModel: AirTravelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AirTravelDetailsBinding.inflate(layoutInflater)
        val searchToken = arguments?.getString("searchTokenArg", "")
        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest { list ->
                list.find { it.searchToken == searchToken }?.let { flight ->
                    with(binding) {
                        val departureAirportText =
                            "${flight.startLocationCode} - ${flight.startCity}"
                        val arrivalAirportText = "${flight.endLocationCode} - ${flight.endCity}"
                        val priceStr = "${flight.price}\u20BD"

                        departureAirport.text = departureAirportText
                        departureDate.text = convertDatePublished(flight.startDate)

                        arrivalAirport.text = arrivalAirportText
                        returnDate.text = convertDatePublished(flight.endDate)

                        priceFlight.text = priceStr

                        favoriteFlights.setImageResource(
                            if (flight.likedByMe) R.drawable.ic_like_filled_48
                            else R.drawable.ic_like_outlined_48
                        )

                        favoriteFlights.setOnClickListener {
                            viewModel.like(flight)
                        }
                    }
                }
            }
        }
        binding.backOnList.setOnClickListener {
            findNavController().navigate(R.id.action_airTravelDetails_to_listOfFlightsFragment)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_airTravelDetails_to_listOfFlightsFragment)
                }
            }
        )
        return binding.root
    }
}

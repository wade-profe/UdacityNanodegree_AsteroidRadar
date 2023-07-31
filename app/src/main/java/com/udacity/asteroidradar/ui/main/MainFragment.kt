package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = AsteroidListAdapter { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        }

        requireActivity().addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.show_saved_menu -> {
                        viewModel.updateFilter(FilterValues.SAVED)
                        true
                    }

                    R.id.show_today_menu -> {
                        viewModel.updateFilter(FilterValues.DAY)
                        true
                    }

                    R.id.show_week_menu -> {
                        viewModel.updateFilter(FilterValues.WEEK)
                        true
                    }

                    else -> false
                }
            }

        })

        viewModel.imageOfTheDay.observe(viewLifecycleOwner) {
            Picasso.with(requireContext()).load(it?.url)
                .placeholder(R.drawable.placeholder_picture_of_day)
                .into(binding.activityMainImageOfTheDay)
        }

        return binding.root
    }
}

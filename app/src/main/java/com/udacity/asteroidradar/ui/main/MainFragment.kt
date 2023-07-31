package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = AsteroidListAdapter { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        }

        setHasOptionsMenu(true)

        viewModel.imageOfTheDay.observe(viewLifecycleOwner) {
            Picasso.with(requireContext()).load(it?.url)
                .placeholder(R.drawable.placeholder_picture_of_day)
                .into(binding.activityMainImageOfTheDay)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.show_saved_menu -> {
                viewModel.updateFilter(FilterValues.SAVED)}
            R.id.show_today_menu -> {
                viewModel.updateFilter(FilterValues.DAY)
            }
            R.id.show_week_menu -> {
                viewModel.updateFilter(FilterValues.WEEK)
            }
        }
        return true
    }
}

package com.vishwajeet.listeddash.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishwajeet.listeddash.databinding.FragmentRecentLinksBinding
import com.vishwajeet.listeddash.ui.dasboard.DashboardViewModel
import com.vishwajeet.listeddash.utils.NetworkResult

class RecentLinks : Fragment() {

    private val sharedDashboardViewModel by activityViewModels<DashboardViewModel>()

    private var _binding: FragmentRecentLinksBinding? = null

    private val binding get() = _binding!!
    private lateinit var adapter: RecentLinkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecentLinksBinding.inflate(inflater, container, false)

        adapter = RecentLinkAdapter()


        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recentRecycler.setHasFixedSize(true)
        binding.recentRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        binding.recentRecycler.adapter = adapter


        bindObservers()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun bindObservers() {
        sharedDashboardViewModel.dashboardLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    Log.d(
                        "dashRespFROM",
                        it.data?.data?.overall_url_chart?.keys.toString()
                    )
                    adapter.submitList(it.data?.data?.recent_links)
                }

                is NetworkResult.Error -> {
                    Log.d("dashResp", it.toString())
                }

                is NetworkResult.Loading -> {
                }
            }
        })
    }


}
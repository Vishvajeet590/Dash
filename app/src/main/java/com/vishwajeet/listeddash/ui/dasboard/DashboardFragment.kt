package com.vishwajeet.listeddash.ui.dasboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayout
import com.vishwajeet.listeddash.R
import com.vishwajeet.listeddash.databinding.FragmentDashboardBinding
import com.vishwajeet.listeddash.models.DashboardResponse
import com.vishwajeet.listeddash.models.Stats
import com.vishwajeet.listeddash.ui.LinkTabAdapter
import com.vishwajeet.listeddash.ui.StatAdapter
import com.vishwajeet.listeddash.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val dashboardViewModel by activityViewModels<DashboardViewModel>()

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!


    private var chart: LineChart? = null

    private lateinit var adapter: StatAdapter
    private lateinit var linkAdapter: LinkTabAdapter


    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        adapter = StatAdapter()


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel.getDashboardResponse()
        dashboardViewModel.greetMsg()

        binding.greet.text = dashboardViewModel.currentGreet.value
        chart = binding.chart

        binding.statRecycler.setHasFixedSize(true)
        binding.statRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.statRecycler.adapter = adapter


        //Recent TABS
        tabLayout = binding.linkTab
        viewPager2 = binding.linkViewpager
        linkAdapter = LinkTabAdapter(parentFragmentManager, lifecycle)
        tabLayout.addTab(tabLayout.newTab().setText("Recent Links"))
        tabLayout.addTab(tabLayout.newTab().setText("Top Links"))
        viewPager2.adapter = linkAdapter
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        //OBSERVING the response

        bindObservers()
    }


    private fun bindObservers() {
        dashboardViewModel.dashboardLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    setupChart(it)

                    //Creating Stat recycler data
                    var statList = ArrayList<Stats>()
                    statList.add(
                        Stats(
                            name = "total_clicks",
                            value = it.data?.total_clicks.toString()
                        )
                    )
                    statList.add(
                        Stats(
                            name = "today_clicks",
                            value = it.data?.today_clicks.toString()
                        )
                    )
                    statList.add(
                        Stats(
                            name = "top_location",
                            value = it.data?.top_location.toString()
                        )
                    )
                    statList.add(Stats(name = "top_source", value = it.data?.top_source.toString()))
                    adapter.submitList(statList)

                }

                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                    Log.d("dashResp", it.toString())
                }

                is NetworkResult.Loading -> {
                    //We can add spinner here
                }
            }
        })
    }


    fun setupChart(it: NetworkResult<DashboardResponse>) {
        val entries = ArrayList<Entry>()
        var labels = it.data?.data?.overall_url_chart?.keys
        val xValues = ArrayList<String>()

        chart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        chart?.axisRight?.isEnabled = false
        chart?.legend?.isEnabled = false
        chart?.setPinchZoom(true)
        chart?.setTouchEnabled(true)
        chart?.setDescription("")

        //preparing Chart data
        for ((c, label) in labels!!.withIndex()) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = formatter.parse(label)
            val desiredFormat = DateTimeFormatter.ofPattern("MMM dd").format(date)
            xValues.add(desiredFormat)
            entries.add(Entry(it.data?.data?.overall_url_chart?.get(label)?.toFloat()!!, c))
        }


        val lineSet = LineDataSet(entries, "data")
        lineSet.color = resources.getColor(R.color.chartColor)
        lineSet.setDrawFilled(true)
        lineSet.fillAlpha = 60
        lineSet.setDrawCubic(true)
        lineSet.lineWidth = 3f
        lineSet.setDrawCircles(false)
        lineSet.setDrawValues(false)


        val data = LineData(xValues, lineSet)
        chart?.data = data
        chart?.animateXY(3000, 3000)
    }
}
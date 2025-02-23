package com.sam.mylife.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import com.sam.mylife.R
import com.sam.mylife.core.model.HomeResponse
import com.sam.mylife.core.model.MonthItemModel
import com.sam.mylife.data.DataViewModel
import com.sam.mylife.databinding.FragmentHomeBinding
import com.sam.mylife.ui.adapter.HomeRecordAdapter
import com.sam.mylife.ui.base.BaseFragment
import com.sam.mylife.utils.listeners.OnClickItemListeners
import java.util.Calendar


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var recordAdapter: HomeRecordAdapter
    private val viewModel by viewModels<DataViewModel>()
    private lateinit var currentSelectedYear:String

    override fun initView() {
        currentSelectedYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        showLoading()

        setupRecycler()

        requestFetchData()

        observeData()

        observeCustomYear()

        binding.llSwipe.setOnRefreshListener {
            showLoading()
            requestFetchData()
        }

    }

    private fun requestFetchData() {
        viewModel.fetchYearData(currentSelectedYear)
    }

    private fun observeData() {
        viewModel.homeResponseLiveData.observe(this) { data ->
            hideProgress()
            binding.llSwipe.isRefreshing=false
            if (data.isNotEmpty()) {
            showDataView(data)
        } else {
            setupErrorAnimation()
        }
        }
    }

    private fun observeCustomYear(){
        sharedViewModel.selectedyear.observe(viewLifecycleOwner) { selectedYear ->
            showLoading()
            currentSelectedYear=selectedYear
           viewModel.fetchYearData(selectedYear)
            Log.d("BaseFragment", "Received data from ViewModel: $selectedYear")
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun showDataView(data: ArrayList<HomeResponse>) {
        binding.llSwipe.visibility = View.VISIBLE
        binding.llEmptyView.visibility = View.GONE
        recordAdapter.apply {
            addData(data)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecycler() {
        recordAdapter = HomeRecordAdapter(requireContext())
        binding.homeRecyclerview.adapter = recordAdapter
        recordAdapter.notifyDataSetChanged()

        recordAdapter.setListeners(object : OnClickItemListeners {
            override fun onItemClick(data: Any) {

            }
            override fun onItemClick(data: Any, docId: String, v: View) {
                showPopupMenu(v, data as MonthItemModel, docId)
            }
        })
    }

    private fun showPopupMenu(view: View, data: MonthItemModel, docId: String) {
        val popup = PopupMenu(requireContext(), view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.home_menu, popup.menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.menu_update -> {
                    val bundle = Bundle()
                    bundle.putString("id", data.id)
                    bundle.putString("item", data.item)
                    bundle.putString("date", data.date)
                    bundle.putString("month", data.month)
                    bundle.putString("token", data.token)
                    bundle.putString("docId", docId)
                    openBottomSheet(bundle)
                    //findNavController().navigate(R.id.addRecordFragment,bundle)
                }

                R.id.menu_delete -> {

                }
            }
            true
        }


        popup.show()
    }

    private fun setupErrorAnimation() {
        binding.llSwipe.visibility = View.GONE
        binding.llEmptyView.visibility = View.VISIBLE

        val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        binding.emptyMessage.startAnimation(shake)

        val scale = AnimationUtils.loadAnimation(requireContext(), R.anim.scale)
        binding.emptyImage.startAnimation(scale)
    }
}
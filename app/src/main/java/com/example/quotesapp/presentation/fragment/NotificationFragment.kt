package com.example.quotesapp.presentation.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentNotificationBinding
import com.example.quotesapp.presentation.adapter.NotificationAdapter
import com.example.quotesapp.presentation.viewModel.NotificationViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.NotificationItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(), NotificationItemClickListener {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNotificationBinding =
        FragmentNotificationBinding::inflate

    val viewModel by viewModels<NotificationViewModel>()

    override fun setup() {
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.showNotificationCount(savedPrefManager.getToken().toString())
        viewModel.getNotificationCountResponse.observe(viewLifecycleOwner){
            if ((it.data ?: 0) > 0)
                viewModel.updateNotificationCount(savedPrefManager.getToken().toString())
            else
                viewModel.showNotification(savedPrefManager.getToken().toString())
        }
        viewModel.updateNotificationCountResponse.observe(viewLifecycleOwner){
            viewModel.showNotification(savedPrefManager.getToken().toString())
        }

        viewModel.notificationResonse.observe(viewLifecycleOwner){
            binding?.noNotif?.isVisible = it.data.size<=0
            binding?.rvNotification?.adapter = NotificationAdapter(requireContext(),it.data,this)
        }
        viewModel.acceptRequest.observe(viewLifecycleOwner){
            viewModel.showNotification(savedPrefManager.getToken().toString())
        }
        viewModel.denyRequest.observe(viewLifecycleOwner){
            viewModel.showNotification(savedPrefManager.getToken().toString())
        }

    }

    override fun clicked(id: String, state: String) {
        if (state==Constants.ACCEPT)
            viewModel.acceptRequest(id,savedPrefManager.getToken().toString())
        if (state==Constants.REJECT)
            viewModel.denyRequest(id,savedPrefManager.getToken().toString())
    }

    override fun onResume() {
        super.onResume()
        val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
        view.visibility = View.GONE
    }

}
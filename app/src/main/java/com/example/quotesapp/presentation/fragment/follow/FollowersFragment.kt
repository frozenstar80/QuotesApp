package com.example.quotesapp.presentation.fragment.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentFollowersBinding
import com.example.quotesapp.presentation.adapter.FollowersFollowingAdapter
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.fragment.SearchPeopleFragmentDirections
import com.example.quotesapp.presentation.viewModel.FollowerViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.EventObserver
import com.example.quotesapp.util.FollowItemClickListener
import com.example.quotesapp.util.SearchUserItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : BaseFragment<FragmentFollowersBinding>(),FollowItemClickListener{
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFollowersBinding =
        FragmentFollowersBinding::inflate

    val viewModel by viewModels<FollowerViewModel>()

    override fun setup() {
        val id = arguments?.getString(Constants.USER_ID)

        viewModel.getFollowers(id.toString(),savedPrefManager.getToken().toString())
        viewModel.postLiveData.observe(viewLifecycleOwner){
            binding?.rvFollowersList?.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = FollowersFollowingAdapter(requireContext(),it.data,this@FollowersFragment)
            }
            binding?.txtCount?.text = "${it.data.size} ${getString(R.string.followers)}"
        }
        viewModel.removeLiveData.observe(viewLifecycleOwner){
            if (it.status==true)   viewModel.getFollowers(savedPrefManager.getId().toString(),savedPrefManager.getToken().toString())
        }
        viewModel.otherUserProfileLiveData.observe(this, EventObserver {
            if (it.status==true) {
                findNavController().navigate(HostFollowersFollowingFragmentDirections.actionHostFollowersFollowingFragmentToOtherUserProfileDetailsFragment(otherUserData = it.data))
            }else
            {
                toast(resources.getString(R.string.something_went_wrong))
            }
        })
    }



    private fun deleteUser(id: String) {

        val alertDialog = AlertDialog.Builder(requireContext())

        alertDialog.setTitle(getString(R.string.remove_follower))
        alertDialog.setMessage(getString(R.string.do_you_want_to_remove_this_user))
        alertDialog.setNegativeButton(
            getString(R.string.cancel)
        ) { _, _ ->
            toast(getString(R.string.cancelled))
        }
        alertDialog.setPositiveButton(getString(R.string.yes)){ _, _->
            viewModel.removeUser(id,savedPrefManager.getToken().toString())
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    override fun delete(id: String) {
        deleteUser(id)
    }

    override fun viewProfile(id: String) {
        viewModel.showUserDetails(id,savedPrefManager.getToken().toString())

    }
}
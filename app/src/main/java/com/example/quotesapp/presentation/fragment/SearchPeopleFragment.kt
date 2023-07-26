package com.example.quotesapp.presentation.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentSearchPeopleBinding
import com.example.quotesapp.presentation.adapter.RecentSearchAdapter
import com.example.quotesapp.presentation.adapter.SearchUsersAdapter
import com.example.quotesapp.presentation.viewModel.SearchPeopleViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.EventObserver
import com.example.quotesapp.util.SearchUserItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchPeopleFragment : BaseFragment<FragmentSearchPeopleBinding>(), SearchUserItemClickListener {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchPeopleBinding =
        FragmentSearchPeopleBinding::inflate

private val searchPeopleViewModel by viewModels<SearchPeopleViewModel>()

    override fun setup() {
        searchPeopleViewModel.recentUsers(savedPrefManager.getToken().toString())
        binding?.imgClearText?.setOnClickListener {
            binding?.txtSearchView?.setText("")
            binding?.imgClearText?.isVisible=false
        }
        searchPeopleViewModel.recentSearchLiveData.observe(viewLifecycleOwner){
            if (it.data?.users?.isNotEmpty() == true)
                binding?.txtRecentSearch?.text = resources.getString(R.string.recent_search)
            val recentSearchAdapter =
                it.data?.users?.let { it1 -> RecentSearchAdapter(requireContext(), it1,this) }
                binding?.rvRecentSearchedPeople?.apply {
                adapter = recentSearchAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
        var job: Job? = null
        binding?.txtSearchView?.addTextChangedListener {
            if (it?.isNotEmpty() == true) binding?.imgClearText?.isVisible = true
            else {
                binding?.txtNoOfPeopleFound?.isVisible = false
                binding?.rvSearchedPeople?.isVisible = false
                binding?.rvSearchedPeople?.apply {
                    val searchUsersAdapter = SearchUsersAdapter(requireContext(), arrayListOf(),this@SearchPeopleFragment)
                    adapter = searchUsersAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
                binding?.imgClearText?.isVisible = false
            }
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_PROFILE_TIME_DELAY)
                it?.let {
                    if(it.toString().isNotEmpty()) {
                        showProgressBar()
                        searchPeopleViewModel.searchPeople(it.toString(),savedPrefManager.getToken().toString())
                    }
                }
            }

        }
        searchPeopleViewModel.postLiveData.observe(viewLifecycleOwner){
            hideProgressBar()
            binding?.txtNoOfPeopleFound?.isVisible = true
            binding?.rvSearchedPeople?.isVisible = true
            binding?.txtNoOfPeopleFound?.text = "${resources.getString(R.string.search_result)} (${it.data.size})"
               val searchUsersAdapter = SearchUsersAdapter(requireContext(),it.data,this)
               binding?.rvSearchedPeople?.apply {
                   adapter = searchUsersAdapter
                   layoutManager = LinearLayoutManager(requireContext())
               }
        }
        searchPeopleViewModel.otherUserProfileLiveData.observe(this,EventObserver {
            if (it.status==true) {
                findNavController().navigate(SearchPeopleFragmentDirections.actionSearchPeopleFragmentToOtherUserProfileDetailsFragment(otherUserData = it.data))
            }else
            {
                toast(resources.getString(R.string.something_went_wrong))
            }
        })

    }

    private fun hideProgressBar() {
        binding?.progressBar?.isVisible = false
    }

    private fun showProgressBar() {
          binding?.progressBar?.isVisible = true
    }


    override fun onResume() {
        super.onResume()
        val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
        view.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
        view.visibility = View.VISIBLE

    }

    override fun clicked(id:String) {
        searchPeopleViewModel.showUserDetails(id,savedPrefManager.getToken().toString())
    }
}
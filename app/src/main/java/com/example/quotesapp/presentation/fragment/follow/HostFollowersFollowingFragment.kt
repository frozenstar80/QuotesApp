package com.example.quotesapp.presentation.fragment.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentFollowersFollowingHostBinding
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HostFollowersFollowingFragment : BaseFragment<FragmentFollowersFollowingHostBinding>(){
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFollowersFollowingHostBinding =
        FragmentFollowersFollowingHostBinding::inflate

    private lateinit var demoCollectionPagerAdapter: GuruSaiBabaPagerAdapter
    private  var viewPager: ViewPager? = null
    private val args by navArgs<HostFollowersFollowingFragmentArgs>()

    override fun setup() {
        val tabLayout = binding?.resultTabs
        demoCollectionPagerAdapter = GuruSaiBabaPagerAdapter(childFragmentManager,this,args.isNormalUser,args.isFollower,args.id.toString())
        viewPager = binding?.viewpager
        viewPager?.adapter = demoCollectionPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)
        binding?.txtTitle?.text = savedPrefManager.getFullName()
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}

class GuruSaiBabaPagerAdapter(
    fm: FragmentManager,
    private val hostFollowersFollowingFragment: HostFollowersFollowingFragment,
    private val normalUser: Boolean,
    private val follower: Boolean,
    private val id:String
) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = if(normalUser) 2 else 1

    override fun getItem(i: Int): Fragment {
        val args= Bundle();
        args.putString(Constants.USER_ID,id);
        val fragment = when(i){
            0 -> {
                if (follower) {
                    val followerFragment = FollowersFragment()
                    followerFragment.arguments = args
                    followerFragment
                }else{
                    val following = FollowingFragment()
                    following.arguments = args
                    following
                }
            }
            1 -> {
                if (!follower) {
                    val followerFragment = FollowersFragment()
                    followerFragment.arguments = args
                    followerFragment
                }else{
                    val following = FollowingFragment()
                    following.arguments = args
                    following
                }
            }
            else -> {
                val following = FollowingFragment()
                following.arguments = args
                following
            }
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        val title   = when(position){
            0 -> {
                if (follower) {
                    hostFollowersFollowingFragment.getString(R.string.followers)
                }else{
                    hostFollowersFollowingFragment.getString(R.string.following)
                }

            }
            1 -> {
                if (!follower) {
                    hostFollowersFollowingFragment.getString(R.string.followers)
                }else{
                    hostFollowersFollowingFragment.getString(R.string.following)
                }

            }
            else -> {
                hostFollowersFollowingFragment.getString(R.string.followers)
            }
        }
        return title
    }
}

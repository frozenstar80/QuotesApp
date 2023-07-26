package com.example.quotesapp.presentation.fragment.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentExchangeBinding
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeFragment : BaseFragment<FragmentExchangeBinding>(){
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentExchangeBinding =
        FragmentExchangeBinding::inflate

    private lateinit var demoCollectionPagerAdapter: ExchangeAdapter
    private  var viewPager: ViewPager? = null
    private val navArg by navArgs<ExchangeFragmentArgs>()

    override fun setup() {
        val tabLayout = binding?.resultTabs
        demoCollectionPagerAdapter = ExchangeAdapter(childFragmentManager,this,navArg.userType)
        viewPager = binding?.viewpager
        viewPager?.adapter = demoCollectionPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}

class ExchangeAdapter(
    fm: FragmentManager,
    private val exchangeFragment: ExchangeFragment,
    val userType:String
) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = if (userType=="memberplus") 1 else 2

    override fun getItem(i: Int): Fragment {
        val args= Bundle();
        args.putString(Constants.USER_TYPE,userType);
        val fragment = when(i){
            1 -> {
               val fragment = SentExchangeFragment()
                fragment.arguments = args
                fragment
            }
            0 -> {
                val fragment =ReceivedExchangeFragment()
                fragment.arguments = args
                fragment
            }
            else -> {
                val fragment =SentExchangeFragment()
                fragment.arguments = args
                fragment
            }
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        val title   = when(position){
            1 -> {
                exchangeFragment.getString(R.string.sent)
            }
            0 -> {
                exchangeFragment.getString(R.string.received)
            }
            else -> {
                exchangeFragment.getString(R.string.sent)
            }
        }
        return title
    }
}

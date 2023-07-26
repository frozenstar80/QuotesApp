package com.example.quotesapp.presentation.fragment.buriaModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentBurialHostBinding
import com.example.quotesapp.databinding.FragmentExchangeBinding
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.fragment.exchange.SentExchangeFragment
import com.example.quotesapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class BurialItemsHostFragment : BaseFragment<FragmentBurialHostBinding>(){
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBurialHostBinding =
        FragmentBurialHostBinding::inflate

    private lateinit var demoCollectionPagerAdapter: ExchangeAdapter
    private  var viewPager: ViewPager? = null
    private val navArg by navArgs<BurialItemsHostFragmentArgs>()

    override fun setup() {
        val tabLayout = binding?.resultTabs
        demoCollectionPagerAdapter = ExchangeAdapter(childFragmentManager,this,navArg.userType,navArg.id,navArg.itemType,navArg.isOtherProfile)
        viewPager = binding?.viewpager
        viewPager?.adapter = demoCollectionPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
        when (navArg.itemType) {
            Constants.BURIAL ->{
                binding?.txtTitle?.text = getString(R.string.burial)

            }
            Constants.MOTIVATION -> {
                binding?.txtTitle?.text = getString(R.string.motivation)
                binding?.appCompatImageView5?.setImageResource(R.drawable.motivation_bg)
            }
            Constants.LINKS -> binding?.txtTitle?.text = getString(R.string.links)
            Constants.IDEAS ->{
                binding?.txtTitle?.text = getString(R.string.ideas)
                binding?.appCompatImageView5?.setImageResource(R.drawable.ideas_bg)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
        view.visibility = View.GONE
    }
}

class ExchangeAdapter(
    fm: FragmentManager,
    private val burialItemsHostFragment: BurialItemsHostFragment,
    private val userType:String,
    private val id:String,
    private val itemItem:String,
    private val isOtherProfile:Boolean
) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int = if (itemItem==Constants.LINKS) 6 else 5

    override fun getItem(i: Int): Fragment {
        val args= Bundle();
        args.putString(Constants.USER_TYPE,userType);
        args.putString(Constants.USER_ID,id)
        args.putString(Constants.ITEM_TYPE,itemItem)
        args.putBoolean(Constants.IS_OTHER_PROFILE,isOtherProfile)
        val fragment = when(i){
            0 -> {
                val tab = when(itemItem){
                    Constants.BURIAL -> Constants.BURIAL1
                    Constants.MOTIVATION -> Constants.MOTIV1
                    Constants.LINKS -> Constants.LINKS1
                    Constants.IDEAS -> Constants.IDEAS1
                    else->Constants.BURIAL1
                }
                val fragment = BurialFragment()
                args.putString(Constants.TAB,tab)
                fragment.arguments = args
                fragment
            }
            1 -> {
                val tab = when(itemItem){
                    Constants.BURIAL -> Constants.BURIAL2
                    Constants.MOTIVATION -> Constants.MOTIV2
                    Constants.LINKS -> Constants.LINKS2
                    Constants.IDEAS -> Constants.IDEAS2
                    else->Constants.BURIAL2
                }
                val fragment = BurialFragment()
                args.putString(Constants.TAB,tab)
                fragment.arguments = args
                fragment
            }
            2 -> {
                val tab = when(itemItem){
                    Constants.BURIAL -> Constants.BURIAL3
                    Constants.MOTIVATION -> Constants.MOTIV3
                    Constants.LINKS -> Constants.LINKS3
                    Constants.IDEAS -> Constants.IDEAS3
                    else->Constants.BURIAL3
                }
                val fragment = BurialFragment()
                args.putString(Constants.TAB,tab)
                fragment.arguments = args
                fragment
            }
            3 -> {
                val tab = when(itemItem){
                    Constants.BURIAL -> Constants.BURIAL4
                    Constants.MOTIVATION -> Constants.MOTIV4
                    Constants.LINKS -> Constants.LINKS4
                    Constants.IDEAS -> Constants.IDEAS4
                    else->Constants.BURIAL4
                }
                val fragment = BurialFragment()
                args.putString(Constants.TAB,tab)
                fragment.arguments = args
                fragment
            }
            4 -> {
                val tab = when(itemItem){
                    Constants.BURIAL -> Constants.BURIAL5
                    Constants.MOTIVATION -> Constants.MOTIV5
                    Constants.LINKS -> Constants.LINKS5
                    Constants.IDEAS -> Constants.IDEAS5
                    else->Constants.BURIAL5
                }
                val fragment = BurialFragment()
                args.putString(Constants.TAB,tab)
                fragment.arguments = args
                fragment
            }
            5 -> {
                val tab = when(itemItem){
                    Constants.LINKS -> Constants.LINKS6
                    else->Constants.LINKS6
                }
                val fragment = BurialFragment()
                args.putString(Constants.TAB,tab)
                fragment.arguments = args
                fragment
            }
            else -> {
                val fragment = BurialFragment()
                args.putString(Constants.TAB,"")
                fragment.arguments = args
                fragment
            }
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        val title   = when(position){
            0 -> {
                val tab = when(itemItem){
                    Constants.BURIAL -> burialItemsHostFragment.getString(R.string.burial_1)
                    Constants.MOTIVATION -> burialItemsHostFragment.getString(R.string.motiv_1)
                    Constants.LINKS -> burialItemsHostFragment.getString(R.string.links_1)
                    Constants.IDEAS -> burialItemsHostFragment.getString(R.string.ideas_1)
                    else -> burialItemsHostFragment.getString(R.string.burial_1)
                }
                tab
            }
            1 -> {
                val tab = when(itemItem){
                    Constants.BURIAL -> burialItemsHostFragment.getString(R.string.burial_2)
                    Constants.MOTIVATION -> burialItemsHostFragment.getString(R.string.motiv_2)
                    Constants.LINKS -> burialItemsHostFragment.getString(R.string.links_2)
                    Constants.IDEAS -> burialItemsHostFragment.getString(R.string.ideas_2)
                    else -> burialItemsHostFragment.getString(R.string.burial_2)
                }
                tab
            }
            2 -> {
                val tab = when(itemItem){
                    Constants.BURIAL -> burialItemsHostFragment.getString(R.string.burial_3)
                    Constants.MOTIVATION -> burialItemsHostFragment.getString(R.string.motiv_3)
                    Constants.LINKS -> burialItemsHostFragment.getString(R.string.links_3)
                    Constants.IDEAS -> burialItemsHostFragment.getString(R.string.ideas_3)
                    else -> burialItemsHostFragment.getString(R.string.burial_3)
                }
                tab
            }
            3 -> {
                val tab = when(itemItem){
                    Constants.BURIAL -> burialItemsHostFragment.getString(R.string.burial_4)
                    Constants.MOTIVATION -> burialItemsHostFragment.getString(R.string.motiv_4)
                    Constants.LINKS -> burialItemsHostFragment.getString(R.string.links_4)
                    Constants.IDEAS -> burialItemsHostFragment.getString(R.string.ideas_4)
                    else -> burialItemsHostFragment.getString(R.string.burial_4)
                }
                tab
            }
            4 -> {
                val tab = when(itemItem){
                    Constants.BURIAL -> burialItemsHostFragment.getString(R.string.burial_5)
                    Constants.MOTIVATION -> burialItemsHostFragment.getString(R.string.motiv_5)
                    Constants.LINKS -> burialItemsHostFragment.getString(R.string.links_5)
                    Constants.IDEAS -> burialItemsHostFragment.getString(R.string.ideas_5)
                    else -> burialItemsHostFragment.getString(R.string.burial_5)
                }
                tab
            }
            5 -> {
                val tab = when(itemItem){
                    Constants.LINKS -> burialItemsHostFragment.getString(R.string.links_6)
                    else -> burialItemsHostFragment.getString(R.string.links_6)
                }
                tab
            }

            else -> {
                ""
            }
        }
        return title
    }

}

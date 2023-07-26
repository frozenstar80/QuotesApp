package com.example.quotesapp.presentation.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.quotesapp.R
import com.example.quotesapp.data.ViewAllQuotesData
import com.example.quotesapp.databinding.FragmentViewAllQuoteBinding
import com.example.quotesapp.presentation.adapter.QuotesIntroAdapter
import com.example.quotesapp.util.SearchUserItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllQuotesFragment : BaseFragment<FragmentViewAllQuoteBinding>(),SearchUserItemClickListener{
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentViewAllQuoteBinding =
        FragmentViewAllQuoteBinding::inflate

    val list : MutableList<ViewAllQuotesData> = arrayListOf()


    override fun setup() {
        setStatusBarColor()
         binding?.txtTitle?.setOnClickListener {
             requireActivity().onBackPressed()
         }

        list.add(ViewAllQuotesData(0,getString(R.string.love_has_no_age_no_limit_and_no_death),false))
        list.add(ViewAllQuotesData(1,getString(R.string.colors_are_the_smiles_of_nature),false))
        list.add(ViewAllQuotesData(2,getString(R.string.being_happy_never_goes_out_of_style),false))
        list.add(ViewAllQuotesData(3,getString(R.string.i_cannot_escape_death_but_at_least_i_can_escape_the_fear_of_it),false))
        list.add(ViewAllQuotesData(4,getString(R.string.to_love_oneself_is_the_beginning_of_a_lifelong_romance),false))
        list.add(ViewAllQuotesData(5,getString(R.string.find_out_who_you_are_and_do_it_on_purpose),false))
        list.add(ViewAllQuotesData(6,getString(R.string.do_not_judge_me_by_my_success_judge_me_by_how_many_times_i_fell_down_and_got_back_up_again),false))
        list.add(ViewAllQuotesData(7,getString(R.string.it_is_not_death_that_a_man_should_fear_but_rather_he_should_fear_never_beginning_to_live),false))
        list.add(ViewAllQuotesData(8,getString(R.string.for_every_minute_you_are_angry_you_lose_sixty_seconds_of_happiness),false))
        list.add(ViewAllQuotesData(9,getString(R.string.the_biggest_adventure_you_can_ever_take_is_to_live_the_life_of_your_dreams),false))
        list.forEach {
            it.isChecked = it.id==savedPrefManager.getQuoteId()
        }
        val adapter = QuotesIntroAdapter(list,this)
        binding?.rvQuotesList?.adapter =adapter

    }

    private fun setStatusBarColor() {
        val window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.grey_bg_color)
    }


    override fun clicked(id: String) {
        savedPrefManager.putQuoteId(id.toInt())
        list.forEach {
            it.isChecked = it.id==id.toInt()
        }
        val adapter = QuotesIntroAdapter(list,this)
        binding?.rvQuotesList?.adapter =adapter
    }
}
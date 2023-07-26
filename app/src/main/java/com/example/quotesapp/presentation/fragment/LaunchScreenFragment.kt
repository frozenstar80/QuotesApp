package com.example.quotesapp.presentation.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentLaunchScreenBinding
import com.example.quotesapp.presentation.viewModel.SharedLaunchScreenViewModel
import com.example.quotesapp.util.locale
import java.util.*


@SuppressLint("CustomSplashScreen")
class LaunchScreenFragment : BaseFragment<FragmentLaunchScreenBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLaunchScreenBinding =
        FragmentLaunchScreenBinding::inflate

    private val launchScreenViewModel by activityViewModels<SharedLaunchScreenViewModel>()

    override fun setup() {

        val window = requireActivity().window

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.purple)

        when(savedPrefManager.getLocale()){
            "en" -> binding?.radEnglish?.isChecked = true
            "de" -> binding?.radGerman?.isChecked = true
         }
        launchScreenViewModel.apply {
            isLaunchPageOpened.observe(this@LaunchScreenFragment) {
               binding?.txtQuote?.isVisible = it
                binding?.txtViewAllQuote?.isVisible = it
            }
        }
        binding?.btnLogin?.setOnClickListener {
            findNavController().navigate(LaunchScreenFragmentDirections.actionLaunchScreenFragmentToLoginBottomSheetFragment())
        }
        binding?.btnRegister?.setOnClickListener {
            findNavController().navigate(LaunchScreenFragmentDirections.actionLaunchScreenFragmentToSignUpBottomSheetFragment())
        }
        binding?.radEnglish?.setOnClickListener {
            binding?.radEnglish?.isChecked = true
            binding?.radGerman?.isChecked = false
            callRefreshText("en")
        }
        binding?.radGerman?.setOnClickListener {
            binding?.radGerman?.isChecked = true
            binding?.radEnglish?.isChecked = false
            callRefreshText("de")
        }
        binding?.txtViewAllQuote?.setOnClickListener {
            findNavController().navigate(LaunchScreenFragmentDirections.actionLaunchScreenFragmentToViewAllQuotesFragment())
        }
        val text = when(savedPrefManager.getQuoteId()){
            0 ->getString(R.string.love_has_no_age_no_limit_and_no_death)
            1 ->getString(R.string.colors_are_the_smiles_of_nature)
            2 ->getString(R.string.being_happy_never_goes_out_of_style)
            3 -> getString(R.string.i_cannot_escape_death_but_at_least_i_can_escape_the_fear_of_it)
            4 ->getString(R.string.to_love_oneself_is_the_beginning_of_a_lifelong_romance)
            5 ->getString(R.string.find_out_who_you_are_and_do_it_on_purpose)
            6 ->getString(R.string.do_not_judge_me_by_my_success_judge_me_by_how_many_times_i_fell_down_and_got_back_up_again)
            7 ->getString(R.string.it_is_not_death_that_a_man_should_fear_but_rather_he_should_fear_never_beginning_to_live)
            8 ->getString(R.string.for_every_minute_you_are_angry_you_lose_sixty_seconds_of_happiness)
            9 ->getString(R.string.the_biggest_adventure_you_can_ever_take_is_to_live_the_life_of_your_dreams)
            else ->getString(R.string.to_love_oneself_is_the_beginning_of_a_lifelong_romance)
        }
        binding?.txtQuote?.text  = text

    }

    private fun callRefreshText(lang:String){
        savedPrefManager.putLocale(lang)
        locale(lang)
        requireActivity().recreate()
    }


}
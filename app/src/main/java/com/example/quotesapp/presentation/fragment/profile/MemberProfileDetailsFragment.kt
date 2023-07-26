package com.example.quotesapp.presentation.fragment.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotesapp.R
import com.example.quotesapp.data.UserDataResponse
import com.example.quotesapp.databinding.FragmentProfileDetailsBinding
import com.example.quotesapp.presentation.activities.MainActivity
import com.example.quotesapp.presentation.adapter.ProfileDetailsAdapter
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.fragment.dashboard.HomeFragmentDirections
import com.example.quotesapp.presentation.viewModel.ProfileDetailsViewModel
import com.example.quotesapp.presentation.viewModel.QuotesViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class MemberProfileDetailsFragment : BaseFragment<FragmentProfileDetailsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileDetailsBinding =
        FragmentProfileDetailsBinding::inflate

    val viewModel by activityViewModels<ProfileDetailsViewModel>()
    var data : UserDataResponse? = UserDataResponse()
    private val quotesViewModel by activityViewModels<QuotesViewModel>()

    override fun setup() {

        binding?.btnCreateMemberProfile?.isVisible = false
        binding?.txtFollowing?.isVisible = false
        binding?.txtFollowingText?.isVisible = false
        viewModel.showOwnProfile(savedPrefManager.getToken().toString())
        binding?.layout?.isVisible = false
        binding?.layout2?.isVisible = false

        quotesViewModel.getDocumentsLiveData.observe(viewLifecycleOwner){
            binding?.quotes?.text = it.data.firstOrNull()?.content ?: resources?.getString(R.string.create_quote)
            if (it.data.size>0) binding?.viewQuotes?.text =  getString(R.string.view_all) +" "+ it.data.size + " " + if (it.data.size > 1) getString(R.string.quotations) else getString(R.string.quote)
            else binding?.viewQuotes?.isVisible = false
        }

        binding?.exchange?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToExchangeFragment("memberplus"))
        }
        binding?.imgLinks?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToBurialItemsHostFragment("memberplus",savedPrefManager.getId().toString(),"links",false))
        }
        binding?.imgMotivation?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToBurialItemsHostFragment("memberplus",savedPrefManager.getId().toString(),"motivations",false))
        }
        binding?.imgBurial?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToBurialItemsHostFragment("memberplus",savedPrefManager.getId().toString(),"burials",false))
        }
        binding?.imgIdeas?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToBurialItemsHostFragment("memberplus",savedPrefManager.getId().toString(),"ideas",false))
        }

        viewModel.postLiveData.observe(viewLifecycleOwner){ response ->
             data = response.data?.memberPlus?.get(0)
            quotesViewModel.getQuotes(savedPrefManager.getToken().toString(),data?.Id.toString(),"memberplus")
             val subset : MutableList<Pair<String,String>> = arrayListOf()
                response.data?.memberPlus?.get(0)?.let { it ->
                subset.add(Pair(resources.getString(R.string.first_name_xd),it.previousName?: ""))
                subset.add(Pair(resources.getString(R.string.last_name_xd),it.newName?: ""))
                subset.add(Pair(resources.getString(R.string.wife_husband_name),it.wifeHusbandName?: ""))
                subset.add(Pair(resources.getString(R.string.relationship),it.relationshipStatus?:""))
                subset.add(Pair(resources.getString(R.string.last_place_of_residence),it.lastPlaceOfResidence?: ""))
                subset.add(Pair(resources.getString(R.string.birth_date),convertDate(it.dateOfBirth)))
                subset.add(Pair(resources.getString(R.string.date_of_death),convertDate(it.dateOfDeath)))
                subset.add(Pair(resources.getString(R.string.quotations),it.quotations?: ""))
                subset.add(Pair(resources.getString(R.string.what_i_guessed),it.whatIGuessed?: ""))
                subset.add(Pair(resources.getString(R.string.what_i_want_to_share_with_you),it.whatIWantToTellYou?: ""))
                subset.add(Pair(resources.getString(R.string.my_final_words),it.finalWords?: ""))
                binding?.rvProfileDetails?.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = ProfileDetailsAdapter(
                        requireContext(),
                        subset)
                }
                    binding?.txtFollowers?.text = it.followers.size.toString()
                    binding?.txtFollowing?.text = it.following.size.toString()
                    if (it.photos.isNotEmpty())
                    GlideLoader(requireContext()).loadPicFromWeb(it.photos[0],binding?.imgProfilePic)
                    binding?.txtUserName?.text  = it.fullName

                    if (System.currentTimeMillis()>getMillisecondsFromDate(it.dateOfDeath)){
                        binding?.appCompatImageView4?.setImageResource(R.drawable.member_plus)
                    }
            }

        }

        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding?.pictures?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToPhoto(id=data?.Id.toString(),
                sourceInside = true,isMemberPlus = true))
        }
        binding?.videos?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToVideo(id=data?.Id.toString(),
                sourceInside = true,isMemberPlus = true))
        }
        binding?.documents?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToDocuments(id=data?.Id.toString(),
                sourceInside = true,isMemberPlus = true))
        }
        binding?.viewQuotes?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToQuotesFragment(id=data?.Id.toString(),
                sourceInside = true,isMemberPlus = true))
        }
        binding?.groupFollower?.setOnClickListener {
            toast(getString(R.string.something_went_wrong_while_loading_followers_details))
        }

        binding?.imgSettings?.setOnClickListener {
        findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToProfileSettings())
        }
        binding?.imgEditProfile?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToCreateMemberProfileFragment(data))
        }
        binding?.imgShareProfile?.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_body))
            startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
        }
        // Define the animation to "pop out" the buttons
        val popOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_out_animation)

// Define the animation to "pop back" the buttons
        val popBackAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pop_back_animation)
        val forward = AnimationUtils.loadAnimation(requireContext(), R.anim.floating_rotate_forward_animation)
        val backward = AnimationUtils.loadAnimation(requireContext(), R.anim.floating_rotate_backward_animation)


// Add an event listener to the FAB to trigger the animations and image changes
        binding?.imgFab?.setOnClickListener {
            if (binding?.btnVideo?.isVisible == false) {
                binding?.viewTransparent?.isVisible = true

                binding?.imgFab?.startAnimation(forward)

                // Show the four buttons with the "pop out" animation
                binding?.btnVideo?.visibility = View.VISIBLE
                binding?.btnVideo?.startAnimation(popOutAnimation)

                binding?.btnDocuments?.visibility = View.VISIBLE
                binding?.btnDocuments?.startAnimation(popOutAnimation)

                binding?.btnPhoto?.visibility = View.VISIBLE
                binding?.btnPhoto?.startAnimation(popOutAnimation)

                binding?.btnQuote?.visibility = View.VISIBLE
                binding?.btnQuote?.startAnimation(popOutAnimation)
            } else {
                binding?.viewTransparent?.isVisible = false
                binding?.imgFab?.startAnimation(backward)

                // Hide the four buttons with the "pop back" animation
                binding?.btnVideo?.startAnimation(popBackAnimation)
                binding?.btnVideo?.visibility = View.INVISIBLE

                binding?.btnDocuments?.startAnimation(popBackAnimation)
                binding?.btnDocuments?.visibility = View.INVISIBLE

                binding?.btnPhoto?.startAnimation(popBackAnimation)
                binding?.btnPhoto?.visibility = View.INVISIBLE

                binding?.btnQuote?.startAnimation(popBackAnimation)
                binding?.btnQuote?.visibility = View.INVISIBLE
            }
        }

        binding?.groupFollower?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToHostFollowersFollowingFragment(
                isNormalUser = false,
                isFollower = true,
                id = data?.Id.toString()
            ))
        }
        binding?.groupFollowing?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToHostFollowersFollowingFragment(
                isNormalUser = false ,
                isFollower = false,
                id = data?.Id.toString()
            ))

        }

        binding?.btnQuote?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToUploadDataFragment(
                Constants.QUOTE,0,true, id = data?.Id.toString() ))
        }
        binding?.btnVideo?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToUploadDataFragment(
                Constants.VIDEO,0,true, id =data?.Id.toString() ))
        }
        binding?.btnDocuments?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToUploadDataFragment(
                Constants.DOCUMENT,0,true, id =data?.Id.toString() ))
        }
        binding?.btnPhoto?.setOnClickListener {
            findNavController().navigate(MemberProfileDetailsFragmentDirections.actionMemberProfileDetailsFragmentToUploadDataFragment(
                Constants.IMAGE,0,true, id = data?.Id.toString() ))
        }

    }

    override fun onResume() {
        super.onResume()
        val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
        view.visibility = View.GONE
    }

    private fun convertDate(date: String?): String {
        if (date.isNullOrEmpty()) return ""
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dateTime = date?.let { inputFormat.parse(it) }
        return if (dateTime != null) {
            outputFormat.format(dateTime)
        } else ""
    }

    private fun getMillisecondsFromDate(date:String?):Long{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return if (date!=null) {
            val parsedDate: Date = dateFormat.parse(date)
            parsedDate.time
        }else{
            0
        }
        }
}
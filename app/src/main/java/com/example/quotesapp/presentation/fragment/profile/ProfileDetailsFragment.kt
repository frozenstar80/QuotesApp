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
import com.example.quotesapp.presentation.adapter.ProfileDetailsAdapter
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.viewModel.ProfileDetailsViewModel
import com.example.quotesapp.presentation.viewModel.QuotesViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


@AndroidEntryPoint
class ProfileDetailsFragment : BaseFragment<FragmentProfileDetailsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileDetailsBinding =
        FragmentProfileDetailsBinding::inflate

    val viewModel by activityViewModels<ProfileDetailsViewModel>()
    var data: UserDataResponse? = UserDataResponse()
    private val quotesViewModel by activityViewModels<QuotesViewModel>()

    override fun setup() {

        binding?.layout?.isVisible = false
        binding?.layout2?.isVisible = false
        viewModel.showOwnProfile(savedPrefManager.getToken().toString())
        quotesViewModel.getQuotes(
            savedPrefManager.getToken().toString(),
            savedPrefManager.getId().toString(),
            "user"
        )
        quotesViewModel.getDocumentsLiveData.observe(viewLifecycleOwner) {
            binding?.quotes?.text = it.data.firstOrNull()?.content ?: resources?.getString(R.string.create_quote)
            if (it.data.size > 0) binding?.viewQuotes?.text =
                getString(R.string.view_all) +" "+ it.data.size + " " + if (it.data.size > 1) getString(R.string.quotations) else getString(R.string.quote)
            else binding?.viewQuotes?.isVisible = false
        }
        binding?.exchange?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToExchangeFragment("user"))
        }

        viewModel.postLiveData.observe(viewLifecycleOwner) { response ->
            data = response.data
            val subset: MutableList<Pair<String, String>> = arrayListOf()
            response.data?.let { it ->
                subset.add(
                    Pair(
                        resources.getString(R.string.first_name_xd),
                        it.previousName ?: ""
                    )
                )
                subset.add(Pair(resources.getString(R.string.last_name_xd), it.newName ?: ""))
                subset.add(
                    Pair(
                        resources.getString(R.string.wife_husband_name),
                        it.wifeHusbandName ?: ""
                    )
                )
                subset.add(
                    Pair(
                        resources.getString(R.string.last_place_of_residence),
                        it.lastPlaceOfResidence ?: ""
                    )
                )
                subset.add(
                    Pair(
                        resources.getString(R.string.birth_date),
                        convertDate(it.dateOfBirth)
                    )
                )
                subset.add(
                    Pair(
                        resources.getString(R.string.date_of_death),
                        convertDate(it.dateOfDeath)
                    )
                )
                subset.add(Pair(resources.getString(R.string.quotations), it.quotations ?: ""))
                subset.add(
                    Pair(
                        resources.getString(R.string.what_i_guessed),
                        it.whatIGuessed ?: ""
                    )
                )
                subset.add(
                    Pair(
                        resources.getString(R.string.what_i_want_to_share_with_you),
                        it.whatIWantToTellYou ?: ""
                    )
                )
                subset.add(
                    Pair(
                        resources.getString(R.string.my_final_words),
                        it.finalWords ?: ""
                    )
                )
                binding?.rvProfileDetails?.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = ProfileDetailsAdapter(
                        requireContext(),
                        subset
                    )
                }
                binding?.txtFollowers?.text = it.followers.size.toString()
                binding?.txtFollowing?.text = it.following.size.toString()
                if (it.photos.isNotEmpty())
                    GlideLoader(requireContext()).loadPicFromWeb(
                        it.photos[0],
                        binding?.imgProfilePic
                    )
                binding?.txtUserName?.text = it.fullName

                if (it.memberPlus.isNotEmpty()) {
                    binding?.btnCreateMemberProfile?.isVisible = false
                    binding?.memberProfile?.isVisible = true
                    if (it.memberPlus[0].photos.isNotEmpty())
                        GlideLoader(requireContext()).loadPicFromWeb(
                            it.memberPlus[0].photos[0],
                            binding?.imgMemberProfilePic
                        )
                    binding?.txtMemberFollower?.text = "${it.memberPlus[0]?.followers?.size} ${
                        resources.getString(R.string.followers)
                    }"
                    binding?.txtMemberUser?.text = it.memberPlus[0].fullName
                }
            }
        }
        binding?.imgLinks?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToBurialItemsHostFragment("user",savedPrefManager.getId().toString(),"links",false))
        }
        binding?.imgMotivation?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToBurialItemsHostFragment("user",savedPrefManager.getId().toString(),"motivations",false))
        }
        binding?.imgBurial?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToBurialItemsHostFragment("user",savedPrefManager.getId().toString(),"burials",false))
        }
        binding?.imgIdeas?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToBurialItemsHostFragment("user",savedPrefManager.getId().toString(),"ideas",false))
        }

        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding?.groupFollower?.setOnClickListener {
           findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToHostFollowersFollowingFragment(
               isNormalUser = true,
               isFollower = true,
               id = savedPrefManager.getId().toString()
           ))
        }
        binding?.groupFollowing?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToHostFollowersFollowingFragment(
                isNormalUser = true,
                isFollower = false,
                id = savedPrefManager.getId().toString()
            ))

        }
        binding?.memberProfile?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToMemberProfileDetailsFragment())
        }
        binding?.pictures?.setOnClickListener {
            findNavController().navigate(
                ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToPhoto(
                    id = "",
                    sourceInside = true
                )
            )
        }
        binding?.videos?.setOnClickListener {
            findNavController().navigate(
                ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToVideo(
                    id = "",
                    sourceInside = true
                )
            )
        }
        binding?.documents?.setOnClickListener {
            findNavController().navigate(
                ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToDocuments(
                    id = "",
                    sourceInside = true
                )
            )
        }
        binding?.viewQuotes?.setOnClickListener {
            findNavController().navigate(
                ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToQuotesFragment(
                    id = "",
                    sourceInside = true
                )
            )
        }

        binding?.imgSettings?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToProfileSettings())
        }
        binding?.imgEditProfile?.setOnClickListener {
            findNavController().navigate(
                ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToCreateProfileFragment(
                    data
                )
            )
        }
        binding?.btnCreateMemberProfile?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToCreateMemberProfileFragment())
        }
        binding?.imgShareProfile?.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_body))
            startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
        }



        // Define the animation to "pop out" the buttons
        val popOutAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.pop_out_animation)

// Define the animation to "pop back" the buttons
        val popBackAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.pop_back_animation)
        val forward =
            AnimationUtils.loadAnimation(requireContext(), R.anim.floating_rotate_forward_animation)
        val backward = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.floating_rotate_backward_animation
        )


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

        binding?.btnQuote?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToUploadDataFragment(
                Constants.QUOTE))
        }
        binding?.btnVideo?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToUploadDataFragment(
                Constants.VIDEO))
        }
        binding?.btnDocuments?.setOnClickListener {
            findNavController().navigate(ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToUploadDataFragment(
                Constants.DOCUMENT))
        }
        binding?.btnPhoto?.setOnClickListener {
            findNavController().navigate(
                ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToUploadDataFragment(
                    Constants.IMAGE
                )
            )
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

}
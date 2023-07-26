package com.example.quotesapp.presentation.fragment.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotesapp.R
import com.example.quotesapp.data.UserDataResponse
import com.example.quotesapp.databinding.FragmentProfileDetailsBinding
import com.example.quotesapp.presentation.adapter.ProfileDetailsAdapter
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.fragment.dashboard.HomeFragmentDirections
import com.example.quotesapp.presentation.viewModel.OtherUserProfileViewModel
import com.example.quotesapp.presentation.viewModel.QuotesViewModel
import com.example.quotesapp.util.GlideLoader
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class OtherUserProfileDetailsFragment : BaseFragment<FragmentProfileDetailsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileDetailsBinding =
        FragmentProfileDetailsBinding::inflate

    val viewModel by viewModels<OtherUserProfileViewModel>()
    private val args by navArgs<OtherUserProfileDetailsFragmentArgs>()
    var data : UserDataResponse? = UserDataResponse()
    private val quotesViewModel by activityViewModels<QuotesViewModel>()
    var userType  = ""


    override fun setup() {
        val user = if(args.otherUserData?.isUser=="member") "user" else "memberplus"
        binding?.imgFab?.isVisible = false
        binding?.otherUserProfileVisibility?.isVisible = true
        binding?.imgEditProfile?.isVisible = false
        binding?.imgSettings?.isVisible = false
        binding?.memberProfile?.isVisible = false

        binding?.appCompatImageView4?.setImageResource(R.drawable.others_profile)

        if (args.otherUserData?.isAccepted == false && args.otherUserData?.isRequestSent == false){
            binding?.btnCreateMemberProfile?.text = resources.getString(R.string.follow)
            binding?.pictures?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, 0, 0);
            binding?.videos?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, 0, 0);
            binding?.documents?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, 0, 0);
            binding?.viewQuotes?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, 0, 0);
            binding?.exchange?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, 0, 0);
            binding?.groupLock?.isVisible =true
            binding?.blurImage?.isVisible  =true
            binding?.followToViewContent?.isVisible = true
            binding?.info?.isVisible = true
        }
        else if (args.otherUserData?.isAccepted==false && args.otherUserData?.isRequestSent==true) {
            binding?.pictures?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, 0, 0);
            binding?.videos?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, 0, 0);
            binding?.documents?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, 0, 0);
            binding?.viewQuotes?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, 0, 0);
            binding?.exchange?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, 0, 0);
            binding?.groupLock?.isVisible =true
            binding?.blurImage?.isVisible  =true
            binding?.followToViewContent?.isVisible = true
            binding?.info?.isVisible = true
                binding?.btnCreateMemberProfile?.text = resources.getString(R.string.requested)
                binding?.btnCreateMemberProfile?.setTextColor(resources.getColor(R.color.white))
                binding?.btnCreateMemberProfile?.backgroundTintList = ContextCompat.getColorStateList(requireActivity(), R.color.light_grey)
            }
            else {
                binding?.pictures?.setOnClickListener {
                    findNavController().navigate(OtherUserProfileDetailsFragmentDirections.actionOtherUserProfileDetailsFragmentToPhoto(id=args.otherUserData?.Id.toString(),
                        sourceInside = true, otherUser = "yes" , isMemberPlus = args.otherUserData?.isUser!="member"
                    ))
                }
                binding?.videos?.setOnClickListener {
                    findNavController().navigate(OtherUserProfileDetailsFragmentDirections.actionOtherUserProfileDetailsFragmentToVideo(id=args.otherUserData?.Id.toString(),
                        sourceInside = true , otherUser = "yes", isMemberPlus = args.otherUserData?.isUser!="member"
                    ))
                }
                binding?.documents?.setOnClickListener {
                    findNavController().navigate(OtherUserProfileDetailsFragmentDirections.actionOtherUserProfileDetailsFragmentToDocuments(id=args.otherUserData?.Id.toString(),
                        sourceInside = true , otherUser = "yes", isMemberPlus = args.otherUserData?.isUser!="member"
                    ))
                }
                binding?.viewQuotes?.setOnClickListener {
                    findNavController().navigate(OtherUserProfileDetailsFragmentDirections.actionOtherUserProfileDetailsFragmentToQuotesFragment(id=args.otherUserData?.Id.toString(),
                        sourceInside = true , otherUser = "yes", isMemberPlus = args.otherUserData?.isUser!="member"
                    ))
                }

            binding?.imgLinks?.setOnClickListener {
                findNavController().navigate(OtherUserProfileDetailsFragmentDirections.actionOtherUserProfileDetailsFragmentToBurialItemsHostFragment(user,savedPrefManager.getId().toString(),"links",false))
            }
            binding?.imgMotivation?.setOnClickListener {
                findNavController().navigate(OtherUserProfileDetailsFragmentDirections.actionOtherUserProfileDetailsFragmentToBurialItemsHostFragment(user,savedPrefManager.getId().toString(),"motivations",false))
            }
            binding?.imgBurial?.setOnClickListener {
                findNavController().navigate(OtherUserProfileDetailsFragmentDirections.actionOtherUserProfileDetailsFragmentToBurialItemsHostFragment(user,savedPrefManager.getId().toString(),"burials",false))
            }
            binding?.imgIdeas?.setOnClickListener {
                findNavController().navigate(OtherUserProfileDetailsFragmentDirections.actionOtherUserProfileDetailsFragmentToBurialItemsHostFragment(user,savedPrefManager.getId().toString(),"ideas",false))
            }

            binding?.exchange?.setOnClickListener {
                    findNavController().navigate(OtherUserProfileDetailsFragmentDirections.actionOtherUserProfileDetailsFragmentToOtherUserExchangeFragment(args.otherUserData?.Id.toString(),user))
            }
            binding?.btnCreateMemberProfile?.text = resources.getString(R.string.following)
            binding?.btnCreateMemberProfile?.setTextColor(resources.getColor(R.color.white))
            binding?.btnCreateMemberProfile?.backgroundTintList = ContextCompat.getColorStateList(requireActivity(), R.color.light_grey)
            }

       val userType  =  if (args.otherUserData?.isUser=="member") "user" else "memberType"
        if (userType=="memberType"){
            binding?.txtFollowing?.isVisible = false
            binding?.txtFollowingText?.isVisible = false
        }
        quotesViewModel.getQuotes(savedPrefManager.getToken().toString(),args.otherUserData?.Id.toString(),userType)
        quotesViewModel.getDocumentsLiveData.observe(viewLifecycleOwner){
            binding?.quotes?.text = it.data.firstOrNull()?.content ?: resources?.getString(R.string.create_quote)
            if (it.data.size>0) binding?.viewQuotes?.text =
                getString(R.string.view_all) +" " + it.data.size + " " + if (it.data.size > 1) getString(R.string.quotations) else getString(R.string.quote)
            else  binding?.viewQuotes?.isVisible = false
        }

            val subset : MutableList<Pair<String,String>> = arrayListOf()
            args.otherUserData.let { it ->
                binding?.txtFollowers?.text = it?.followers?.size.toString()
                binding?.txtFollowing?.text = it?.following?.size.toString()
                subset.add(Pair(resources.getString(R.string.first_name_xd),it?.previousName?: ""))
                subset.add(Pair(resources.getString(R.string.last_name_xd),it?.newName?: ""))
                subset.add(Pair(resources.getString(R.string.wife_husband_name),it?.wifeHusbandName?: ""))
                subset.add(Pair(resources.getString(R.string.last_place_of_residence),it?.lastPlaceOfResidence?: ""))
                subset.add(Pair(resources.getString(R.string.birth_date),convertDate(it?.dateOfBirth)))
                subset.add(Pair(resources.getString(R.string.date_of_death),convertDate(it?.dateOfDeath)))
                subset.add(Pair(resources.getString(R.string.quote),it?.quotations?: ""))
//                subset.add(Pair(resources.getString(R.string.what_i_guessed),it?.whatIGuessed?: ""))
//                subset.add(Pair(resources.getString(R.string.what_i_want_to_share_with_you),it?.whatIWantToTellYou?: ""))
                subset.add(Pair(resources.getString(R.string.my_final_words),it?.finalWords?: ""))
                binding?.rvProfileDetails?.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = ProfileDetailsAdapter(
                        requireContext(),
                        subset)
                }
                binding?.txtTitleWhatIGuessed?.text=it?.whatIGuessed?: ""
                binding?.txtTitleWhatIShare?.text=it?.whatIWantToTellYou?: ""
                if (it?.photos?.isNotEmpty() == true)
                    GlideLoader(requireContext()).loadPicFromWeb(it.photos[0],binding?.imgProfilePic)
                binding?.txtUserName?.text  = it?.fullName
            }


        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding?.info?.setOnClickListener {
            toast(getString(R.string.only_friends_can_see_the_data_which_is_locked))
        }
        binding?.imgShareProfile?.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_body))
            startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
        }

        binding?.groupFollower?.setOnClickListener {
            findNavController().navigate(OtherUserProfileDetailsFragmentDirections.actionOtherUserProfileDetailsFragmentToHostFollowersFollowingFragment(
                isNormalUser = userType != "memberType",
                isFollower = true,
                id = args.otherUserData?.Id.toString()
            ))
        }
        binding?.groupFollowing?.setOnClickListener {
            findNavController().navigate(OtherUserProfileDetailsFragmentDirections.actionOtherUserProfileDetailsFragmentToHostFollowersFollowingFragment(
                isNormalUser = userType != "memberType" ,
                isFollower = false,
                id =  args.otherUserData?.Id.toString()
            ))

        }



        //send Follow request
        binding?.btnCreateMemberProfile?.setOnClickListener {
            if (args.otherUserData?.isAccepted==false && args.otherUserData?.isRequestSent==false)
            {
                viewModel.sendFollowRequest(args.otherUserData?.Id.toString(),savedPrefManager.getToken().toString())
                binding?.btnCreateMemberProfile?.text = resources.getString(R.string.requested)
                toast(resources.getString(R.string.request_sent))
                binding?.btnCreateMemberProfile?.setTextColor(resources.getColor(R.color.white))
                binding?.btnCreateMemberProfile?.backgroundTintList = ContextCompat.getColorStateList(requireActivity(), R.color.light_grey)
            }
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
        val dateTime = date.let { inputFormat.parse(it) }
        return if (dateTime != null) {
            outputFormat.format(dateTime)
        } else ""
    }

}
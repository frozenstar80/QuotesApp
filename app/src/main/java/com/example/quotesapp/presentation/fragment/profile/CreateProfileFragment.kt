package com.example.quotesapp.presentation.fragment.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentProfileBinding
import com.example.quotesapp.presentation.activities.HomeActivity
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.viewModel.CreateProfileViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.Utils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Calendar


@AndroidEntryPoint
class CreateProfileFragment : BaseFragment<FragmentProfileBinding>()  {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding =
        FragmentProfileBinding::inflate

    private val createProfileViewModel by viewModels<CreateProfileViewModel>()
    private var headerImageLauncher: ActivityResultLauncher<Intent>? = null
    private var headerImagePart: MultipartBody.Part? = null
    private var uploadedImageName: String = ""
    private lateinit var map: HashMap<String, Any?>
    private var dob = ""
    private var dod = ""
    private val args by navArgs<CreateProfileFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (args.userData == null)
                        return
                    else
                        NavHostFragment.findNavController(requireParentFragment()).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun setup() {

        setStatusBarColor()


        if (args.userData != null) {
            setDataInFields()
            binding?.txtTitle?.text = resources.getString(R.string.update_profile)
            binding?.btnCreateProfile?.text = resources.getString(R.string.update_profile)
        }

        headerImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    try {
                        val intent = result.data
                        val imageUri = intent?.data
                        lifecycleScope.launch{
                            if (imageUri == null) {
                             return@launch
                            }
                            val outputFile =  Utils.getCompressImagePath(requireContext(), imageUri)
                            val requestBody = outputFile?.asRequestBody("image/*".toMediaTypeOrNull())
                            headerImagePart = requestBody?.let {
                                MultipartBody.Part.createFormData("image", outputFile.name,
                                    it
                                )
                            }
                            uploadImage()
                            binding?.imgAddImage?.isVisible = false
                            GlideLoader(requireContext()).loadUserPicture(imageUri, binding?.imgProfilePic)
                        }

                    } catch (e: NullPointerException) {
                        println(e.printStackTrace())
                    }
                } else {
                    Log.e("Error", resources.getString(R.string.profile_image_change_error))
                }
            }

        createProfileViewModel.createProfileLiveData.observe(this) {
            if (it.status == true) {
                savedPrefManager.putNamePhotoDetails(
                    binding?.edtFullName?.text.toString().trim(),
                    uploadedImageName
                )
                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                requireActivity().finish()
            } else showSnackBar(it.message)
        }

        createProfileViewModel.uploadPhotoLiveData.observe(this) {
            if (it?.data?.isEmpty() == true) {
                showSnackBar(resources.getString(R.string.error_occured_pic_upload))
                binding?.imgAddImage?.isVisible = true
                binding?.imgProfilePic?.setImageDrawable(null)
                return@observe
            }
            toast(it.message)
            uploadedImageName = it.data[0].file.toString()
            headerImagePart = null
        }

        binding?.groupBirthDate?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    binding!!.edtBirthDate.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                    dob = "${monthOfYear + 1}-$dayOfMonth-$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
        binding?.groupDeathDate?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    binding!!.edtDod.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                    dod = "${monthOfYear + 1}-$dayOfMonth-$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        binding?.imgProfilePic?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkPermissionForTiramisu()
            }else{
                checkPermission()
            }
        }

        binding?.btnCreateProfile?.setOnClickListener {
            if (validation()) {
                map = HashMap()
                extractDataFromFields()
                createProfileViewModel.createProfile(map, savedPrefManager.getId().toString())
            }
        }
    }

    private fun setStatusBarColor() {
        val window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.grey_bg_color)
    }

    private fun setDataInFields() {
        args.userData?.apply {
            binding?.edtFullName?.setText(fullName)
            binding?.edtPrevName?.setText(previousName)
            binding?.edtNewName?.setText(newName)
            binding?.edtWifeHusbandName?.setText(wifeHusbandName)
            binding?.edtResidance?.setText(lastPlaceOfResidence)
            binding?.edtQuot?.setText(quotations)
            binding?.edtGuessed?.setText(whatIGuessed)
            binding?.edtShareWithYou?.setText(whatIWantToTellYou)
            binding?.edtMyFinalWord?.setText(finalWords)
            if (dateOfBirth != null) {
                binding?.edtBirthDate?.setText(
                    Constants.formatDate(
                        dateOfBirth,
                        Constants.DATEMONTHYEAR
                    )
                )
                dob = Constants.formatDate(dateOfBirth, Constants.MONTHDATEYEAR)
            }
            if (dateOfDeath != null) {
                binding?.edtDod?.setText(
                    Constants.formatDate(
                        dateOfDeath,
                        Constants.DATEMONTHYEAR
                    )
                )
                dod = Constants.formatDate(dateOfDeath, Constants.MONTHDATEYEAR)
            }
            if (photos.isNotEmpty()) {
                binding?.imgAddImage?.isVisible = false
                uploadedImageName = photos[0]
                GlideLoader(requireContext()).loadPicFromWeb(photos[0], binding?.imgProfilePic)
            }

        }
    }

    private fun extractDataFromFields() {
        map[Constants.FULLNAME] = binding?.edtFullName?.text.toString().trim()
        map[Constants.PREVNAME] = binding?.edtPrevName?.text.toString().trim()
        map[Constants.NEWNAME] = binding?.edtNewName?.text.toString().trim()
        map[Constants.WIFEHUSNAME] = binding?.edtWifeHusbandName?.text.toString().trim()
        map[Constants.RESIDENCE] = binding?.edtResidance?.text.toString().trim()
        if (dob.isNotEmpty()) map[Constants.DOB] = dob
        if (dod.isNotEmpty())  map[Constants.DOD] =  dod
        map[Constants.RELATIONSHIPSTATUS] = ""
        map[Constants.QUOTATION] = binding?.edtQuot?.text.toString().trim()
        map[Constants.GUESSED] = binding?.edtGuessed?.text.toString().trim()
        map[Constants.WANTTOTELL] = binding?.edtShareWithYou?.text.toString().trim()
        map[Constants.MYFINALWORD] = binding?.edtMyFinalWord?.text.toString().trim()
        map[Constants.PHOTOS] = arrayOf(uploadedImageName)
    }

    private fun validation(): Boolean {
        if (headerImagePart!=null){
            toast(getString(R.string.file_is_getting_uploaded_please_wait))
            return false
        }
        if (uploadedImageName.isEmpty()) {
            toast(resources.getString(R.string.upload_profile_picture_toast))
            return false
        }
        if (binding?.edtFullName?.text?.trim()?.isEmpty() == true) {
            showSnackBar(resources.getString(R.string.full_name_toast))
            return false
        }
        return true
    }

    private fun uploadImage() {
        createProfileViewModel.uploadUserFile(headerImagePart)
    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        headerImageLauncher?.launch(intent)

    }

    // Define the permissions array
    private val requestMultiplePermissions =  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions ->
        permissions.entries.forEach {
            Log.e("DEBUG", "${it.key} = ${it.value}")
            getImage()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                storge_permissions[0]
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                storge_permissions[1]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getImage()
        } else {
            requestMultiplePermissions.launch(permissions())
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionForTiramisu() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                storge_permissions_33_img[0]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getImage()
        } else {
            requestMultiplePermissions.launch(permissionsImage())
        }
    }



    private fun showSnackBar(message: String?) {
        val view = view?.findViewById<NestedScrollView>(R.id.lyt_root)
        val snackBar = view?.let { Snackbar.make(it, message ?: "", Snackbar.LENGTH_LONG) }
        val snackBarView = snackBar?.view

        snackBarView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
        )
        snackBar?.show()
    }

    companion object {
        private const val PERMISSION = 999
    }


}
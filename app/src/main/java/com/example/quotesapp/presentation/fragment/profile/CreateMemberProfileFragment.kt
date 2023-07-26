package com.example.quotesapp.presentation.fragment.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentMemberProfileBinding
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.viewModel.CreateMemberProfileViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.Utils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Calendar


@AndroidEntryPoint
class CreateMemberProfileFragment : BaseFragment<FragmentMemberProfileBinding>() , AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMemberProfileBinding =
        FragmentMemberProfileBinding::inflate

    private val viewModel by viewModels<CreateMemberProfileViewModel>()
    private var headerImageLauncher: ActivityResultLauncher<Intent>? = null
    private var permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private var headerImagePart: MultipartBody.Part? = null
    private var uploadedImageName: String = ""
    private lateinit var map: HashMap<String, Any?>
    private var dob = ""
    private var dod = ""
    private val args by navArgs<CreateMemberProfileFragmentArgs>()

    override fun setup() {

        setStatusBarColor()
        binding?.fieldRelationship?.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.relationship_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.fieldRelationship?.adapter = adapter
        }

        if (args.userData != null ) {
            setDataInFields()
            binding?.txtTitle?.text = resources.getString(R.string.update_member_plus_profile)
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
                            val imageFile =  Utils.getCompressImagePath(requireContext(), imageUri)
                        val requestBody = imageFile?.asRequestBody("image/*".toMediaTypeOrNull())
                        headerImagePart = requestBody?.let {
                            MultipartBody.Part.createFormData("image", imageFile.name,
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

        viewModel.createProfileLiveData.observe(this) {
            if (it.status == true) {
             showSnackBar(resources.getString(R.string.member_profile_created_successfully))
             requireActivity().onBackPressed()
            } else showSnackBar(it.message)
        }
        viewModel.updateProfileLiveData.observe(this) {
            if (it.status == true) {
                showSnackBar(resources.getString(R.string.member_profile_update_successfully))
                findNavController().navigate(CreateMemberProfileFragmentDirections.actionCreateMemberProfileFragmentToMemberProfileDetailsFragment())
            } else showSnackBar(it.message)
        }

        viewModel.uploadPhotoLiveData.observe(this) {
            if (it?.data?.isEmpty() == true) {
                showSnackBar(resources.getString(R.string.error_occured_pic_upload))
                binding?.imgAddImage?.isVisible = true
                binding?.imgProfilePic?.setImageDrawable(null)
                return@observe
            }
            uploadedImageName = it.data[0].file.toString()
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
                if (args.userData != null) viewModel.updateMemberProfile(map,savedPrefManager.getToken().toString())
                else viewModel.createMemberProfile(map,savedPrefManager.getToken().toString())
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
            val array = resources.getStringArray(R.array.relationship_array)
            if (array.contains(relationshipStatus)) {
                val position = array.binarySearch(relationshipStatus)
                binding?.fieldRelationship?.setSelection(position,true)
            }
            binding?.edtGuessed?.setText(whatIGuessed)
            binding?.edtShareWithYou?.setText(whatIWantToTellYou)
            binding?.edtMyFinalWord?.setText(finalWords)
            if (dateOfBirth != null) {
                binding?.edtBirthDate?.setText(
                    Constants.formatDate(
                        dateOfBirth!!,
                        Constants.DATEMONTHYEAR
                    )
                )
                dob = Constants.formatDate(dateOfBirth!!, Constants.MONTHDATEYEAR)
            }
            if (dateOfDeath != null) {
                binding?.edtDod?.setText(
                    Constants.formatDate(
                        dateOfDeath!!,
                        Constants.DATEMONTHYEAR
                    )
                )
                dod = Constants.formatDate(dateOfDeath!!, Constants.MONTHDATEYEAR)
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
        map[Constants.RELATIONSHIPSTATUS] = binding?.fieldRelationship?.selectedItem
        map[Constants.EMAIL] = savedPrefManager.getMailId()
        map[Constants.QUOTATION] = binding?.edtQuot?.text.toString().trim()
        map[Constants.GUESSED] = binding?.edtGuessed?.text.toString().trim()
        map[Constants.WANTTOTELL] = binding?.edtShareWithYou?.text.toString().trim()
        map[Constants.MYFINALWORD] = binding?.edtMyFinalWord?.text.toString().trim()
        map[Constants.PHOTOS] = arrayOf(uploadedImageName)
    }

    private fun validation(): Boolean {
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
        viewModel.uploadUserFile(headerImagePart)
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
                R.color.green
            )
        )
        snackBar?.show()
    }

    companion object {
        private const val PERMISSION = 999
    }


}
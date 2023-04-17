package com.example.quotesapp.presentation.fragment

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentProfileBinding
import com.example.quotesapp.presentation.activities.HomeActivity
import com.example.quotesapp.presentation.viewModel.CreateProfileViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.getFileName
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class CreateProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding =
        FragmentProfileBinding::inflate

    val createProfileViewModel by viewModels<CreateProfileViewModel>()
    private var headerImageLauncher: ActivityResultLauncher<Intent>? = null
    private var permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private var headerImagePart: MultipartBody.Part? = null
    private var uploadedImageName: String = "1681655223595.jpg"
    private lateinit var map : HashMap<String,Any?>
    private var dob =""
    private var dod=""


    override fun setup() {
        headerImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    try {
                        val intent = result.data
                        val imageUri = intent!!.data
                        val imageProjection = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor = requireContext().contentResolver.query(
                            imageUri!!,
                            imageProjection,
                            null,
                            null,
                            null
                        )!!
                        cursor.moveToFirst()
                        val indexImage = cursor.getColumnIndex(imageProjection[0])
                        val imagePath = cursor.getString(indexImage)
                        cursor.close()
                        val imageFile = File(imagePath)
                        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                        headerImagePart = MultipartBody.Part.createFormData(
                            "image",
                            imageFile.name,
                            requestBody
                        )
          /*              val uri = result?.data?.data!!
                        val parcelFileDescriptor =
                            requireContext().contentResolver?.openFileDescriptor(uri, "r", null)
                        parcelFileDescriptor?.use {
                            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                            val file = File(requireContext().cacheDir, requireContext().contentResolver?.getFileName(uri)!!)
                            val outputStream = FileOutputStream(file)
                            inputStream.copyTo(outputStream)
                            headerImagePart = file.run {
                                MultipartBody.Part.createFormData(
                                    "image",
                                    name,
                                    asRequestBody()
                                )
                            }
                        }

           */



                        uploadImage()
                        binding?.imgAddImage?.isVisible = false
                        GlideLoader(requireContext()).loadUserPicture(
                            imageUri,
                            binding?.imgProfilePic
                        )
                    } catch (e:NullPointerException){
                        println(e.printStackTrace())
                    }
                } else {
                    Log.e("Error", resources.getString(R.string.profile_image_change_error))
                }
            }

        GlideLoader(requireContext()).loadUserPicture("http://143.110.247.128:8008/api/user/upload/image/1681414294909.png",binding?.imgProfilePic)

        createProfileViewModel.createProfileLiveData.observe(this){
            if (it.status==true) {
                savedPrefManager.putNamePhotoDetails(binding?.edtFullName?.text.toString().trim(),uploadedImageName)
                toast(resources.getString(R.string.profile_created_successfuly))
                startActivity(Intent(requireActivity(),HomeActivity::class.java))
                requireActivity().finish()
            }
            else toast(it.message)
        }

        createProfileViewModel.uploadPhotoLiveData.observe(this){
            if (it?.data?.isEmpty() == true) {
                toast(resources.getString(R.string.error_occured_pic_upload))
                binding?.imgAddImage?.isVisible = false
                binding?.imgProfilePic?.setImageDrawable(null)
                return@observe
            }
             uploadedImageName = it.data[0].file.toString()
        }

        binding?.fieldBirthDate?.setEndIconOnClickListener {
            // Create a Calendar instance with the current date
            val calendar = Calendar.getInstance()

            // Create a DatePickerDialog with the current date and an OnDateSetListener
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    // Update the TextView with the selected date
                    binding!!.edtBirthDate.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                    dob = "$dayOfMonth-${monthOfYear + 1}-$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }
        binding?.fieldDod?.setEndIconOnClickListener {
            // Create a Calendar instance with the current date
            val calendar = Calendar.getInstance()

            // Create a DatePickerDialog with the current date and an OnDateSetListener
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    // Update the TextView with the selected date
                    binding!!.edtDod.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                    dod = "$dayOfMonth-${monthOfYear + 1}-$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }

        binding?.imgProfilePic?.setOnClickListener {
            checkPermission()
        }
        binding?.btnCreateProfile?.setOnClickListener {
            if(validation()) {
                map = HashMap()
                extractDataFromFields()
                createProfileViewModel.createProfile(map,savedPrefManager.getId().toString())
            }
        }

    }

    private fun extractDataFromFields(){
        map[Constants.FULLNAME] = binding?.edtFullName?.text.toString().trim()
        map[Constants.PREVNAME] =  binding?.edtPrevName?.text.toString().trim()
        map[Constants.NEWNAME] = binding?.edtNewName?.text.toString().trim()
        map[Constants.WIFEHUSNAME] =  binding?.edtWifeHusbandName?.text.toString().trim()
        map[Constants.RELATIONSHIPSTATUS] = ""
        map[Constants.RESIDENCE] =  binding?.edtResidance?.text.toString().trim()
        map[Constants.DOB] = dob.ifEmpty { null }
        map[Constants.DOD] =  dod.ifEmpty { null }
        map[Constants.QUOTATION] = binding?.edtQuot?.text.toString().trim()
        map[Constants.GUESSED] =  binding?.edtGuessed?.text.toString().trim()
        map[Constants.WANTTOTELL] = binding?.edtShareWithYou?.text.toString().trim()
        map[Constants.MYFINALWORD] =  binding?.edtMyFinalWord?.text.toString().trim()
        map[Constants.PHOTOS] =  arrayOf(uploadedImageName)
    }

    private fun validation(): Boolean {
//           if (uploadedImageName.isEmpty()) {
//               toast(resources.getString(R.string.upload_profile_picture_toast))
//               return false
//           }
          if (binding?.edtFullName?.text?.trim()?.isEmpty() == true) {
            toast(resources.getString(R.string.full_name_toast))
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

    companion object {
        private const val PERMISSION = 999
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== PERMISSION)
        {
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                getImage()
            }
            else{
                toast(getString(R.string.grant_read_permission))
            }
        }

    }
    private fun checkPermission(){
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permissions[0]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getImage()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), permissions,
                PERMISSION
            )
        }
    }
}
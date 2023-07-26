package com.example.quotesapp.presentation.fragment.buriaModule

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentRecievedExchangeBinding
import com.example.quotesapp.databinding.FragmentUploadBurialBinding
import com.example.quotesapp.presentation.fragment.BaseFragment
import com.example.quotesapp.presentation.viewModel.BurialViewModel
import com.example.quotesapp.presentation.viewModel.UploadBurialViewModel
import com.example.quotesapp.presentation.viewModel.UploadDataViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@AndroidEntryPoint
class UploadBurialFragment : BaseFragment<FragmentUploadBurialBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUploadBurialBinding =
        FragmentUploadBurialBinding::inflate

    private val viewModel by viewModels<UploadBurialViewModel>()
    private val navArg by navArgs<UploadBurialFragmentArgs>()
    private var headerImageLauncher: ActivityResultLauncher<Intent>? = null
    private var headerImagePart: MultipartBody.Part? = null
    var uploadedImageName =""


    override fun setup() {
        if (navArg.itemType==Constants.MOTIVATION) {binding?.layout?.isVisible  = true
        binding?.fieldTitle?.apply {
            isVisible = false
        }}
        binding?.imgBack?.setOnClickListener {
            requireActivity()?.onBackPressed()
        }
        if (navArg.burialData!=null){
            //set Data
            binding?.edtTitle?.setText(navArg.burialData!!.title)
            binding?.edtCaption?.setText(navArg.burialData!!.caption)
            if (!navArg.burialData!!.file.isNullOrEmpty()) {
                GlideLoader(requireContext()).loadPicFromWeb(navArg.burialData!!.file,binding?.uploadPic)
                binding?.editPhoto?.isVisible = true
            }
            binding?.delete?.isVisible = true
        }
        binding?.editPhoto?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkPermissionForTiramisu()
            }else{
                checkPermission()
            }
        }
        binding?.btnSave?.setOnClickListener {
            binding?.edtTitle?.setText("hi")
            if (validation()){
                val map = HashMap<String,String>()
                map.put("title",binding?.edtTitle?.text?.trim().toString())
                map.put("caption",binding?.edtCaption?.text?.trim().toString())
                map.put("tab",navArg.tab)
                if (navArg.itemType==Constants.MOTIVATION)  map.put("file",uploadedImageName)
                if (navArg.burialData!=null) {
                    viewModel.updateBurial(map,savedPrefManager.getToken().toString(),navArg.id,navArg.itemType)
                }else{
                    viewModel.addBurial(map,savedPrefManager.getToken().toString(),navArg.id,navArg.itemType,navArg.userType)
                }
            }
        }
        binding?.delete?.setOnClickListener {
            deleteData()
        }
        binding?.viewUpload?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkPermissionForTiramisu()
            }else{
                checkPermission()
            }
        }
        viewModel.addBurialLiveData.observe(viewLifecycleOwner){
            if (it.status==true) toast(getString(R.string.saved))
            requireActivity().onBackPressed()
        }
        viewModel.deleteBurialLiveData.observe(viewLifecycleOwner){
            if (it.status==false) toast(it.message)
            else {
                toast(getString(R.string.successfully_deleted))
                requireActivity().onBackPressed()
            }
        }
        viewModel.uploadFileLiveData.observe(viewLifecycleOwner){
            if (it.data.isNotEmpty()){
                toast(getString(R.string.uploaded))
                uploadedImageName = it.data[0].file.toString()
            }else{
                toast(it.message)
            }
            headerImagePart =null
        }

        uploadPhotoOnServer()
    }

    private fun deleteData() {

        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(getString(R.string.delete) )
        alertDialog.setMessage(getString(R.string.do_you_want_to_delete) + navArg.itemType)
        alertDialog.setNegativeButton(
            getString(R.string.cancel)
        ) { _, _ ->
            getString(R.string.cancelled)
        }
        alertDialog.setPositiveButton(getString(R.string.yes)){ _,_->
            viewModel.deleteBurial(savedPrefManager.getToken().toString(),navArg.id,navArg.itemType)
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    private fun validation(): Boolean {
        if (binding?.edtTitle?.text?.trim()?.isEmpty()==true){
            toast(getString(R.string.please_add_title))
            return false
        }
        if (binding?.edtCaption?.text?.trim()?.isEmpty()==true){
            toast(getString(R.string.please_add_caption))
            return false
        }
        if (uploadedImageName.isEmpty() && navArg.itemType==Constants.MOTIVATION){
            toast(getString(R.string.please_upload_image))
            return false
        }
        return true
    }

    private fun uploadPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        headerImageLauncher?.launch(intent)

    }

    override fun onResume() {
        super.onResume()
        val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
        view.visibility = View.GONE
    }

    private fun uploadPhotoOnServer(){
        headerImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    try {
                        val intent = result.data
                        val imageUri = intent?.data
                        lifecycleScope.launch {
                            if (imageUri == null) {
                                return@launch
                            }
                            val imageFile = Utils.getCompressImagePath(requireContext(), imageUri)
                            val requestBody = imageFile?.asRequestBody("image/*".toMediaTypeOrNull())
                            headerImagePart = requestBody?.let {
                                MultipartBody.Part.createFormData(
                                    "image",
                                    imageFile.name,
                                    it
                                )
                            }
                            binding?.uploadPic?.isVisible = true
                            binding?.upload?.isVisible = false
                            binding?.txtUpload?.isVisible = false
                            binding?.editPhoto?.visibility = View.VISIBLE
                            GlideLoader(requireContext()).loadUserPicture(
                                imageUri,
                                binding?.uploadPic
                            )
                            viewModel.uploadUserFile(headerImagePart,"image")
                        }
                    } catch (e: NullPointerException) {
                        println(e.printStackTrace())
                    }
                } else {
                    Log.e("Error", "Upload Error")
                }
            }
    }

    private val requestMultiplePermissions =  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions ->
        permissions.entries.forEach {
            Log.e("DEBUG", "${it.key} = ${it.value}")
            uploadPhoto()
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermissionForTiramisu()
            return
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                storge_permissions[0]
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                storge_permissions[1]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            uploadPhoto()
        } else {
            requestMultiplePermissions.launch(permissions())
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionForTiramisu() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                storge_permissions_33[0]
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                storge_permissions_33[1]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            uploadPhoto()
        } else {
            requestMultiplePermissions.launch(permissions())
        }
    }

}
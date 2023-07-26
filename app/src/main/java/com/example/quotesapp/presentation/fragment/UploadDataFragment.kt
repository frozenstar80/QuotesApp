package com.example.quotesapp.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import com.abedelazizshe.lightcompressorlibrary.config.SaveLocation
import com.abedelazizshe.lightcompressorlibrary.config.SharedStorageConfiguration
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentUploadDataBinding
import com.example.quotesapp.presentation.viewModel.HomeSharedViewModel
import com.example.quotesapp.presentation.viewModel.UploadDataViewModel
import com.example.quotesapp.util.Constants
import com.example.quotesapp.util.EventObserver
import com.example.quotesapp.util.GlideLoader
import com.example.quotesapp.util.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@AndroidEntryPoint
class UploadDataFragment : BaseFragment<FragmentUploadDataBinding>(){
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUploadDataBinding =
        FragmentUploadDataBinding::inflate

    val viewModel by viewModels<UploadDataViewModel>()
    private val homeSharedViewModel by activityViewModels<HomeSharedViewModel>()

    private var headerImageLauncher: ActivityResultLauncher<Intent>? = null
    private var headerImagePart: MultipartBody.Part? = null
    private var uploadedImageName: String = ""
    private val args by navArgs<UploadDataFragmentArgs>()
    var type = ""
    private  val EOF = -1
    private var isCompressionStarted=false

    override fun setup() {
        type = args.dataType
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding?.delete?.setOnClickListener {
          deleteData()
        }

        if (args.title.isEmpty()) {
            when (type) {
                Constants.VIDEO -> {
                    binding?.txtTitle?.setText(resources.getString(R.string.add_video))
                    binding?.txtUpload?.setText(getString(R.string.upload_video))
                    binding?.fieldData?.setHint(getString(R.string.add_video_title))
                    setUpVideo()
                }

                Constants.DOCUMENT -> {
                    binding?.txtTitle?.setText(getString(R.string.add_document))
                    binding?.txtUpload?.setText(getString(R.string.upload_document))
                    binding?.fieldData?.hint = getString(R.string.add_document_title)
                    setUpDocument()
                }

                Constants.IMAGE -> {
                    binding?.txtTitle?.setText(getString(R.string.add_image))
                    binding?.txtUpload?.setText(getString(R.string.upload_image))
                    binding?.fieldData?.hint = getString(R.string.add_image_title)

                    setUpImage()
                }

                Constants.QUOTE -> {
                    binding?.txtTitle?.setText(getString(R.string.add_quote))
                    binding?.txtUpload?.setText(getString(R.string.upload_quote))
                    binding?.fieldData?.hint = getString(R.string.add_caption)
                    setUpQuote()
                }
            }
        }else{
            uploadedImageName = args.file
            binding?.edtData?.setText(args.title?:"")
            when (type) {
                Constants.VIDEO -> {
                    binding?.txtTitle?.setText(getString(R.string.edit_video))
                    binding?.txtUpload?.isVisible = false
                    binding?.fieldData?.hint = getString(R.string.add_video_title)
                    binding?.uploadVideo?.isVisible = true
                    binding?.delete?.isVisible = true
                    binding?.playPause?.isVisible = true
                    val uri = Uri.parse("http://143.110.247.128:8008/user/video/"+args.file)
                    binding?.uploadVideo?.setVideoURI(uri)
                    binding?.uploadVideo?.start()
                    binding?.editVideo?.visibility = View.VISIBLE
                    setUpVideo()
                }

                Constants.DOCUMENT -> {
                    binding?.txtTitle?.setText(getString(R.string.edit_document))
                    binding?.txtUpload?.isVisible = false
                    binding?.delete?.isVisible = true
                    binding?.fieldData?.hint = getString(R.string.add_document_title)
                    binding?.txtUpload?.setText(args.file)
                    binding?.upload?.setImageResource(R.drawable.baseline_file_present_24)
                    binding?.upload?.isVisible = true
                    binding?.txtUpload?.isVisible = true
                    binding?.editDocument?.visibility = View.VISIBLE

                    setUpDocument()
                }

                Constants.IMAGE -> {
                    binding?.txtTitle?.setText(getString(R.string.edit_image))
                    binding?.txtUpload?.isVisible = false
                    binding?.delete?.isVisible = true
                    binding?.fieldData?.hint = getString(R.string.add_image_title)
                    binding?.uploadPic?.isVisible = true
                    binding?.upload?.isVisible = false
                    binding?.txtUpload?.isVisible  =false
                    binding?.editPhoto?.visibility = View.VISIBLE
                    GlideLoader(requireContext()).loadPicFromWeb(args.file,binding?.uploadPic)
                    setUpImage()

                }

                Constants.QUOTE -> {
                    binding?.txtTitle?.setText(getString(R.string.edit_quote))
                    binding?.txtUpload?.isVisible = false
                    binding?.fieldData?.hint = getString(R.string.add_caption)
                    binding?.uploadPic?.isVisible = true
                    binding?.delete?.isVisible = true
                    binding?.upload?.isVisible = false
                    binding?.txtUpload?.isVisible  =false
                    binding?.editPhoto?.visibility = View.VISIBLE
                    GlideLoader(requireContext()).loadPicFromWeb(args.file,binding?.uploadPic)
                    setUpQuote()
                }
            }
            viewModel.deleteQuote.observe(viewLifecycleOwner){
                if (it.status==false) toast(it.message)
                else {
                    homeSharedViewModel.isDataChange.value = true
                    toast(getString(R.string.successfully_deleted))
                    requireActivity().onBackPressed()
                }
            }
            viewModel.deleteVideoImageDocument.observe(viewLifecycleOwner){
                if (it.status==false) toast(it.message)
                else {
                    homeSharedViewModel.isDataChange.value = true
                    toast(getString(R.string.successfully_deleted))
                    requireActivity().onBackPressed()
                }
            }
            viewModel.updateQuote.observe(viewLifecycleOwner){
                if (it.status==false) toast(it.message)
                else {
                    homeSharedViewModel.isDataChange.value = true
                    toast(getString(R.string.updated_successfully))
                    requireActivity().onBackPressed()
                }
            }
            viewModel.updateVideoImageDocument.observe(viewLifecycleOwner){
                if (it.status==false) toast(it.message)
                else {
                    homeSharedViewModel.isDataChange.value = true
                    toast(getString(R.string.updated_successfully))
                    requireActivity().onBackPressed()
                }
            }
        }

        binding?.playPause?.setOnClickListener {
            if (binding?.uploadVideo?.isPlaying == true){
                binding?.uploadVideo?.pause()
                binding?.playPause?.setImageResource(R.drawable.baseline_play_circle_filled_24)
            }else{
                binding?.uploadVideo?.start()
                binding?.playPause?.setImageResource(R.drawable.baseline_pause_circle_24)
            }
        }
        binding?.uploadVideo?.setOnCompletionListener {
            binding?.playPause?.setImageResource(R.drawable.baseline_play_circle_filled_24)
        }

        viewModel.uploadFileLiveData.observe(viewLifecycleOwner){
            binding?.videoUploadProgressBar?.isVisible = false
            if (it.data.isNotEmpty()){
                toast(getString(R.string.uploaded))
                uploadedImageName = it.data[0].file.toString()
            }else{
                toast(it.message)
            }
            headerImagePart =null
        }

        viewModel.uploadExchangeFileLiveData.observe(viewLifecycleOwner){
            binding?.videoUploadProgressBar?.isVisible = false
            if (it.data.isNotEmpty()){
                toast(getString(R.string.uploaded))
                uploadedImageName = it.data[0].file.toString()
            }else{
                toast(it.message)
            }
            headerImagePart =null
        }


        binding?.btnSave?.setOnClickListener {
            if (!validation()) return@setOnClickListener
            if (args.title.isNotEmpty()) {
                updateData()
                return@setOnClickListener
            }
            val map = HashMap<String,String>()
            if (type!=Constants.QUOTE) {
                if(args.isExchangeFragment==1){
                    map.put("caption", binding?.edtData?.text.toString().trim())
                    map.put("item", uploadedImageName)
                    viewModel.saveExchangeFile(
                        map,
                        savedPrefManager.getToken().toString(),
                        type,
                        if (args.isNormalUser) "user" else "memberplus",
                        args.id.ifEmpty { savedPrefManager.getId().toString() })
                }
                else{
                    map.put("type", type)
                    map.put("title", binding?.edtData?.text.toString().trim())
                    map.put("name", uploadedImageName)
                    viewModel.saveFile(
                        map,
                        savedPrefManager.getToken().toString(),
                        args.id.ifEmpty { savedPrefManager.getId().toString() })
                }
                }
            else{
                val userType = if(args.isNormalUser) "user" else "memberplus"
                map.put("content", binding?.edtData?.text.toString().trim())
                map.put("file", uploadedImageName)
                viewModel.saveQuote(map, savedPrefManager.getToken().toString(),args.id.ifEmpty { savedPrefManager.getId().toString() },userType)
            }

        }

        viewModel.saveFileDataResponse.observe(viewLifecycleOwner){
            if (it.status==true) toast(getString(R.string.saved))
            homeSharedViewModel.isDataChange.value = true
            requireActivity().onBackPressed()
        }
        viewModel.saveExchangeFileDataResponse.observe(viewLifecycleOwner,EventObserver{
            if (it.status==true)
                toast(getString(R.string.saved))
            findNavController().navigate(UploadDataFragmentDirections.actionUploadDataFragmentToSentExchangeFragment(if (args.isNormalUser) "user" else "memberplus"))
        })
    }

    private fun updateData() {
        val map = HashMap<String,String>()
        if (type!=Constants.QUOTE) {
            map.put("type", type)
            map.put("title", binding?.edtData?.text.toString().trim())
            map.put("name", uploadedImageName)
            viewModel.updateData(map, savedPrefManager.getToken().toString(),args.documentImageVideoQuoteId.ifEmpty { savedPrefManager.getId().toString() })
        }
        else{
            map.put("content", binding?.edtData?.text.toString().trim())
            map.put("file", uploadedImageName)
            viewModel.updateQuote(map, savedPrefManager.getToken().toString(),args.documentImageVideoQuoteId.ifEmpty { savedPrefManager.getId().toString() })
        }
    }


    private fun deleteData() {

        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(getString(R.string.delete) + args.dataType)
        alertDialog.setMessage(getString(R.string.do_you_want_to_delete) + args.dataType + "?")
        alertDialog.setNegativeButton(
            getString(R.string.cancel)
        ) { _, _ ->
            getString(R.string.cancelled)
        }
        alertDialog.setPositiveButton(getString(R.string.yes)){ _,_->
            if (type==Constants.QUOTE) {
                viewModel.deleteQuote(savedPrefManager.getToken().toString(),args.documentImageVideoQuoteId.ifEmpty { savedPrefManager.getId().toString() })
            }
            else{
                viewModel.deleteData( savedPrefManager.getToken().toString(),args.documentImageVideoQuoteId.ifEmpty { savedPrefManager.getId().toString() })
            }
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    private fun setUpQuote() {
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
                            uploadOnServer()
                        }
                    } catch (e: NullPointerException) {
                        println(e.printStackTrace())
                    }
                } else {
                    Log.e("Error", "Upload Error")
                }
            }
        binding?.viewUpload?.setOnClickListener {
            checkPermission()
        }
        binding?.editPhoto?.setOnClickListener {
            checkPermission()
        }
    }

    private fun setUpImage() {
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
                            uploadOnServer()
                        }
                    } catch (e: NullPointerException) {
                        println(e.printStackTrace())
                    }
                } else {
                    Log.e("Error", "Upload Error")
                }
            }

        binding?.viewUpload?.setOnClickListener {
            checkPermission()
        }
        binding?.editPhoto?.setOnClickListener {
            checkPermission()
        }

    }

    private fun uploadOnServer() {
        if (args.isExchangeFragment==1){
            when(type){
                Constants.VIDEO->{
                    viewModel.uploadExchangeUserFile(headerImagePart,Constants.VIDEO,savedPrefManager.getToken().toString())
                }
                Constants.DOCUMENT->{
                    viewModel.uploadExchangeUserFile(headerImagePart,Constants.DOCUMENT,savedPrefManager.getToken().toString())
                }
                Constants.IMAGE,Constants.QUOTE->{
                    viewModel.uploadExchangeUserFile(headerImagePart,Constants.IMAGE,savedPrefManager.getToken().toString())
                }
            }
            return
        }
        if (args.isNormalUser){
            when(type){
                Constants.VIDEO->{
                    viewModel.uploadUserFile(headerImagePart,Constants.VIDEO,savedPrefManager.getToken().toString())
                }
                Constants.DOCUMENT->{
                    viewModel.uploadUserFile(headerImagePart,Constants.DOCUMENT,savedPrefManager.getToken().toString())
                }
                Constants.IMAGE,Constants.QUOTE->{
                    viewModel.uploadUserFile(headerImagePart,Constants.IMAGE,savedPrefManager.getToken().toString())
                }
            }
        }else {
            when (type) {
                Constants.VIDEO -> {
                    viewModel.uploadUserFile(headerImagePart, Constants.VIDEO)
                }

                Constants.DOCUMENT -> {
                    viewModel.uploadUserFile(headerImagePart, Constants.DOCUMENT)
                }

                Constants.IMAGE, Constants.QUOTE -> {
                    viewModel.uploadUserFile(headerImagePart, Constants.IMAGE)
                }
            }
        }
    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK)
        when(type){
            Constants.VIDEO->{
                intent.type = "video/*"
            }
            Constants.DOCUMENT->{
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "application/*"
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                headerImageLauncher?.launch(intent)
                return
            }
            Constants.IMAGE,Constants.QUOTE->{
                intent.type = "image/*"
            }
        }
        headerImageLauncher?.launch(intent)

    }

    private val requestMultiplePermissions =  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions ->
        permissions.entries.forEach {
            Log.e("DEBUG", "${it.key} = ${it.value}")
            getImage()
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
            getImage()
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
            getImage()
        } else {
            requestMultiplePermissions.launch(permissions())
        }
    }

    private fun setUpVideo() {

        headerImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {

                    val intent = result.data
                    val imageUri = intent?.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor =
                        imageUri?.let { requireContext().contentResolver.query(it, filePathColumn, null, null, null) }
                    cursor?.moveToFirst()
                    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                    val filePath = columnIndex?.let { cursor?.getString(it) }
                    cursor?.close()

                    val file = filePath?.let { File(it) }
                    val ls = file?.length()
                    val fileSizeInMb = ls?.div((1024 * 1024))
                    println(fileSizeInMb)
                    if (fileSizeInMb != null) {
                        if (fileSizeInMb>100) {
                            println(fileSizeInMb)
                            toast("Upload File Less Than 100MB")
                            return@registerForActivityResult
                        }
                    }

                    binding?.uploadVideo?.isVisible = true
                    binding?.playPause?.isVisible = true
                    binding?.uploadVideo?.setVideoURI(imageUri)
                    binding?.uploadVideo?.start()
                    lifecycleScope.launch(Dispatchers.IO){

                        if (imageUri == null) return@launch

                        try {
                            VideoCompressor.start(
                                context = requireContext(), // => This is required
                                uris = listOf(imageUri), // => Source can be provided as content uris
                                isStreamable = true,
                                // THIS STORAGE
                                // OR AND NOT BOTH
                                sharedStorageConfiguration = SharedStorageConfiguration(
                                    saveAt = SaveLocation.movies, // => default is movies
                                    videoName = "compressed_video ${System.currentTimeMillis()}" // => required name
                                ),
                                configureWith = Configuration(
                                    quality = VideoQuality.VERY_LOW,
                                    isMinBitrateCheckEnabled = false,
                                    videoBitrateInMbps = 1, /*Int, ignore, or null*/
                                    disableAudio = false, /*Boolean, or ignore*/
                                    keepOriginalResolution = false, /*Boolean, or ignore*/
                                    videoHeight = 480.0,
                                    videoWidth = 360.0
                                ),
                                listener = object : CompressionListener {
                                    override fun onCancelled(index: Int) {
                                        toast("Upload Cancelled")
                                        isCompressionStarted = false
                                        binding?.videoUploadProgressBar?.isVisible = false

                                    }

                                    override fun onFailure(index: Int, failureMessage: String) {
                                        toast(failureMessage)
                                        isCompressionStarted = false
                                        binding?.videoUploadProgressBar?.isVisible = false

                                    }

                                    override fun onProgress(index: Int, percent: Float) {
                                        // Update UI with progress value
                                        println(percent)
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            binding?.videoUploadProgressBar?.setProgress(
                                                percent.toInt(),
                                                true
                                            )
                                        } else {
                                            binding?.videoUploadProgressBar?.progress =
                                                percent.toInt()
                                        }

                                    }

                                    override fun onStart(index: Int) {
                                        println("Start")
                                        isCompressionStarted = true
                                        binding?.videoUploadProgressBar?.isVisible = true
                                        toast(getString(R.string.please_wait_while_video_is_getting_uploaded))
                                    }

                                    override fun onSuccess(index: Int, size: Long, path: String?) {
                                        isCompressionStarted = false
                                        println(path)
                                        val imageFile = path?.let { File(it) }
                                        val requestBody =
                                            imageFile?.asRequestBody("video/*".toMediaTypeOrNull())
                                        if (imageFile != null) {
                                            headerImagePart = requestBody?.let {
                                                MultipartBody.Part.createFormData(
                                                    "video", imageFile.name,
                                                    it
                                                )
                                            }
                                            uploadOnServer()
                                        }
                                    }
                                }
                            )
                        }catch (e:java.lang.Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }
        binding?.viewUpload?.setOnClickListener {
            checkPermission()
        }
        binding?.editVideo?.setOnClickListener {
            checkPermission()
        }
    }


    @SuppressLint("Range")
    private fun setUpDocument() {
        headerImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    try {
                        val intent = result.data
                        val uri = intent?.data
                        val uriString: String = uri.toString()
                        var pdfName: String? = null
                        if (uriString.startsWith("content://")) {
                            var myCursor: Cursor? = null
                            try {
                                // Setting the PDF to the TextView
                                myCursor = uri?.let {
                                    requireContext().contentResolver.query(
                                        it,
                                        null,
                                        null,
                                        null,
                                        null
                                    )
                                }
                                if (myCursor != null && myCursor.moveToFirst()) {
                                    pdfName =
                                        myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    binding?.txtUpload?.setText(pdfName)
                                    binding?.upload?.setImageResource(R.drawable.baseline_file_present_24)
                                    binding?.editDocument?.isVisible = true
                                }
                            } finally {
                                myCursor?.close()
                            }
                        }
                        val file = uri?.let { from(requireContext(), it) }
                        val requestFile =
                            file?.let {
                                it.asRequestBody("application/*".toMediaTypeOrNull())
                            }
                        headerImagePart = requestFile?.let {
                            MultipartBody.Part.createFormData("document", file.name,
                                it
                            )
                        }
                        uploadOnServer()
                }catch (e:Exception){
                    e.printStackTrace()
                }
                }else {
                    Log.e("Error", "Upload Error")
                }
            }
        binding?.viewUpload?.setOnClickListener {
            checkPermission()
        }
        binding?.editDocument?.setOnClickListener {
            checkPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        val view = requireActivity().findViewById<View>(R.id.bottom_nav_view)
        view.visibility = View.GONE
    }


    private fun validation(): Boolean {
        if (isCompressionStarted){
            toast(getString(R.string.file_is_getting_uploaded_please_wait))
            return false
        }
        if (headerImagePart!=null){
            toast(getString(R.string.file_is_getting_uploaded_please_wait))
            return false
        }
        if (uploadedImageName.isEmpty() && type  != Constants.QUOTE){
            toast(getString(R.string.upload_first))
            return false
        }
        if (binding?.edtData?.text?.trim()?.isEmpty()==true){
            toast(getString(R.string.enter_caption))
            return false
        }
        return true
    }

    companion object {
        private const val PERMISSION = 999
    }

    fun from(context: Context, uri: Uri, ext : String? = null): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        var fileName = context.getFileName(uri)
        val splitName = splitFileName(fileName)
        ext?.let {
            if(splitName[1]?.isEmpty() == true)
            {
                fileName = "$fileName$ext"
                splitName[1] = ext
            }

        }

        var tempFile = File.createTempFile(splitName[0], splitName[1])
        tempFile = rename(tempFile, fileName)
        tempFile.deleteOnExit()
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(tempFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        if (inputStream != null) {
            copy(inputStream, out)
            inputStream.close()
        }
        out?.close()
        return tempFile
    }

    @Throws(IOException::class)
    private fun copy(input: InputStream, output: OutputStream?): Long {
        var count: Long = 0
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (EOF != input.read(buffer).also { n = it }) {
            output?.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }

    private fun rename(file: File, newName: String?): File = File(file.parent, newName).apply {
        if (this != file) {
            if (this.exists() && this.delete())
                if (file.renameTo(this))
                    return@apply
        }
    }

    fun splitFileName(fileName: String?): Array<String?> {
        var name:String? = fileName
        var extension: String? = ""
        fileName?.lastIndexOf(".")?.let {
            if (it != -1) {
                name = fileName.substring(0, it)
                extension = fileName.substring(it)
            }
        }
        return arrayOf(name, extension)
    }

    fun Context.getFileName(uri: Uri): String? = when(uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
        else -> uri.path?.let(::File)?.name
    }

    private fun Context.getContentFileName(uri: Uri): String? = runCatching {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME).let(cursor::getString)
        }
    }.getOrNull()

}
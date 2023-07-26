package com.example.quotesapp.presentation.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.quotesapp.util.SavedPrefManager

abstract class BaseFragment<T: ViewBinding>  : Fragment() {
    protected var binding: T? = null
    open lateinit var savedPrefManager: SavedPrefManager


    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T

    var storge_permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    var storge_permissions_33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO
    )

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    var storge_permissions_33_img = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )

    fun permissions(): Array<String> {
        val p: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storge_permissions_33
        } else {
            storge_permissions
        }
        return p
    }

    fun permissionsImage(): Array<String> {
        val p: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storge_permissions_33_img
        } else {
            storge_permissions
        }
        return p
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = bindingInflater.invoke(inflater, container, false)


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedPrefManager = SavedPrefManager(requireContext())
        setup()

    }

    abstract fun setup()

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

    fun toast(message:String?){
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }


}
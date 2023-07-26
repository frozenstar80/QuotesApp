package com.example.quotesapp.presentation.bottomSheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.example.quotesapp.util.SavedPrefManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheetFragment<T: ViewBinding>  : BottomSheetDialogFragment() {
    protected var binding: T? = null
    open lateinit var savedPrefManager: SavedPrefManager


    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T



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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        return bottomSheetDialog
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
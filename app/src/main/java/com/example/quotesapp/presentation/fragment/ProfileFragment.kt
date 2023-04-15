package com.example.quotesapp.presentation.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quotesapp.databinding.FragmentProfileBinding
import com.example.quotesapp.presentation.activities.HomeActivity
import java.util.*


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding =
        FragmentProfileBinding::inflate

    override fun setup() {
        binding?.btnCreateProfile?.setOnClickListener {
        savedPrefManager.putLogin(true)
           startActivity(Intent(requireActivity(),HomeActivity::class.java))
            requireActivity().finish()
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
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }

    }
}
package com.example.quotesapp.presentation.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quotesapp.databinding.FragmentSearchPeopleBinding


class SearchPeopleFragment : BaseFragment<FragmentSearchPeopleBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchPeopleBinding =
        FragmentSearchPeopleBinding::inflate

    override fun setup() {

    }
}
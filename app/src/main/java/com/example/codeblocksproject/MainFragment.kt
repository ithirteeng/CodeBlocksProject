package com.example.codeblocksproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.codeblocksproject.databinding.FragmentMainBinding


class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        setupButtonClickListeners()
        return binding.root
    }

    private fun setupButtonClickListeners() {
        toBlocksButtonEvent()
        toTextWorkspaceButtonEvent()
    }

    private fun toBlocksButtonEvent() {
        binding.buttonToWorkspace.setOnClickListener {
            findNavController().navigate(R.id.workspaceFragment)
        }
    }

    private fun toTextWorkspaceButtonEvent() {
        binding.buttonToTextWorkspace.setOnClickListener {
            findNavController().navigate(R.id.textCodingFragment)
        }
    }


}
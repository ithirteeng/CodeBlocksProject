package com.example.codeblocksproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
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
            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }
            findNavController().navigate(R.id.workspaceFragment, null, options)
        }
    }

    private fun toTextWorkspaceButtonEvent() {
        binding.buttonToTextWorkspace.setOnClickListener {
            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }
            findNavController().navigate(R.id.textCodingFragment, null, options)
        }
    }
}
package com.example.codeblocksproject

import com.example.codeblocksproject.interpreter.Lexer
import com.example.codeblocksproject.interpreter.Parser
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.codeblocksproject.databinding.FragmentTextCodingBinding
import com.example.codeblocksproject.ui.UserInterfaceClass

class TextCodingFragment : Fragment(R.layout.fragment_text_coding) {
    private val consoleFragment = ConsoleFragment()
    private val blocksFragment = BlocksFragment()
    private lateinit var binding: FragmentTextCodingBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ui = UserInterfaceClass(view.context, consoleFragment, blocksFragment)
        ui.setupAllUIFunctions(view)
        setupOtherFragmentsFunctions()
        backToMenuButtonEvent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextCodingBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun setupOtherFragmentsFunctions() {
        addingFragments()
        startButtonEvent()
    }

    private fun addingFragments() {
        val fragmentManager = childFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.container, consoleFragment)
        transaction.hide(consoleFragment)
        transaction.commit()
    }

    private fun startButtonEvent() {
        binding.startButton.setOnClickListener {
            if (consoleFragment.getIsClosedStart()) {
                consoleFragment.setISClosedStart(false)
                val fragmentManager = childFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.bottom_panel_slide_out, 0)
                consoleFragment.onCreateAnimation(0, true, 1)
                transaction.show(consoleFragment)
                transaction.commit()

                Handler(Looper.getMainLooper()).postDelayed({
                    kotlin.run {
                        binding.startButton.visibility = View.GONE
                    }
                }, 350)
                try {
//                    val lexer = Lexer(binding.codingField.text.toString(), DEBUG = true)
//                    val tokens = lexer.lexicalAnalysis()
//
//                    tokens.forEach { x -> println(x.aboutMe()) }
//                    var answer = ""
//                    val parser = Parser(tokens, DEBUG = true)
//                    val array = parser.run()
//
//                    for (string in array) {
//                        if (string != "") {
//                            answer += "$string\n"
//                        }
//                    }
//                    consoleFragment.resultsToConsole(answer)
                } catch (e: Exception) {
                    consoleFragment.resultsToConsole(e.message.toString())
                }


            }

        }
    }

    fun displayButtons() {
        binding.startButton.visibility = View.VISIBLE
    }

    private fun backToMenuButtonEvent() {
        view?.findViewById<Button>(R.id.backToMainButton)?.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }
    }
}
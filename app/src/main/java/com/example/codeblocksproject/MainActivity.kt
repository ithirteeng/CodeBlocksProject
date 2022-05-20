package com.example.codeblocksproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.codeblocksproject.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onDestroy() {
        val fragment = supportFragmentManager.findFragmentById(R.id.workspaceFragment)
        (fragment as WorkspaceFragment).saveData()
        super.onDestroy()
    }
}
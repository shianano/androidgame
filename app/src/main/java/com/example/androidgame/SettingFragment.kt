package com.example.androidgame

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidgame.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {


    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.titleButton.setOnClickListener{
            val intent = Intent(activity,StaActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }



}
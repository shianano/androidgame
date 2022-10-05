package com.example.androidgame

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.androidgame.databinding.FragmentSettingBinding
import java.util.*


class NextFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        var volume = AudioManager.STREAM_MUSIC
        binding.ringVolSeekBarr.setProgress(0)
        binding.ringVolSeekBarr.setMax(100)
        binding.pervolume.text = 0.toString() + "%"
        

        return binding.root
    }


}




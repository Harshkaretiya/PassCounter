package com.example.passcounter.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.passcounter.Extra.NotificationService
import com.example.passcounter.R
import com.example.passcounter.databinding.ActivityMainBinding
import com.example.passcounter.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    lateinit var binding :ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val notify = sharedPreferences.getBoolean("notify", true)

        if (notify)
        {
            binding.notificationSwitch.isChecked = true
        }

        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch is ON, perform action
                startService(Intent(applicationContext, NotificationService::class.java))
                val sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("notify", true)
                editor.apply()
            } else {
                // Switch is OFF, perform another action
                stopService(Intent(applicationContext,NotificationService::class.java))
                val sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("notify", false)
                editor.apply()
            }
        }

    }
}
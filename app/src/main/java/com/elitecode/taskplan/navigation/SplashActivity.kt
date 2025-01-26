package com.elitecode.taskplan.navigation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.elitecode.taskplan.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity: ComponentActivity() {
    private val splashScreenDuration = 1200L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setTheme(R.style.Theme_TaskPlan_Splash)

        lifecycleScope.launch {
            delay(splashScreenDuration)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }

    }
}
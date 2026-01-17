package com.halt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class BlockActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block)

        val reason = intent.getStringExtra("REASON") ?: getString(R.string.reels_blocked)
        findViewById<TextView>(R.id.tvTitle).text = reason

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            // Go back to home screen or minimize
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
            finish()
        }
    }

    override fun onBackPressed() {
        // Prevent back press from dismissing the overlay easily, require explicit exit
        // or just behave like the button
        super.onBackPressed() 
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }
}

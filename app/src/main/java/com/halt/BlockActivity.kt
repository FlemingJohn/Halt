package com.halt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class BlockActivity : AppCompatActivity() {

        private val focusMessages =
                listOf(
                        "STAY LURKING?" to
                                "The algorithm is winning. You have dreams that don't involve a scroll wheel.",
                        "NOT AGAIN." to
                                "You didn't open this app to scroll for 20 minutes. Get back to what matters.",
                        "TIME IS LEAKING." to
                                "Every reel you watch is a minute you'll never get back. Stop the leak.",
                        "IS THIS IT?" to
                                "Is this really how you want to spend your peak hours? Wake up.",
                        "SCROLL TRAP." to
                                "You've been caught in the loop. Break free and do something real.",
                        "CHOOSE GROWTH." to
                                "Discipline is choosing between what you want now and what you want most.",
                        "FOCUS REGAINED." to
                                "The work you are avoiding is the work you need to do. Start now.",
                        "YOUR FUTURE SELF." to
                                "Will your future self thank you for this? Close the app and create.",
                        "BUILDING CAPACITY." to
                                "Attention is your most valuable resource. Don't trade it for cheap dopamine.",
                        "STAY HUNGRY." to
                                "The world is waiting for your contribution, not your views. Get back to it."
                )

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_block)

                val (title, subtitle) = focusMessages.random()

                findViewById<TextView>(R.id.tvTitle).text = title
                findViewById<TextView>(R.id.tvSubtitle).text = subtitle

                findViewById<Button>(R.id.btnBack).setOnClickListener { handleExit() }

                // Modern back press handling
                onBackPressedDispatcher.addCallback(
                        this,
                        object : OnBackPressedCallback(true) {
                                override fun handleOnBackPressed() {
                                        handleExit()
                                }
                        }
                )
        }

        private fun handleExit() {
                // Go back to home screen or minimize
                val startMain = Intent(Intent.ACTION_MAIN)
                startMain.addCategory(Intent.CATEGORY_HOME)
                startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(startMain)
                finish()
        }
}

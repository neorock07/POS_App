package com.example.pos.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.pos.R
import java.lang.Exception
import java.lang.Thread.sleep

class Splash : AppCompatActivity() {
    private lateinit var cardSplash:CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //define
        cardSplash = findViewById(R.id.card_splash)
        //threading

        val thread:Thread = Thread(){
            kotlin.run {
                try{
                    cardSplash.setOnClickListener(){
                        startActivity(Intent(this@Splash, formBarang::class.java))
                        finish()
                    }
                }catch (e:Exception){
                    Toast.makeText(this@Splash, "Kesalahan :\n" + e.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
        thread.start()
    }
}
package com.example.pos.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.pos.R

class form_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_login)
        val kembali: ImageView= findViewById(R.id.kembali)

//        val username:String = "Login"
//        val password:String = "123"

        val ed_username: EditText = findViewById(R.id.et_username)
        val ed_password: EditText = findViewById(R.id.et_password)
        val btn_login: CardView = findViewById(R.id.btn_login)
        btn_login.setOnClickListener {
            if (ed_username.text.isNotEmpty() && ed_password.text.isNotEmpty()) {
                if (ed_username.text.toString().equals("login") && ed_password.text.toString()
                        .equals("123")
                ) {
                    val intenlogin = Intent(this@form_login, settingAplikasi::class.java)
                    startActivity(intenlogin)
                    ed_password.text.clear()
                    ed_username.text.clear()
                } else {
                    Toast.makeText(this, "Username/Password Salah", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Username & Password harus diisi!", Toast.LENGTH_SHORT).show()
                ed_username.error = "Tidak boleh kosong"
                ed_password.error = "Tidak boleh kosong"
            }
        }
        kembali.setOnClickListener{
                    onBackPressed()
                }
    }
}

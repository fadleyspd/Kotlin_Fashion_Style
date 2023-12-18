package com.example.bfore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.bfore.api.ApiConfig
import com.example.bfore.model.ResponModel
import retrofit2.Call

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btn_login: Button
    private lateinit var txt_rgs: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login = findViewById(R.id.btn_login)
        btn_login.setOnClickListener(this)

        txt_rgs = findViewById(R.id.txt_rgs)
        txt_rgs.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                login()
            }
            R.id.txt_rgs -> {
                val moveIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(moveIntent)
            }
        }
    }

    fun login(){
        val emailEdit  = findViewById<EditText>(R.id.txtemail)
        val passwordEdit  = findViewById<EditText>(R.id.txtpass)

        if (emailEdit.text.isEmpty()) {
            emailEdit.error = "Email Wajib Di isi!"
            emailEdit.requestFocus()
            return
        }   else if (passwordEdit.text.isEmpty()) {
            passwordEdit.error = "Password Wajib Di isi!"
            passwordEdit.requestFocus()
            return
        }

        ApiConfig.instanceRetrofit.login(emailEdit.text.toString(),passwordEdit.text.toString()).enqueue(object :
            retrofit2.Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: retrofit2.Response<ResponModel>) {
                Log.d("LoginActivity", "Response code: ${response.code()}")
                Log.d("LoginActivity", "Response body: ${response.body()}")
                val respon = response.body()
                if (respon != null && respon.status == 200) {
                    Toast.makeText(this@LoginActivity, "Login Berhasil, Otw Beranda", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()  // Menutup aktivitas saat ini agar pengguna tidak dapat kembali ke halaman pendaftaran
                    }, 2000)
                } else {
                    Toast.makeText(this@LoginActivity, respon?.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                    Log.d("LoginActivity", "Response body: $respon")
                }
            }
        })
    }
}
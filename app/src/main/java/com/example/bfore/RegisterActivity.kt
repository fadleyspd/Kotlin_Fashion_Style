package com.example.bfore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.bfore.api.ApiConfig
import com.example.bfore.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btn_register: Button
    private lateinit var txt_lgn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btn_register = findViewById(R.id.btn_register)
        btn_register.setOnClickListener(this)

        txt_lgn = findViewById(R.id.txt_lgn)
        txt_lgn.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                register()
            }
            R.id.txt_lgn -> {
                val moveIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(moveIntent)
            }
        }
    }

    fun register(){
        val usernameEdit  = findViewById<EditText>(R.id.username)
        val passwordEdit  = findViewById<EditText>(R.id.pass)
        val email  = findViewById<EditText>(R.id.email)

        if (usernameEdit.text.isEmpty()) {
            usernameEdit.error = "Kolom Username tidak boleh kosong"
            usernameEdit.requestFocus()
            return
        } else if (email.text.isEmpty()) {
            email.error = "Kolom Email tidak boleh kosong"
            email.requestFocus()
            return
        } else if (passwordEdit.text.isEmpty()) {
            passwordEdit.error = "Kolom Password tidak boleh kosong"
            passwordEdit.requestFocus()
            return
        }
        ApiConfig.instanceRetrofit.register(usernameEdit.text.toString(), email.text.toString(), passwordEdit.text.toString()).enqueue(object :
            Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                val respon =response.body() !!

                if (respon.status == 200){
                    Toast.makeText(this@RegisterActivity, "Berhasil Membuat Akun, Silahkan Login", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this@RegisterActivity, com.example.bfore.LoginActivity::class.java)
                        startActivity(intent)
                        finish()  // Menutup aktivitas saat ini agar pengguna tidak dapat kembali ke halaman pendaftaran
                    }, 2000)

                }else {
                    Toast.makeText(this@RegisterActivity, "Gagal :" + respon.message, Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}
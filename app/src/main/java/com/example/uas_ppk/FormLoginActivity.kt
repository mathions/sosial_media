package com.example.uas_ppk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uas_ppk.databinding.ActivityFormLoginBinding
import com.example.uas_ppk.shared_preferences.PrefManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormLoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormLoginBinding
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this)

        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this, FormRegisterActivity::class.java))
        }

    }

    fun doLogin(){
        val username = binding.txtUsername.text.toString()
        val userpassword = binding.txtPassword.text.toString()

        if(username.isEmpty() || username == ""){
            binding.txtUsername.setError("Username is required")
            binding.txtUsername.requestFocus()
        }else if(userpassword.isEmpty() || userpassword == ""){
            binding.txtPassword.setError("Password is required")
            binding.txtPassword.requestFocus()
        }else {
            RClient.instance.checkUserLogin(username,userpassword)
                .enqueue(object : Callback<ResponseLogin>{
                    override fun onResponse(
                        call: Call<ResponseLogin>,
                        response: Response<ResponseLogin>
                    ) {
                        if(response.isSuccessful){
                            response.body()?.let { prefManager.setToken(it.token) }
                            response.body()?.let { prefManager.setUsername(it.username) }
                            response.body()?.let { prefManager.setEmail(it.email) }

                            Toast.makeText(applicationContext,"${response.body()?.msg}",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@FormLoginActivity,MainActivity::class.java))
                            finish()
                        }else{
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            val messagesError = JSONObject(jsonObj.getString("messages"))

                            if(messagesError.getString("error") != ""){
                                binding.txtUsername.setError(messagesError.getString("error"))
                                binding.txtPassword.setText("")
                                binding.txtUsername.requestFocus()
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {

                    }
                })
        }
    }
}
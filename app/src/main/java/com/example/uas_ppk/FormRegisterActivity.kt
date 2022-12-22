package com.example.uas_ppk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uas_ppk.databinding.ActivityFormAddContentBinding
import com.example.uas_ppk.databinding.ActivityFormRegisterBinding
import com.example.uas_ppk.shared_preferences.PrefManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormRegisterBinding
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager(this)

        binding.btnRegister.setOnClickListener {
            saveData()
        }

        binding.btnLogin.setOnClickListener{
            startActivity(Intent(this, FormLoginActivity::class.java))
        }

    }

    fun saveData() {
        with(binding) {
            val username = txtUsername.text.toString()
            val userpassword = txtUserpassword.text.toString()
            val useremail = txtUseremail.text.toString()
            val fullname = txtFullname.text.toString()

            RClient.instance.createUser(username, userpassword, useremail, fullname)
                .enqueue(object :
                    Callback<ResponseCreate> {
                    override fun onResponse(
                        call: Call<ResponseCreate>,
                        response: Response<ResponseCreate>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "${response.body()?.pesan}",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this@FormRegisterActivity,FormLoginActivity::class.java))
                            finish()
                        } else {
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            val messagesError = JSONObject(jsonObj.getString("message"))
                            if(messagesError.getString("error") != ""){
                                binding.txtUsername.setError(messagesError.getString("error"))
                                binding.txtUsername.requestFocus()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {

                    }

                })

        }
    }
}

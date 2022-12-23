package com.example.uas_ppk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uas_ppk.databinding.ActivityEditProfilBinding
import com.example.uas_ppk.shared_preferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfilActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfilBinding
    private lateinit var prefManager: PrefManager
    private var b:Bundle? = null
    private val profile = ArrayList<Profile>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager(this)

        b = intent.extras

        val id = prefManager.getId()

        getProfile(id)

        binding.btnUpdate.setOnClickListener {
            with(binding){
                val email = txtEmail.text.toString()
                val fullname = txtFullname.text.toString()
                val userid = txtUserid.text.toString()
                RClient.instance.updateProfil(userid,email,fullname).enqueue(object : Callback<ResponseEditProfile>{
                    override fun onResponse(
                        call: Call<ResponseEditProfile>,
                        response: Response<ResponseEditProfile>
                    ) {
                       if(response.isSuccessful){
                           Toast.makeText(applicationContext,"${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                           finish()
                       }
                    }
                    override fun onFailure(call: Call<ResponseEditProfile>, t: Throwable) {
                        Toast.makeText(applicationContext,"gagal", Toast.LENGTH_LONG).show()
                        finish()
                    }
                })

            }

        }
    }

    fun getProfile(id: String?){
        RClient.instance.getProfile(id).enqueue(object : Callback<ResponseProfile> {
            override fun onResponse(
                call: Call<ResponseProfile>,
                response: Response<ResponseProfile>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { profile.addAll(it.data) }
                    with(binding){
                        txtUsername.setText(profile[0].username)
                        txtEmail.setText(profile[0].useremail)
                        txtFullname.setText(profile[0].fullname)
                        txtUserid.setText(profile[0].id)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseProfile>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}


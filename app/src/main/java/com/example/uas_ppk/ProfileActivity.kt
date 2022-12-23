package com.example.uas_ppk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.uas_ppk.databinding.ActivityProfileBinding
import com.example.uas_ppk.shared_preferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private var b:Bundle? = null
    private val profile = ArrayList<Profile>()
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager(this)
        b = intent.extras

        val id = prefManager.getId()

        getProfile(id)

        binding.btnEdit.setOnClickListener {
            startActivity(Intent(this,EditProfilActivity::class.java))
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

                        tvUsername.text = profile[0].username
                        tvEmail.text = profile[0].useremail
                        tvFullname.text = profile[0].fullname

                        Glide.with(applicationContext)
                            .load("${RClient.BASE_URL+"profile/" + profile[0].image}")
                            .into(binding.image)

                    }
                }
            }

            override fun onFailure(call: Call<ResponseProfile>, t: Throwable) {

            }
        })
    }




}
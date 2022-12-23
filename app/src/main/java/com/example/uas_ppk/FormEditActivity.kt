package com.example.uas_ppk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.uas_ppk.databinding.ActivityFormEditBinding
import com.example.uas_ppk.shared_preferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormEditActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormEditBinding
    private lateinit var prefManager: PrefManager
    private var b:Bundle? = null
    private val listContent = ArrayList<ContentData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager(this)

        b = intent.extras
        val id = b?.getString("id")

        id?.let { getDataDetail(it) }

        binding.btnAdd.setOnClickListener {
            with(binding){
                val caption = txtCaption.text.toString()
                val token_auth = "Bearer ${prefManager.getToken()}"
                RClient.instance.updateData(token_auth,id,caption).enqueue(object : Callback<ResponseCreate>{
                    override fun onResponse(
                        call: Call<ResponseCreate>,
                        response: Response<ResponseCreate>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(applicationContext,"${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {

                    }

                })
            }

        }
    }

    fun getDataDetail(id:String){
        val token_auth = "Bearer ${prefManager.getToken()}"
        RClient.instance.getData(token_auth,id).enqueue(object : Callback<ResponseDataContent> {
            override fun onResponse(
                call: Call<ResponseDataContent>,
                response: Response<ResponseDataContent>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { listContent.addAll(it.data) }
                    with(binding){
                        txtCaption.setText(listContent[0].caption)

                        tvUsername.text = listContent[0].username

                        Glide.with(applicationContext)
                            .load("${RClient.BASE_URL+"profile/" + listContent[0].username}")
                            .into(binding.profileImage)

                        Glide.with(applicationContext)
                            .load("${RClient.BASE_URL+"uploads/" + listContent[0].image}")
                            .into(binding.image)

                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataContent>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}
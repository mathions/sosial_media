package com.example.uas_ppk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uas_ppk.databinding.ActivityFormEditBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormEditActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormEditBinding
    private var b:Bundle? = null
    private val listContent = ArrayList<ContentData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Form Edit"

        b = intent.extras
        val id = b?.getString("id")

        id?.let { getDataDetail(it) }

        binding.btnAdd.setOnClickListener {
            with(binding){
                val caption = txtCaption.text.toString()
                RClient.instance.updateData(id,caption).enqueue(object : Callback<ResponseCreate>{
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
        RClient.instance.getData(id).enqueue(object : Callback<ResponseDataContent> {
            override fun onResponse(
                call: Call<ResponseDataContent>,
                response: Response<ResponseDataContent>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { listContent.addAll(it.data) }
                    with(binding){
                        txtImage.setText(listContent[0].image)
                        txtCaption.setText(listContent[0].caption)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataContent>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}
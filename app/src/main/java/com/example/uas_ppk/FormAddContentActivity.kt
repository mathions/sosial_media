package com.example.uas_ppk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uas_ppk.databinding.ActivityFormAddContentBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormAddContentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormAddContentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAddContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener{
            saveData()
        }
    }

    fun saveData(){
        with(binding){
            val txtUsername = "user"
            val username = txtUsername.toString()
            val image = txtImage.toString()
            val caption = txtCaption.toString()
            val date = txtDate.toString()

            RClient.instance.createData(username,image,caption,date).enqueue(object : Callback<ResponseCreate>{
                override fun onResponse(
                    call: Call<ResponseCreate>,
                    response: Response<ResponseCreate>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(applicationContext,"${response.body()?.pesan}",Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
//                        txtUsername.setError(jsonObj).getString("message")
                    }
                }

                override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {

                }

            })

        }
    }
}
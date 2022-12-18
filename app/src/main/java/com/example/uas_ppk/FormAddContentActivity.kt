package com.example.uas_ppk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uas_ppk.databinding.ActivityFormAddContentBinding
import com.example.uas_ppk.shared_preferences.PrefManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormAddContentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormAddContentBinding
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAddContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager(this)
        binding.btnAdd.setOnClickListener{
            saveData()
        }
    }

    fun saveData(){
        with(binding){
            val username = "user"
            val image = txtImage.text.toString()
            val caption = txtCaption.text.toString()
            val date = txtDate.text.toString()
            val token_auth = "Bearer ${prefManager.getToken()}"

            RClient.instance.createData(token_auth,username,image,caption,date).enqueue(object : Callback<ResponseCreate>{
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
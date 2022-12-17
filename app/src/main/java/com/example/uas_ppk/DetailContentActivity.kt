package com.example.uas_ppk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.uas_ppk.databinding.ActivityDetailContentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailContentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailContentBinding
    private var b:Bundle? = null
    private val listContent = ArrayList<ContentData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        b = intent.extras
        val id = b?.getString("id")

        id?.let { getDataDetail(it) }

        binding.btnDelete.setOnClickListener{
            id?.let { it1 -> deleteData(it1) }
        }

    }

    fun getDataDetail(id:String){
        RClient.instance.getData(id).enqueue(object : Callback<ResponseDataContent>{
            override fun onResponse(
                call: Call<ResponseDataContent>,
                response: Response<ResponseDataContent>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { listContent.addAll(it.data) }
                    with(binding){
                        tvId.text = listContent[0].id
                        tvImage.text = listContent[0].image
                        tvCaption.text = listContent[0].caption
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataContent>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun deleteData(idc: String){
        val builder = AlertDialog.Builder(this@DetailContentActivity)
        builder.setMessage("Are you will delete this content ?")
            .setCancelable(false)
            .setPositiveButton("Yes, delete"){dialog, id->
                doDeleteData(idc)
            }
            .setNegativeButton("No, cancel"){dialog, id->
                dialog.dismiss()
            }
        var alert = builder.create()
        alert.show()
    }

    private fun doDeleteData(id:String) {
        RClient.instance.deleteData(id).enqueue(object : Callback<ResponseCreate>{
            override fun onResponse(
                call: Call<ResponseCreate>,
                response: Response<ResponseCreate>
            ) {
                if(response.isSuccessful){
                    Toast.makeText(applicationContext, "Content has been deleted", Toast.LENGTH_LONG).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {

            }

        })
    }
}
package com.example.uas_ppk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.uas_ppk.databinding.ActivityDetailContentBinding
import com.example.uas_ppk.databinding.FragmentDataContentBinding
import com.example.uas_ppk.shared_preferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailContentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailContentBinding
    private var b:Bundle? = null
    private val listContent = ArrayList<ContentData>()
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager(this)
        b = intent.extras
        val id = b?.getString("id")

        id?.let { getDataDetail(it) }

        binding.btnDelete.setOnClickListener{
            id?.let { it1 -> deleteData(it1) }
        }

        binding.btnEdit.setOnClickListener{
            startActivity(Intent(this,FormEditActivity::class.java).apply{
                putExtra("id",id)
            })
        }

        binding.btnUpload.setOnClickListener {
            startActivity(Intent(this,FormUploadActivity::class.java).apply {
                putExtra("id",id)
            })
        }

    }


    override fun onRestart() {
        super.onRestart()
        this.recreate()
    }

    fun getDataDetail(id:String){
        val token_auth = "Bearer ${prefManager.getToken()}"
        RClient.instance.getData(token_auth,id).enqueue(object : Callback<ResponseDataContent>{
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

                        Glide.with(applicationContext)
                            .load("${RClient.BASE_URL+"uploads/" + listContent[0].image}")
                            .into(binding.profileImage)

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
        val token_auth = "Bearer ${prefManager.getToken()}"
        RClient.instance.deleteData(token_auth,id).enqueue(object : Callback<ResponseCreate>{
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
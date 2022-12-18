package com.example.uas_ppk

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import com.example.uas_ppk.databinding.ActivityFormUploadBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FormUploadActivity : AppCompatActivity(),UploadRequestBody.UploadCallback {
    private lateinit var binding : ActivityFormUploadBinding
    private var b:Bundle? = null

    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        b = intent.extras
        val id = b?.getString("id")

        binding.btnSelectImage.setOnClickListener{
            getImage()
        }

        binding.btnSave.setOnClickListener {
            id?.let { it1 -> doUploadImage(it1) }
        }

    }

    private fun doUploadImage(id:String) {
        if(selectedImageUri == null){
            Toast.makeText(applicationContext,"Choose an Image", Toast.LENGTH_LONG).show()
            return
        }

        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!,"r",null)?:return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir,contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        binding.progressBar.progress = 0
        val foto = UploadRequestBody(file,"image",this)

        RClient.instance.uploadImage(
            id,
            MultipartBody.Part.createFormData("foto",file.name,foto),
            RequestBody.create(MediaType.parse("multipart/form-data"),"PUT")
        ).enqueue(object : Callback<ResponseCreate>{
            override fun onResponse(
                call: Call<ResponseCreate>,
                response: Response<ResponseCreate>
            ) {

                if(response.isSuccessful){
                    Toast.makeText(applicationContext,"${response.body()?.pesan}",
                        Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Log.e(TAG, "onResponse: ${response.errorBody()!!.charStream().readText()}", )
                }
                binding.progressBar.progress = 100
            }

            override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {

            }

        })
    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == 100){
            selectedImageUri = data?.data
            binding.imgFotoView.setImageURI(selectedImageUri)
        }
    }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if(returnCursor != null){
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }

}


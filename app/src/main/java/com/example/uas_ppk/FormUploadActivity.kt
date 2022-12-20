package com.example.uas_ppk

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
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
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FormUploadActivity : AppCompatActivity(),UploadRequestBody.UploadCallback {
    private lateinit var binding : ActivityFormUploadBinding
    private var b:Bundle? = null
    private lateinit var currentPhotoPath: String
    private lateinit var photoFile:File

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

        binding.btnTakeCamera.setOnClickListener{
            takePictureCamera()
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1
    private fun takePictureCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                photoFile = createImageFile()
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        applicationContext,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
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

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val uri = FileProvider.getUriForFile(applicationContext,"com.example.android.fileprovider"
                ,photoFile)
            selectedImageUri = uri
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

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

}


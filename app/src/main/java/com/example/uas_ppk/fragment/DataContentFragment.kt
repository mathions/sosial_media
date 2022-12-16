package com.example.uas_ppk.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_ppk.*
import com.example.uas_ppk.databinding.FragmentDataContentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DataContentFragment : Fragment() {
    private var _binding: FragmentDataContentBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val listContent = ArrayList<ContentData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDataContentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

        getDataContent()
    }

    override fun onStart() {
        super.onStart()
        getDataContent()
    }

    fun getDataContent(){
        binding.rvData.setHasFixedSize(true)
        binding.rvData.layoutManager=LinearLayoutManager(context)

        val bundle = arguments
        val cari = bundle?.getString("cari")

        binding.progressBar.visibility
        RClient.instance.getData(cari).enqueue(object:  Callback<ResponseDataContent>{
            override fun onResponse(
                call: Call<ResponseDataContent>,
                response: Response<ResponseDataContent>
            ) {
                if(response.isSuccessful){
                    listContent.clear()
                    response.body()?.let { listContent.addAll(it.data) }

                    val adapter = ContentAdapter(listContent)
                    binding.rvData.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.progressBar.isVisible = false

                }
            }

            override fun onFailure(call: Call<ResponseDataContent>, t: Throwable) {
//                TODO("Not yet implemented")
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.uas_ppk.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_ppk.*
import com.example.uas_ppk.databinding.FragmentDataProdukBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DataProdukFragment : Fragment() {
    private var _binding: FragmentDataProdukBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val listProduk = ArrayList<ProdukData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDataProdukBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

        getDataProduk()
    }

    override fun onStart() {
        super.onStart()
        getDataProduk()
    }

    fun getDataProduk(){
        binding.rvData.setHasFixedSize(true)
        binding.rvData.layoutManager=LinearLayoutManager(context)

        val bundle = arguments
        val cari = bundle?.getString("cari")

        binding.progressBar.visibility
        RClient.instance.getData(cari).enqueue(object:  Callback<ResponseDataProduk>{
            override fun onResponse(
                call: Call<ResponseDataProduk>,
                response: Response<ResponseDataProduk>
            ) {
                if(response.isSuccessful){
                    listProduk.clear()
                    response.body()?.let { listProduk.addAll(it.data) }

                    val adapter = ProdukAdapter(listProduk)
                    binding.rvData.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.progressBar.isVisible = false

                }
            }

            override fun onFailure(call: Call<ResponseDataProduk>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
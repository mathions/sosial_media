package com.example.uas_ppk

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uas_ppk.databinding.ListDataContentBinding
import com.example.uas_ppk.shared_preferences.PrefManager

class ContentAdapter(
    private val listContent:ArrayList<ContentData>,
    private val context: Context
    ):
    RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

    inner class ContentViewHolder(item:ListDataContentBinding):RecyclerView.ViewHolder(item.root){
        private val binding = item
        fun bind(contentData: ContentData){
            with(binding){
                txtUsername.text = contentData.username
                txtCaption.text = contentData.caption

                Glide.with(context)
                    .load("${RClient.BASE_URL+"profile/" + contentData.username}")
                    .into(binding.profileImage)

                Glide.with(context)
                    .load("${RClient.BASE_URL+"uploads/" + contentData.image}")
                    .into(binding.image)

                cvData.setOnClickListener{
                    var i = Intent(context,DetailContentActivity::class.java).apply {
                        putExtra("id", contentData.id)
                    }
                    context.startActivity(i)
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder(ListDataContentBinding.inflate(LayoutInflater.from(parent.context),
        parent,false
        ))
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(listContent[position])
    }

    override fun getItemCount(): Int = listContent.size

}
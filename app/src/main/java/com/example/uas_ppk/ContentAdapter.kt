package com.example.uas_ppk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_ppk.databinding.ListDataContentBinding

class ContentAdapter( private val listContent:ArrayList<ContentData>):
    RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

    inner class ContentViewHolder(item:ListDataContentBinding):RecyclerView.ViewHolder(item.root){
        private val binding = item
        fun bind(contentData: ContentData){
            with(binding){
                txtUsername.text = contentData.username
                txtImage.text = contentData.image
                txtCaption.text = contentData.caption
                txtDate.text = contentData.date
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
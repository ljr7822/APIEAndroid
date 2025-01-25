package com.example.xiaoxun.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xiaoxun.R

class TemplateAdapter : RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder>() {

    private val templates = mutableListOf<Template>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_template, parent, false)
        return TemplateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        val template = templates[position]
        holder.bind(template)
    }

    override fun getItemCount(): Int = templates.size

    fun submitList(newTemplates: List<Template>) {
        templates.clear()
        templates.addAll(newTemplates)
        notifyDataSetChanged()
    }

    class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

        fun bind(template: Template) {
            nameTextView.text = template.name
            contentTextView.text = template.content
        }
    }
}
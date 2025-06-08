package com.gauravbajaj.newsapp.ui.languages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Language

class LanguageAdapter : ListAdapter<Language, LanguageAdapter.LanguageViewHolder>(LanguageDiffCallback()) {

    private val selectedLanguages = mutableSetOf<String>()
    var onSelectionChanged: ((List<String>) -> Unit)? = null
    var onMaxSelectionReached: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setSelectedLanguages(codes: List<String>) {
        selectedLanguages.clear()
        selectedLanguages.addAll(codes.take(2))
        notifyDataSetChanged()
    }

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLanguageName: TextView = itemView.findViewById(R.id.tvLanguageName)
        private val tvLanguageNativeName: TextView = itemView.findViewById(R.id.tvLanguageNativeName)
        private val tvLanguageCode: TextView = itemView.findViewById(R.id.tvLanguageCode)
        private val ivSelected: ImageView = itemView.findViewById(R.id.ivSelected)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val language = getItem(adapterPosition)
                    if (selectedLanguages.contains(language.code)) {
                        selectedLanguages.remove(language.code)
                    } else {
                        if (selectedLanguages.size < 2) {
                            selectedLanguages.add(language.code)
                        } else {
                            onMaxSelectionReached?.invoke()
                        }
                    }
                    notifyItemChanged(adapterPosition)
                    onSelectionChanged?.invoke(selectedLanguages.toList())
                }
            }
        }

        fun bind(language: Language) {
            tvLanguageName.text = language.name
            tvLanguageNativeName.text = language.nativeName
            tvLanguageCode.text = language.code
            ivSelected.visibility = if (selectedLanguages.contains(language.code)) View.VISIBLE else View.GONE
        }
    }
}

class LanguageDiffCallback : DiffUtil.ItemCallback<Language>() {
    override fun areItemsTheSame(oldItem: Language, newItem: Language): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: Language, newItem: Language): Boolean {
        return oldItem == newItem
    }
}

package com.ivangarzab.carbud.overview

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivangarzab.carbud.data.Part
import com.ivangarzab.carbud.databinding.ItemComponentBinding
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Ivan Garza Bermea.
 */
class PartListAdapter(
    private val theme: Resources.Theme,
    private val parts: List<Part>,
    val onItemClicked: (Part) -> Unit,
    val onEditClicked: (Part) -> Unit
) : RecyclerView.Adapter<PartListAdapter.PartListViewHolder>() {

    inner class PartListViewHolder(val binding: ItemComponentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartListViewHolder {
        ItemComponentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).also {
            return PartListViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: PartListViewHolder, position: Int) {
        with(holder) {
            with(parts[position]) {
                binding.componentItemName.text = this.name
                val today = Calendar.getInstance().timeInMillis
                binding.componentItemContentText.text = when (
                    this.dueDate.timeInMillis > today
                ) {
                    true -> {
                        val typedValue = TypedValue()
                        theme.resolveAttribute(android.R.attr.textColor, typedValue, true)
                        Log.d("TAG", "${typedValue.data}")
                        binding.componentItemContentText.setTextColor(typedValue.data) // TODO: Use ?android:textColor instead
                        binding.componentItemContentText.setTypeface(null, Typeface.NORMAL)
                        (this.dueDate.timeInMillis - today).let { timeLeftInMillis ->
                            "${TimeUnit.MILLISECONDS.toDays(timeLeftInMillis)} days"
                        }
                    }
                    false -> {
                        binding.componentItemContentText.setTextColor(Color.RED)
                        binding.componentItemContentText.setTypeface(null, Typeface.BOLD)
                        "DUE"
                    }
                }
                binding.root.setOnClickListener { onItemClicked(this) }
                binding.componentItemContentImage.setOnClickListener { onEditClicked(this) }
            }
        }
    }

    override fun getItemCount(): Int = parts.size


}
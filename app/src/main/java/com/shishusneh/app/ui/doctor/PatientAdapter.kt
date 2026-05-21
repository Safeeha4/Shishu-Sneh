package com.shishusneh.app.ui.doctor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.shishusneh.app.R
import com.shishusneh.app.data.local.entities.BabyProfile
import com.shishusneh.app.databinding.ItemPatientBinding
import java.util.concurrent.TimeUnit

class PatientAdapter(private val onPatientClick: (BabyProfile) -> Unit) :
    ListAdapter<BabyProfile, PatientAdapter.PatientViewHolder>(PatientDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = ItemPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PatientViewHolder(private val binding: ItemPatientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(baby: BabyProfile) {
            binding.tvName.text = baby.name
            binding.tvAge.text = calculateAge(baby.dateOfBirth)
            
            if (!baby.photoUri.isNullOrEmpty()) {
                binding.ivPatientPhoto.load(baby.photoUri) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                    error(R.drawable.logo_intern)
                }
            } else {
                binding.ivPatientPhoto.setImageResource(R.drawable.logo_intern)
            }

            binding.root.setOnClickListener { onPatientClick(baby) }
        }

        private fun calculateAge(dob: java.util.Date): String {
            val diff = java.util.Date().time - dob.time
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            val weeks = days / 7
            return if (weeks < 10) "$weeks weeks old" else "${weeks / 4} months old"
        }
    }

    class PatientDiffCallback : DiffUtil.ItemCallback<BabyProfile>() {
        override fun areItemsTheSame(oldItem: BabyProfile, newItem: BabyProfile): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BabyProfile, newItem: BabyProfile): Boolean {
            return oldItem == newItem
        }
    }
}

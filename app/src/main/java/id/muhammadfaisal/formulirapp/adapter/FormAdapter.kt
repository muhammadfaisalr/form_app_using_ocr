package id.muhammadfaisal.formulirapp.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.muhammadfaisal.formulirapp.R
import id.muhammadfaisal.formulirapp.activity.FormActivity
import id.muhammadfaisal.formulirapp.database.entity.FormEntity
import id.muhammadfaisal.formulirapp.databinding.ItemFormBinding
import id.muhammadfaisal.formulirapp.helper.GeneralHelper
import id.muhammadfaisal.formulirapp.helper.ViewHelper
import id.muhammadfaisal.formulirapp.utils.Constant

class FormAdapter(private val context: Context, private val formEntities: List<FormEntity>) : RecyclerView.Adapter<FormAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val binding = ItemFormBinding.bind(itemView)

        private lateinit var formEntity: FormEntity
        private lateinit var context: Context

        fun bind(context: Context, formEntity: FormEntity) {
            this.formEntity = formEntity
            this.context = context

            this.binding.apply {
                this.textName.text = formEntity.fullName
                this.textPhone.text = "+62${formEntity.phoneNumber}"

            if (formEntity.status == Constant.Status.LOCAL) {
                    this.imageStatus.setImageResource(R.drawable.ic_outline_cloud_off_24)
                } else {
                    this.imageStatus.setImageResource(R.drawable.ic_outline_cloud_done_24)
                }
            }

            ViewHelper.makeClickable(this, this.itemView)
        }

        override fun onClick(p0: View?) {
            val bundle = Bundle()
            bundle.putLong(Constant.Key.FORM_ID, this.formEntity.id)
            bundle.putInt(Constant.Key.MODE, Constant.Mode.VIEW)

            GeneralHelper.move(this.context, FormActivity::class.java, bundle, false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_form, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.context, this.formEntities[position])
    }

    override fun getItemCount(): Int {
        return this.formEntities.size
    }
}
package com.example.neostore.storage
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.neostore.R
import com.example.neostore.storage.AddressInfo
import kotlinx.android.synthetic.main.address_list_item.view.*


class AdapterFile(var data:MutableList<AddressInfo>, var click : cartInterface): RecyclerView.Adapter<AdapterFile.ViewHolder>() {
    private var lastChecked: RadioButton? = null
    private var lastCheckedPos = 0
    lateinit var context: Context
    var mlisterner = click


    /*  interface cartInterface{
          fun onClick(position: Int, item: AddressInfo)
          fun onClose(data:  MutableList<AddressInfo>)
      }*/
    /*  fun click(listerner:cartInterface){
          mlisterner = listerner
      }*/
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val radioButton = itemView.addressRadioButton
        //            var username= itemView.findViewById<TextView>(R.id.UserName)
        var fulladdress= itemView.findViewById<TextView>(R.id.fullAddress)
        var imageview= itemView.findViewById<ImageView>(R.id.close)
        /*val close = itemView.close*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterFile.ViewHolder {

        var itemView= LayoutInflater.from(parent.context).
        inflate(R.layout.address_list_item,parent,false)
        context = parent.context
        return  ViewHolder(itemView)
        /* var itemView = LayoutInflater.from(parent.context).inflate(R.layout.address_list_item,parent,false)
         context = parent.context
         return ViewHolder(itemView)*/

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = data[position]
        holder.fulladdress.setText(item.address)
        holder.imageview.setOnClickListener(){
            /* mlisterner.onClick(position,item = item)*/
            mlisterner.onClose(item,position)

        }
        /* holder.imageview.setOnClickListener {
             deleteAddress(position)
         }*/



        if (position == 0) {
            holder.radioButton.isChecked = true

            lastChecked = holder.radioButton
            lastCheckedPos = 0

            val item = data[lastCheckedPos]
            mlisterner.onClick(lastCheckedPos, item)
        }
        holder.radioButton.setOnClickListener() {
            val pos = holder.adapterPosition
            if (holder.radioButton.isChecked) {
                if (lastChecked != null) {
                    lastChecked!!.isChecked = false
                }
                lastChecked = holder.radioButton
                lastCheckedPos = holder.adapterPosition
                val item = data[lastCheckedPos]
                mlisterner.onClick(lastCheckedPos, item)

                Toast.makeText(context, "$lastCheckedPos and $item", Toast.LENGTH_LONG).show()
            } else {
                lastChecked = null
            }
        }
    }





    override fun getItemCount(): Int {
        return data.size
    }

    public fun addItems(data:AddressInfo){
        this.data.add(data)
        notifyDataSetChanged()
    }
    interface cartInterface{
        fun onClick(position: Int, item: AddressInfo)
        fun onClose(data: AddressInfo,position: Int)


    }

    fun deleteAddress(position: Int) {
        val item = data[position]
        (data as MutableList)
        /* notifyItemChanged(position)*/
        /* deleteDao.deleteHistory(data)*/

    }
    /* fun click(listerner:cartInterface){
         mlisterner = listerner
     }*/


}
package com.example.neostore.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.neostore.R
import kotlinx.android.synthetic.main.address_list_item.view.*
import android.widget.Toast
import com.example.neostore.storage.SharedPreferenceManager


class AddressListAdapter(val addressList: MutableList<String>) : RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {

    private var lastChecked: RadioButton? = null
    private var lastCheckedPos = 0
    lateinit var context:Context
    lateinit var mlisterner : cartInterface
    interface cartInterface{
        fun onClick(position: Int, item: String)
        fun onClose(addressList: MutableList<String>)

    }
    fun setOnItemClick(listerner:cartInterface){
        mlisterner = listerner
    }
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val fullAddress:TextView = itemView.fullAddress
        val radioButton = itemView.addressRadioButton
        val name = itemView.UserName
        val close = itemView.close
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressListAdapter.ViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.address_list_item,parent,false)
        context = parent.context
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddressListAdapter.ViewHolder, position: Int) {
        val itemPosition = addressList[position]
        holder.fullAddress.text = itemPosition

        val firstName = SharedPreferenceManager.getInstance(context).data.first_name
        val lastName = SharedPreferenceManager.getInstance(context).data.last_name

        holder.name.text = "$firstName  $lastName"

        holder.close.setOnClickListener(){
            addressList.removeAt(position)
            mlisterner.onClose(addressList)

        }
        if(position == 0)
        {
            holder.radioButton.isChecked = true

            lastChecked = holder.radioButton
            lastCheckedPos = 0

            val item = addressList[lastCheckedPos]
            mlisterner.onClick(lastCheckedPos,item)
        }
        holder.radioButton.setOnClickListener(){
            val pos = holder.adapterPosition
            if(holder.radioButton.isChecked){
                if(lastChecked != null){
                    lastChecked!!.isChecked = false
                }
                lastChecked = holder.radioButton
                lastCheckedPos = holder.adapterPosition
                val item = addressList[lastCheckedPos]
                mlisterner.onClick(lastCheckedPos,item)

                Toast.makeText(context,"$lastCheckedPos and $item",Toast.LENGTH_LONG).show()
            }
            else{
                lastChecked=null
            }
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }
    fun notifyChanged(){
        notifyDataSetChanged()
    }
}

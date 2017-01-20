package ischool.noosphere.qpointer.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ischool.noosphere.qpointer.R
import kotlinx.android.synthetic.main.device_item.view.*

class DevicesAdapter(objects: List<Device>, listener: OnDeviceSelected) : RecyclerView.Adapter<DevicesAdapter.ViewHolder>() {

    private val items: List<Device>
    private val listener: OnDeviceSelected

    init {
        this.items = objects
        this.listener = listener
    }

    interface OnDeviceSelected  {
        fun deviceSelected(deviceAddress: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return ViewHolder(layoutInflater.inflate(R.layout.device_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = items[position]

        holder?.name?.text = item.name
        holder?.address?.text = item.address

        holder?.container?.setOnClickListener {
            listener.deviceSelected(item.address)
        }
    }

    override fun getItemCount(): Int {
       return items.count()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container = view.container
        val name = view.device_name
        val address = view.device_address
    }
}
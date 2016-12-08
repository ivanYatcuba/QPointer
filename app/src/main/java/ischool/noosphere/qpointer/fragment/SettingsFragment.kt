package ischool.noosphere.qpointer.fragment

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ischool.noosphere.qpointer.R
import ischool.noosphere.qpointer.adapter.Device
import ischool.noosphere.qpointer.adapter.DevicesAdapter
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment : BaseQPointerFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_settings
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scan_devices.setOnClickListener {
            getMainPresenter().startScan()
        }

        with (device_list) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = buildDeviceAdapter(ArrayList(getMainPresenter().getDevices()))
        }
    }

    override fun onResume() {
        super.onResume()
        // Register for broadcasts when a device is discovered
        activity.registerReceiver(mReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        // Register for broadcasts when discovery has finished
        activity.registerReceiver(mReceiver, IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
    }

    override fun onPause() {
        super.onPause()
        activity.unregisterReceiver(mReceiver)
    }

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private val mReceiver = object : BroadcastReceiver() {

        var listOfDevices : MutableList<BluetoothDevice> = LinkedList()

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Get the BluetoothDevice object from the Intent
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                listOfDevices.add(device)
            } // When discovery is finished, change the Activity title
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                device_list.swapAdapter(buildDeviceAdapter(listOfDevices), true)
                listOfDevices = LinkedList()
            }
        }
    }

    private fun buildDeviceAdapter(devices: MutableList<BluetoothDevice>) : DevicesAdapter {
        return DevicesAdapter(devices.map { d -> Device(d.name!!, d.address, d.bondState == BluetoothDevice.BOND_BONDED) },
                object : DevicesAdapter.OnDeviceSelected { override fun deviceSelected(deviceAddress: String) {
                    getMainPresenter().connect(deviceAddress)
                } })
    }
}
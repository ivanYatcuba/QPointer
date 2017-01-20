package ischool.noosphere.qpointer.presenter

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.util.Log
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ischool.noosphere.qpointer.view.MainView
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue


@InjectViewState
class MainPresenter(context: Context) : MvpPresenter<MainView>() {

    val bt : BluetoothSPP = BluetoothSPP(context)
    val executor : BtCommandExecutor

    class BtCommandExecutor(bt : BluetoothSPP) {

        val bt : BluetoothSPP
        val commands : Queue<String>

        init {
            this.bt = bt
            this.commands = ConcurrentLinkedQueue()
        }

        fun addCommand(data: String) {
            commands.add(data)
            if(commands.size == 1) {
                executeNext()
            }
        }

        fun executeNext() {
            if(commands.size > 0) {
                bt.send(commands.peek(), true)
            }
        }

        fun commandExecuted(executed: String) {
            Log.d("BT", executed)
            commands.poll()
            executeNext()
        }

    }

    init {

        executor = BtCommandExecutor(bt)

        bt.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener{

            override fun onDeviceDisconnected() {
                viewState.notifyDeviceDisconnected()
            }

            override fun onDeviceConnected(name: String?, address: String?) {
                viewState.notifyDeviceConnected(address!!)
            }

            override fun onDeviceConnectionFailed() {
                viewState.notifyDeviceDisconnected()
            }

        })

        bt.setOnDataReceivedListener { bytes, s -> executor.commandExecuted(s) }

    }

    fun connect(data: Intent) {
        bt.connect(data)
        viewState.showConnectingToDevice(data.extras.getString(BluetoothState.EXTRA_DEVICE_ADDRESS))
    }

    fun connect(address: String) {
        bt.connect(address)
        viewState.showConnectingToDevice(address)
    }

    fun sendData(data: String) {
        if(bt.connectedDeviceName != null) {
            executor.addCommand(data)
        }
    }

    fun sendData(data: List<String>) {
        if(bt.connectedDeviceName != null) {
            for(command in data) {
                executor.addCommand(command)
            }
        }
    }

    fun setUpService() {
        bt.setupService()
        bt.startService(BluetoothState.DEVICE_OTHER)
    }

    fun getDevices() : Set<BluetoothDevice> {
        return bt.bluetoothAdapter.bondedDevices
    }

    fun startScan() {
        bt.startDiscovery()
    }

    fun isBluetoothEnabled() : Boolean {
        return bt.isBluetoothEnabled
    }

    fun isServiceAvailable() : Boolean {
        return bt.isServiceAvailable
    }


}
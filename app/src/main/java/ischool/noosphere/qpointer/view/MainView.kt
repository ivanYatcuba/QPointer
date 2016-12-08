package ischool.noosphere.qpointer.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {

    fun notifyDeviceConnected(deviceAddress: String)

    fun notifyDeviceDisconnected()

    fun showConnectingToDevice(deviceName : String)

    fun hideConnectingToDevice()

}
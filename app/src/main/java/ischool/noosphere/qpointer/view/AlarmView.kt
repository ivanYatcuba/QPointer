package ischool.noosphere.qpointer.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ischool.noosphere.sensebridge.model.Alarm

@StateStrategyType(AddToEndSingleStrategy::class)
interface AlarmView : MvpView {

    fun setAlarmListData(alarmList: List<Alarm>)
}
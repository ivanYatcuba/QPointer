package ischool.noosphere.sensebridge.fragment

import android.app.Fragment
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import ischool.noosphere.qpointer.R
import ischool.noosphere.qpointer.fragment.BaseQPointerFragment
import ischool.noosphere.qpointer.presenter.AlarmPresenter
import ischool.noosphere.qpointer.protocol.QPointerProtocol
import ischool.noosphere.qpointer.view.AlarmView
import ischool.noosphere.sensebridge.adapter.AlarmAdapter
import ischool.noosphere.sensebridge.model.Alarm
import kotlinx.android.synthetic.main.fragment_alarm.*


class AlarmFragment : BaseQPointerFragment(), AlarmView {

    private var mIsStateSaved: Boolean = false
    private val mMvpDelegate: MvpDelegate<out AlarmView> = MvpDelegate(this)

    @InjectPresenter(type = PresenterType.WEAK)
    lateinit var alarmPresenter: AlarmPresenter

    @ProvidePresenterTag(presenterClass = AlarmPresenter::class, type = PresenterType.WEAK)
    fun provideAlarmPresenterTag(): String = "Alarm"

    @ProvidePresenter(type = PresenterType.WEAK)
    fun provideAlarmPresenter(): AlarmPresenter = AlarmPresenter(activity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMvpDelegate.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        alarms.layoutManager = linearLayoutManager
        setAlarmListData(alarmPresenter.getAlarmList())

        add_alarm.setOnClickListener {
            val newFragment = TimePickerFragment(object : TimePickerFragment.OnAlarmCreated {

                override fun onAlarmCreated(alarm: Alarm) {
                    alarmPresenter.addAlarm(alarm)
                }

            })
            newFragment.show(activity.fragmentManager, "timePicker")
        }

        val simpleItemTouchCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                val alarm = (alarms.adapter as AlarmAdapter).getItem((viewHolder as AlarmAdapter.AlarmViewHolder).adapterPosition)
                alarmPresenter.removeAlarm(alarm)
            }

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
              return false
            }
        }

        val itemTouchHelper =  ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(alarms)
    }

    override fun setAlarmListData(alarmList: List<Alarm>) {
        alarms.adapter = AlarmAdapter(alarmList, object : AlarmAdapter.OnAlarmActionListener {

            override fun alarmTouched(alarm: Alarm) {
                val newFragment = TimePickerFragment(object : TimePickerFragment.OnAlarmCreated {

                    override fun onAlarmCreated(alarmLocal: Alarm) {
                        alarmPresenter.removeAlarm(alarm)
                        alarmPresenter.addAlarm(alarmLocal)

                        if(alarmLocal.enabled) {
                            getMainPresenter().sendData(QPointerProtocol.setAlarm(alarmLocal))
                        }
                    }

                })
                newFragment.alarm = alarm
                newFragment.show(activity.fragmentManager, "timePicker")
            }

            override fun alarmSwitched(alarm: Alarm, isChecked: Boolean) {
                alarmPresenter.checkAlarm(alarm, isChecked)
                if(isChecked) {
                    getMainPresenter().sendData(QPointerProtocol.setAlarm(alarm))
                } else {
                    getMainPresenter().sendData(QPointerProtocol.unsetAlarm())
                }

            }

        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_alarm
    }

    override fun onStart() {
        super.onStart()
        mIsStateSaved = false
        mMvpDelegate.onAttach()
    }

    override fun onResume() {
        super.onResume()
        mIsStateSaved = false
        mMvpDelegate.onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mIsStateSaved = true
        mMvpDelegate.onSaveInstanceState(outState)
        mMvpDelegate.onDetach()
    }

    override fun onStop() {
        super.onStop()
        mMvpDelegate.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mMvpDelegate.onDetach()
        mMvpDelegate.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (mIsStateSaved) {
            mIsStateSaved = false
            return
        }

        var anyParentIsRemoving = false

        if (Build.VERSION.SDK_INT >= 17) {
            var parent: Fragment? = parentFragment
            while (!anyParentIsRemoving && parent != null) {
                anyParentIsRemoving = parent.isRemoving
                parent = parent.parentFragment
            }
        }

        if (isRemoving || anyParentIsRemoving || activity.isFinishing) {
            mMvpDelegate.onDestroy()
        }
    }

}
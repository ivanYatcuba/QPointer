package ischool.noosphere.qpointer.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ischool.noosphere.qpointer.R
import ischool.noosphere.qpointer.protocol.QPointerProtocol
import ischool.noosphere.qpointer.widget.TimePicker
import kotlinx.android.synthetic.main.fragment_clock.*
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*

class ClockFragment : BaseQPointerFragment() {

    override fun getLayoutId(): Int {
       return R.layout.fragment_clock
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        syncTime()

        set_time.setOnClickListener {
            getMainPresenter().sendData(QPointerProtocol.setHour(time_picker.currentHour))
            getMainPresenter().sendData(QPointerProtocol.setMinute(time_picker.currentMinute))
            getMainPresenter().sendData(QPointerProtocol.setSecond(time_picker.currentSeconds))
        }

        sync_time.setOnClickListener {
            syncTime()
        }
    }

    private fun syncTime() {
        val now = LocalDateTime.now()

        time_picker.currentHour = now.hourOfDay
        time_picker.currentMinute = now.minuteOfHour
        time_picker.setCurrentSecond(now.secondOfMinute)
    }
}
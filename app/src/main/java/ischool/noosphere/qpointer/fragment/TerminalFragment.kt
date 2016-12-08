package ischool.noosphere.qpointer.fragment

import android.os.Bundle
import android.view.View
import ischool.noosphere.qpointer.R
import kotlinx.android.synthetic.main.fragment_terminal.*
import android.text.InputFilter



class TerminalFragment : BaseQPointerFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_terminal
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data_to_send.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        send_data.setOnClickListener {
            getMainPresenter().sendData(data_to_send.text.toString())
        }
    }
}
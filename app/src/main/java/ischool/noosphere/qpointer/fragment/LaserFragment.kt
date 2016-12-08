package ischool.noosphere.qpointer.fragment

import android.os.Bundle
import android.view.View
import ischool.noosphere.qpointer.R
import ischool.noosphere.qpointer.protocol.QPointerProtocol
import kotlinx.android.synthetic.main.fragment_laser.*

class LaserFragment : BaseQPointerFragment() {

    override fun getLayoutId(): Int {
       return R.layout.fragment_laser
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        right_laser.setOnCheckedChangeListener { compoundButton, b ->  getMainPresenter().sendData(QPointerProtocol.setLasers(left_laser.isChecked, b)) }
        left_laser.setOnCheckedChangeListener { compoundButton, b -> getMainPresenter().sendData(QPointerProtocol.setLasers(b, right_laser.isChecked)) }
    }
}
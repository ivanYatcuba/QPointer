package ischool.noosphere.qpointer.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import ischool.noosphere.qpointer.R
import ischool.noosphere.qpointer.protocol.QPointerProtocol
import kotlinx.android.synthetic.main.fragment_color.*


class ColorFragment : BaseQPointerFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_color;
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.findViewById(R.id.set_color)?.setOnClickListener {
            val data = Integer.toHexString(color_picker_view?.selectedColor!!)
            getMainPresenter().sendData(QPointerProtocol.setColor(data.substring(2, data.length))) // removing alpha
        }

        fix(color_picker_view!!)
        fix(lightness_slider!!)
    }

    //Fix for color picker in fragment see lib repo for details
    private fun fix(view: View) {
        val vto = view.viewTreeObserver
        vto?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.onWindowFocusChanged(true)
                val obs = view.getViewTreeObserver()
                obs?.removeGlobalOnLayoutListener(this)
            }
        })
    }
}
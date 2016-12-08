package ischool.noosphere.qpointer.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ischool.noosphere.qpointer.MainActivity
import ischool.noosphere.qpointer.R
import ischool.noosphere.qpointer.presenter.MainPresenter

open abstract class BaseQPointerFragment : Fragment() {

    fun getMainPresenter() : MainPresenter {
        return (activity as MainActivity).mainPresenter
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater?.inflate(getLayoutId(), null)!!
    }

    protected abstract fun getLayoutId() : Int

}
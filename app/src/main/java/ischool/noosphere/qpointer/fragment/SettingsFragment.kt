package ischool.noosphere.qpointer.fragment

import android.os.Bundle
import android.view.View
import ischool.noosphere.qpointer.MainActivity
import ischool.noosphere.qpointer.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseQPointerFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_settings
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val theme =  MainActivity.MainActivity.Theme.valueOf((activity as MainActivity).getPreferences().getString(MainActivity.APP_THEME, MainActivity.MainActivity.Theme.LIGHT.name))

        when(theme) {
            MainActivity.MainActivity.Theme.LIGHT -> selector_light_theme.isChecked = true
            MainActivity.MainActivity.Theme.DARK -> selector_dark_theme.isChecked = true
        }

        selector_light_theme.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                (activity as MainActivity).setTheme(MainActivity.MainActivity.Theme.LIGHT)
                activity.recreate()
            }
        }

        selector_dark_theme.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                (activity as MainActivity).setTheme(MainActivity.MainActivity.Theme.DARK)
                activity.recreate()
            }
        }
    }
}
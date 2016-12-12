package ischool.noosphere.qpointer

import android.app.Activity
import android.app.Fragment
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import app.akexorcist.bluetotohspp.library.BluetoothState
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import ischool.noosphere.qpointer.fragment.*
import ischool.noosphere.qpointer.presenter.MainPresenter
import ischool.noosphere.qpointer.view.MainView
import kotlinx.android.synthetic.main.app_bar_main.*




class MainActivity : MvpAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainView {

    companion object MainActivity {
        val PREFERENCES : String = "QPOINTER_PREFS"
        val DEVICE_ADDRESS : String = "DEVICE_ADDRESS"
        val APP_THEME : String = "APP_THEME"

        enum class Theme { LIGHT, DARK }

        var isLaunched : Boolean = false
    }

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var mainPresenter: MainPresenter

    @ProvidePresenterTag(presenterClass = MainPresenter::class, type = PresenterType.GLOBAL)
    fun provideDialogPresenterTag(): String = "Main"

    @ProvidePresenter(type = PresenterType.GLOBAL)
    fun provideDialogPresenter(): MainPresenter = MainPresenter(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val theme = Theme.valueOf(getPreferences().getString(APP_THEME, Theme.LIGHT.name))

        setTheme(theme)

        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null
        navigationView.menu.getItem(0).isChecked = true
        if(!isLaunched) {
            switchContent(ClockFragment())
            isLaunched = true
        }
    }

    fun setTheme(theme : Theme) {
        when(theme) {
            Theme.LIGHT -> {
                application.setTheme(R.style.AppThemeLight)
                setTheme(R.style.AppThemeLight_NoActionBar)
            }
            Theme.DARK -> {
                application.setTheme(R.style.AppThemeDark)
                setTheme(R.style.AppThemeDark_NoActionBar)
            }
        }
        getPreferences().edit().putString(APP_THEME, theme.name).apply()
    }

    public override fun onStart() {
        super.onStart()
        if (!mainPresenter.isBluetoothEnabled()) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT)
        } else {
            if (!mainPresenter.isServiceAvailable()) {
                mainPresenter.setUpService()
                connectToDefaultDevice()
            }
        }
        registerReceiver(btStateReceiver,  IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(btStateReceiver)
    }

    private fun connectToDefaultDevice() {
        val address = getPreferences().getString(DEVICE_ADDRESS, null)
        if(address != null) {
            mainPresenter.connect(address)
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun switchContent(fragment: Fragment) {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        switchContent( when (id) {
            R.id.nav_clock -> ClockFragment()
            R.id.nav_color -> ColorFragment()
            R.id.nav_laser -> LaserFragment()
            R.id.nav_settings -> SettingsFragment()
            R.id.nav_device -> DeviceFragment()
            R.id.nav_terminal -> TerminalFragment()
            else -> ClockFragment()
        })

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                mainPresenter.connect(data!!)
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
               mainPresenter.setUpService()
            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }
    }

    override fun notifyDeviceConnected(deviceAddress: String) {
        device_connected.setImageDrawable(resources.getDrawable(R.drawable.ic_device_connected))
        hideConnectingToDevice()
        getPreferences().edit().putString(DEVICE_ADDRESS, deviceAddress).apply()
    }

    override fun notifyDeviceDisconnected() {
        device_connected.setImageDrawable(resources.getDrawable(R.drawable.ic_device_disconnected))
        hideConnectingToDevice()
    }

    override fun showConnectingToDevice(deviceName: String) {
        device_connected.visibility = View.GONE
        device_connection_progress.visibility = View.VISIBLE
    }

    override fun hideConnectingToDevice() {
        device_connected.visibility = View.VISIBLE
        device_connection_progress.visibility = View.GONE
    }

    private val btStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action = intent.action

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {
                    connectToDefaultDevice()
                }
            }
        }
    }

    fun getPreferences() : SharedPreferences {
        return this.getSharedPreferences(PREFERENCES, android.content.Context.MODE_PRIVATE)
    }

}

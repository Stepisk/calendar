package com.stepisk.calendar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.stepisk.calendar.database.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_event_list.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var viewModel: EventViewModel? = null
    private var eventAdapter: EventDataAdapter? = null
    private val cal: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataBaseInstance = AppDatabase.getDatabaseInstance(this)
        viewModel = ViewModelProvider.NewInstanceFactory().create(EventViewModel::class.java)
        viewModel?.setInstanceOfDB(dataBaseInstance)

        initViews()
        setListeners()
        observerViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> startActivity(Intent(this, AboutActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        updateCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
    }

    private fun initViews() {
        eventAdapter = EventDataAdapter(this)
        eventList.adapter = eventAdapter
    }

    private fun observerViewModel() {
        viewModel?.eventsList?.observe(this, {
            eventAdapter?.setData(it)
        })
    }

    private fun setListeners() {
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            updateCalendar(year, month, dayOfMonth)
        }
    }

    private fun updateCalendar(year: Int, month: Int, dayOfMonth: Int) {
        cal.set(year, month, dayOfMonth)

        val calStart = Calendar.getInstance()
        val calEnd = Calendar.getInstance()
        calStart.set(year, month, dayOfMonth, 0, 0, 0)
        calEnd.set(year, month, dayOfMonth, 23, 59, 59)

        viewModel?.getBetweenTimes(calStart.timeInMillis, calEnd.timeInMillis)
    }

    fun onAddEvent(@Suppress("UNUSED_PARAMETER") view: View) {
        startActivity(Intent(this, AddEventActivity::class.java).apply {
            putExtra("cal", cal.timeInMillis)
        })
    }
}
package com.stepisk.calendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.stepisk.calendar.database.AppDatabase
import com.stepisk.calendar.database.EventData
import kotlinx.android.synthetic.main.activity_add_event.*
import java.util.*

class AddEventActivity : AppCompatActivity() {

    private var viewModel: EventViewModel? = null
    private val cal: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_topcoat_cancel)
        supportActionBar?.setHomeButtonEnabled(true)

        cal.timeInMillis = intent.getLongExtra("cal", cal.timeInMillis)

        val dataBaseInstance = AppDatabase.getDatabaseInstance(this)
        viewModel = ViewModelProvider.NewInstanceFactory().create(EventViewModel::class.java)
        viewModel?.setInstanceOfDB(dataBaseInstance)

        initViews()
        observerViewModel()
    }

    private fun initViews() {
        eventDate.text = MyDateFormat.sdf.format(Date(cal.timeInMillis))
        eventTime.text = MyDateFormat.stf.format(Date(cal.timeInMillis))

        val eventId = intent.getIntExtra("event_id", 0)
        viewModel?.getEventById(eventId)
    }

    private fun observerViewModel() {
        viewModel?.event?.observe(this, {
            it?.let {
                eventTitle.setText(it.title)
                eventText.setText(it.text)
                cal.timeInMillis = it.timeInMillis
                eventDate.text = MyDateFormat.sdf.format(Date(cal.timeInMillis))
                eventTime.text = MyDateFormat.stf.format(Date(cal.timeInMillis))
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_event_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.event_ok -> {
                val title = eventTitle.text.toString()
                val text = eventText.text.toString()
                val timeInMillis = cal.timeInMillis

                viewModel?.event?.value?.let {
                    it.title = title
                    it.text = text
                    it.timeInMillis = timeInMillis

                    viewModel?.updateEvent(it)

                    this.finish()
                    return true
                }

                val newEvent = EventData(title = title, text = text, timeInMillis = timeInMillis)
                viewModel?.insertEvent(newEvent)

                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onDate(@Suppress("UNUSED_PARAMETER") view: View) {
        val dpd = DatePickerDialog(
            this, { _, year, month, dayOfMonth ->
                cal.set(year, month, dayOfMonth)

                eventDate.text = MyDateFormat.sdf.format(cal.time)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        dpd.show()
    }

    fun onTime(@Suppress("UNUSED_PARAMETER") view: View) {
        val tpd = TimePickerDialog(
            this, { _, hourOfDay, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)

                eventTime.text = MyDateFormat.stf.format(cal.time)
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        )

        tpd.show()
    }
}
package com.stepisk.calendar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.stepisk.calendar.database.AppDatabase
import kotlinx.android.synthetic.main.activity_event_info.*
import java.util.*

class EventInfoActivity : AppCompatActivity() {
    private var viewModel: EventViewModel? = null
    private var eventId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_info)

        supportActionBar?.setHomeButtonEnabled(true)

        eventId = intent.getIntExtra("event_id", 0)

        val dataBaseInstance = AppDatabase.getDatabaseInstance(this)
        viewModel = ViewModelProvider.NewInstanceFactory().create(EventViewModel::class.java)
        viewModel?.setInstanceOfDB(dataBaseInstance)

        initViews()
        observerViewModel()
    }

    override fun onResume() {
        super.onResume()
        eventId?.let { viewModel?.getEventById(it) }
    }

    private fun initViews() {
        eventId?.let { viewModel?.getEventById(it) }
    }

    private fun observerViewModel() {
        viewModel?.event?.observe(this, {
            it?.let {
                val time = MyDateFormat.stf.format(Date(it.timeInMillis))
                val title = "${it.title} ($time)"

                eventTitle.text = title
                eventText.text = it.text
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.event_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (viewModel?.event?.value == null)
            return false
        val event = viewModel?.event?.value!!

        when (item.itemId) {
            R.id.event_edit -> startActivity(Intent(this, AddEventActivity::class.java).apply {
                putExtra("event_id", event.id)
            })

            R.id.event_delete -> {
                viewModel?.deleteEvent(event)
                this.finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
package com.stepisk.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stepisk.calendar.database.AppDatabase
import com.stepisk.calendar.database.EventData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EventViewModel: ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var dataBaseInstance: AppDatabase? = null
    var eventsList = MutableLiveData<List<EventData>>()
    var event = MutableLiveData<EventData?>()

    fun setInstanceOfDB(dataBaseInstance: AppDatabase) {
        this.dataBaseInstance = dataBaseInstance
    }

    fun insertEvent(data: EventData) {
        dataBaseInstance?.eventDao()?.insertEvent(data)
            ?.doOnComplete {  }
            ?.doOnError {  }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()?.let {
                compositeDisposable.add(it)
            }
    }

    fun getAllEvents() {
        dataBaseInstance?.eventDao()?.getAllRecords()
            ?.doOnSuccess {
                if (!it.isNullOrEmpty()) {
                    eventsList.postValue(it)
                } else {
                    eventsList.postValue(listOf())
                }
            }
            ?.doOnError {  }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()?.let {
                compositeDisposable.add(it)
            }
    }

    fun getEventById(id: Int) {
        dataBaseInstance?.eventDao()?.getEventById(id)
            ?.doOnSuccess {
                event.postValue(it)
            }
            ?.doOnError {
                event.postValue(null)
            }
            ?.doOnComplete {  }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()?.let {
                compositeDisposable.add(it)
            }
    }

    fun getBetweenTimes(timeStart: Long, timeEnd: Long) {
        dataBaseInstance?.eventDao()?.getBetweenTimes(timeStart, timeEnd)
            ?.doOnSuccess {
                if (!it.isNullOrEmpty()) {
                    eventsList.postValue(it)
                } else {
                    eventsList.postValue(listOf())
                }
            }
            ?.doOnError {  }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()?.let {
                compositeDisposable.add(it)
            }
    }

    fun deleteEvent(event: EventData) {
        dataBaseInstance?.eventDao()?.deleteEvent(event)
            ?.doOnComplete {
                getAllEvents()
            }
            ?.doOnError {  }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()?.let {
                compositeDisposable.add(it)
            }
    }

    fun updateEvent(event: EventData) {
        dataBaseInstance?.eventDao()?.updateEvent(event)
            ?.doOnComplete {  }
            ?.doOnError {  }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()?.let {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
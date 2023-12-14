package ie.wit.map.main

import android.app.Application
import timber.log.Timber

class MapApp : Application() {

    //lateinit var donationsStore: DonationStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
      //  donationsStore = DonationManager()
        Timber.i("DonationX Application Started")
    }
}
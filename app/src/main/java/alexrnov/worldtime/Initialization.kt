package alexrnov.worldtime

//import alexrnov.worldtime.model.ApplicationComponent
//import alexrnov.worldtime.model.ApplicationModule
//import alexrnov.worldtime.model.DaggerApplicationComponent
import alexrnov.worldtime.model.ApplicationComponent
import alexrnov.worldtime.model.ApplicationModule
import alexrnov.worldtime.model.DaggerApplicationComponent
import android.app.Application




// applicationComponent lives in the Application class to share its lifecycle
class Initialization : Application() {
    // Reference to the application graph that is used across the whole app
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        // Reference to the application graph that is used across the whole app
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this.applicationContext))
                .build()
    }

}
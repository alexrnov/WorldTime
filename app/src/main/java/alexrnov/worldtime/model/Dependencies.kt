package alexrnov.worldtime.model

import alexrnov.worldtime.MainActivity
import alexrnov.worldtime.ui.home.HomeFragment
import alexrnov.worldtime.ui.list.TimeListFragment
import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Singleton

// @Module informs Dagger that this class is a Dagger Module
@Module
class ApplicationModule(private val context: Context) {

    // @Singleton - way to scope types inside a Dagger Module. @Provides tell Dagger how to create instances
    // of the type that this function returns (i.e. Context). Function parameters are the dependencies of this type.
    @Singleton
    @Provides
    fun provideContext(): Context {
        return context
    }
}

// The "subcomponents" attribute in the @Module annotation tells Dagger what
// Subcomponents are children of the Component this module is included in.
@Module(subcomponents = [ActivityComponent::class])
class SubModule

/*
 @Component makes Dagger create a graph of dependencies. Dagger can create a graph of the dependencies
 in your project that it can use to find out where it should get those dependencies when they are needed. To make
 Dagger do this, you need to create an interface and annotate it with @Component. Dagger creates a container as
 you would have done with manual dependency injection. Scope annotations on a @Component interface informs Dagger
 that classes annotated with this annotation (i.e. @Singleton) are bound to the life of the graph and so the same
 instance of that type is provided every time the type is requested. The "modules" attribute in the @Component
 annotation tells Dagger what Modules to include when building the graph. @Singleton - You can use it to annotate
 ApplicationComponent and the objects you want to reuse across the whole application. Because ApplicationComponent
 is created when the app is launched (in the application class), it is destroyed when the app gets destroyed.
 Thus, the unique instance of Repository always remains in memory until the application is destroyed. Including
 SubcomponentsModule, tell ApplicationComponent that ActivityComponent is its subcomponent.
*/
@Singleton
@Component(modules = [ApplicationModule::class, SubModule::class])
interface ApplicationComponent {
    /*
     ApplicationComponent doesn't need to inject MainActivity anymore because that responsibility now belongs to
     ActivityComponent, so you can remove the inject() method from ApplicationComponent. Consumers of ApplicationComponent
     need to know how to create instances of ActivityComponent. The parent component must add a method in its interface
     to let consumers create instances of the subcomponent out of an instance of the parent component: Expose the factory
     that creates instances of ActivityComponent the interface: This function exposes the ActivityComponent Factory
     out of the graph so consumers can use it to obtain new instances of ActivityComponent
    */
    fun activityComponent(): ActivityComponent.Factory
}
/*
 To scope PageViewModel to the lifecycle of MainActivity you need to create a new component (a new subgraph)
 for the main activity flow and a new scope. @Subcomponent annotation informs Dagger this interface is a Dagger
 Subcomponent. Note that you cannot use the @Singleton annotation because it's already been used by the
 parent component and that'd make the object an application singleton (unique instance for the whole app).
 */
@Subcomponent
interface ActivityComponent {
    // You also must define a subcomponent factory inside MainComponent so that ApplicationComponent knows
    // how to create instances of MainComponent. Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    /*
     This tells Dagger that MainActivity requests injection from ActivityComponent so that this
     subcomponent graph needs to satisfy all the dependencies of the fields that MainActivity is injecting
     If you have multiple classes that request injection, you have to specifically declare them all in the
     component with their exact type. MainActivity and PageContentFragment request injection from ActivityComponent.
     */

    fun inject(mainActivity: MainActivity)
    fun inject(timeListFragment: TimeListFragment)
    fun inject(homeFragment: HomeFragment)
}
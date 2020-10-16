package alexrnov.worldtime.ui.list;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

class TimeListObserver implements LifecycleObserver {

    private TimeListViewModel timeListViewModel;

    public TimeListObserver(TimeListViewModel timeListViewModel) {
        this.timeListViewModel = timeListViewModel;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start() {
        timeListViewModel.loadListFromServer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void stop() {
        timeListViewModel = null;
    }

}
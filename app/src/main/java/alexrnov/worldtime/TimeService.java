package alexrnov.worldtime;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import io.reactivex.Observable;

/** Привязанная служба, на основе использования расширенного класса Binder */
public class TimeService extends Service {
  private final IBinder binder = new LocalBinder();
  TimeApiInterface timeApiService = TimeApiClient.getClientWithRx().create(TimeApiInterface.class);

  /*
   * Класс используется для связи с клиентом. Так как служба всегда
   * запускается в том же самом процессе, что и клиент, нам не нужно
   * иметь дело с межпроцессорным взаимодействием.
   */
  public class LocalBinder extends Binder {
    TimeService getService() {
      // Вернуть экземпляр службы клиенту, который может вызвать его public-методы
      return TimeService.this;
    }
  }

  // метод, который может вызвать клиент
  public Observable<Time> getTimeObservable(String path) {
    return timeApiService.getTime(path);
  }

  // invoke when services are created by bindService()
  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  /*
  // invoke when services are created by startService()
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);
    // If we get killed, after returning from here, restart
    return START_STICKY;
  }
  */
}
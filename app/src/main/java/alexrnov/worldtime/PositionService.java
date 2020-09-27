package alexrnov.worldtime;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

import androidx.annotation.Nullable;

/** Привязанная служба, на основе использования расширенного класса Binder */
public class PositionService extends Service {

  private final IBinder binder = new LocalBinder();

  private final Random r = new Random();
  /*
   * Класс используется для связи с клиентом. Так как служба всегда
   * запускается в том же самом процессе, что и клиент, нам не нужно
   * иметь дело с межпроцессорным взаимодействием.
   */
  public class LocalBinder extends Binder {
    PositionService getService() {
      /*
       * Возвращает экземпляр данной службы клиенту, который может
       * вызвать его public-методы
       */
      return PositionService.this;
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  //метод, который может вызвать клиент
  public Integer getRandomNumber() {
    return r.nextInt();
  }
}

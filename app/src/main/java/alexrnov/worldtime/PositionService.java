package alexrnov.worldtime;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

class PositionService extends Service {

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}

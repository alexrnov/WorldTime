package alexrnov.worldtime.ui.sun;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import alexrnov.worldtime.R;

public class SunFragment extends Fragment {

  private SunViewModel sunViewModel;

  //private FusedLocationProviderClient locationClient;
  private LocationObserver locationObserver;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    Log.i("P", "onCreateView() fragment");

    sunViewModel = new ViewModelProvider(this).get(SunViewModel.class);
    View root = inflater.inflate(R.layout.fragment_sun, container, false);
    final TextView textView = root.findViewById(R.id.text_dashboard);
    sunViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
      @Override
      public void onChanged(@Nullable String s) {
        textView.setText(s);
      }
    });

    locationObserver = new LocationObserver(this, getLifecycle());
    getLifecycle().addObserver(locationObserver);

    return root;
  }

  // request location permission handler
  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NotNull String[] permissions, @NotNull int[] grantResults) {
    if (requestCode == 1) { // or permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION))
      if (grantResults.length > 0 //permission granted
              && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Log.i("P", "onRequestPermissionsResult2");
        locationObserver.getLocation();
      }
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

}
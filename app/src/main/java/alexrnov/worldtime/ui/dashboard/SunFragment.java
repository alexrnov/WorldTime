package alexrnov.worldtime.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import alexrnov.worldtime.R;

public class SunFragment extends Fragment {

  private SunViewModel sunViewModel;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    sunViewModel =
            ViewModelProviders.of(this).get(SunViewModel.class);
    View root = inflater.inflate(R.layout.fragment_sun, container, false);
    final TextView textView = root.findViewById(R.id.text_dashboard);
    sunViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
      @Override
      public void onChanged(@Nullable String s) {
        textView.setText(s);
      }
    });
    return root;
  }
}
package alexrnov.worldtime.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import alexrnov.worldtime.R;

public class ListFragment extends Fragment {

  private ListViewModel listViewModel;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    listViewModel = new ViewModelProvider(this).get(ListViewModel.class);
    View root = inflater.inflate(R.layout.fragment_list, container, false);
    final TextView textView = root.findViewById(R.id.text_notifications);
    listViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
      @Override
      public void onChanged(@Nullable String s) {
        textView.setText(s);
      }
    });
    return root;
  }
}
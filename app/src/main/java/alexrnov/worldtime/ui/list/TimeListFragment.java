package alexrnov.worldtime.ui.list;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import alexrnov.worldtime.MainActivity;
import alexrnov.worldtime.R;
import static alexrnov.worldtime.ApplicationUtilsKt.showSnackBar;

public class TimeListFragment extends Fragment {

  // fields that need to be injected by the login graph
  @Inject TimeListViewModel timeListViewModel;

  private TimeListObserver timeListObserver;

  private TimeListAdapter adapter;

  // when using fragments, inject Dagger in the fragment's onAttach() method.
  // In this case, it can be done before or after calling super.onAttach().
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    // obtaining the activity graph from MainActivity and instantiate
    // the @Inject fields with objects from the graph
    MainActivity mainActivity = (MainActivity) getActivity();
    if (mainActivity != null) {
      mainActivity.activityComponent.inject(this);
    }
    // now access PageViewModel here and onCreateView too
    // (shared instance with the Activity and the other Fragment)
  }

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    //timeListViewModel = new ViewModelProvider(this).get(TimeListViewModel.class);
    View root = inflater.inflate(R.layout.fragment_list, container, false);
    RecyclerView recyclerView = root.findViewById(R.id.time_list_recyclerview);
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
    recyclerView.setLayoutManager(layoutManager);

    timeListViewModel.getTimeList().observe(getViewLifecycleOwner(), items -> {
      if (items.component1() != null) {
        adapter = new TimeListAdapter(items.component1());
        recyclerView.setAdapter(adapter);
      } else {
        if (items.component2() != null) {
          showSnackBar(this.requireView(), getString(R.string.error_load_data));
        }
      }
    });

    timeListObserver = new TimeListObserver(timeListViewModel);
    getLifecycle().addObserver(timeListObserver);



    return root;
  }

}
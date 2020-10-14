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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alexrnov.worldtime.R;


public class TimeListFragment extends Fragment {

  private TimeListViewModel timeListViewModel;

  private TimeListObserver timeListObserver;

  private TimeListAdapter adapter;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    timeListViewModel = new ViewModelProvider(this).get(TimeListViewModel.class);
    View root = inflater.inflate(R.layout.fragment_list, container, false);




    RecyclerView recyclerView = root.findViewById(R.id.time_list_recyclerview);
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
    recyclerView.setLayoutManager(layoutManager);

    timeListViewModel.loadListFromServer();

    timeListViewModel.getTimeList().observe(getViewLifecycleOwner(), items -> {
      adapter = new TimeListAdapter(items);
      recyclerView.setAdapter(adapter);
    });


    timeListObserver = new TimeListObserver(timeListViewModel);
    getLifecycle().addObserver(timeListObserver);

    return root;
  }
}
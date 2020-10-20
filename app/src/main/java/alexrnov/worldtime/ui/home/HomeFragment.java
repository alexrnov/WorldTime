package alexrnov.worldtime.ui.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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

import javax.inject.Inject;

import alexrnov.worldtime.MainActivity;
import alexrnov.worldtime.R;
import alexrnov.worldtime.TimeService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {

  @Inject HomeViewModel homeViewModel;

  private TimeService timeService;
  private CompositeDisposable compositeDisposable = new CompositeDisposable();
  boolean bound = false;

  // when using fragments, inject Dagger in the fragment's onAttach() method.
  // In this case, it can be done before or after calling super.onAttach().
  @Override
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

    //homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    View root = inflater.inflate(R.layout.fragment_home, container, false);
    final TextView textView = root.findViewById(R.id.text_home);
    homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
      @Override
      public void onChanged(@Nullable String s) {
        textView.setText(s);
      }
    });
    return root;
  }

  @Override
  public void onStart() {
    super.onStart();
    Intent intent = new Intent(this.getActivity(), TimeService.class);
    //BIND_AUTO_CREATE - параметр привязки: создать службу если она еще не выполняется
    this.getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  public void onStop() {
    super.onStop();

    if (bound) { // открепиться от сервиса
      this.getActivity().unbindService(mConnection);
      bound = false;
    }
  }

  //определяет обратный вызов для связанной службы, передаваемый в bindService()
  private ServiceConnection mConnection = new ServiceConnection() {

    /*
     * Система вызывает этот метод, чтобы выдать объект IBinder, возвращенный
     * методом onBind() службы.
     */
    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
      Log.i("P", "onServiceConnected init");
      //мы получаем связь с TimeService, преобразуем интерфейс IBinder в LocalBinder
      //и получаем экземпляр TimeService
      TimeService.LocalBinder binder = (TimeService.LocalBinder) service;
      timeService = binder.getService();
      bound = true;
      //вызывается public-метод связанной службы. Однако если c этим вызовом
      //было что-то, что могло привести к зависанию(длительной работы метода),
      //тогда этот запрос должен происходить в отдельном потоке, чтобы избежать
      //снижения производительности активити-класса
      Disposable subscribe = timeService.getTimeObservable("America/Whitehorse.json")
              .subscribeOn(Schedulers.newThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(time -> {
                Log.i("P", "service time = " + time.getDateTime());
              }, error -> {
                Log.i("P", "service error");
              });

      compositeDisposable.add(subscribe);
    }

    /*
     * Система Android вызывает этот метод в случае непредвиденной потери
     * подключения к службе, например при сбое в работе службы или в случае
     * ее завершения. Этот метод не вызывается, когда клиент отменяет привязку
     */
    @Override
    public void onServiceDisconnected(ComponentName arg0) {
      bound = false;
    }
  };






}
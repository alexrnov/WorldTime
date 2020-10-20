package alexrnov.worldtime.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import alexrnov.worldtime.model.Repository;

public class HomeViewModel extends ViewModel {

  private MutableLiveData<String> mText;

  private Repository repository;

  @Inject public HomeViewModel(@NotNull Repository repository) {
    this.repository = repository;
    mText = new MutableLiveData<>();
    mText.setValue("This is home fragment");
    repository.print();
  }

  public LiveData<String> getText() {
    return mText;
  }
}
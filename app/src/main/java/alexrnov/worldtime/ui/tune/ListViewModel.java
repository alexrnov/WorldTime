package alexrnov.worldtime.ui.tune;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListViewModel extends ViewModel {

  private MutableLiveData<String> mText;

  public ListViewModel() {
    mText = new MutableLiveData<>();
    mText.setValue("This is notifications fragment");
  }

  public LiveData<String> getText() {
    return mText;
  }
}
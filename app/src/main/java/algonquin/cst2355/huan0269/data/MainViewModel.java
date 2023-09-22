package algonquin.cst2355.huan0269.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    //observe this object:
    public MutableLiveData<String> userString = new MutableLiveData("");
    public MutableLiveData<Boolean> onOrOff = new MutableLiveData<>();
}

package com.example.administrator.lztsg;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayerStateViewModel extends ViewModel {
    private MutableLiveData<Boolean> data = new MutableLiveData<>();

    public void setData(boolean State) {
        data.setValue(State);
    }

    public LiveData<Boolean> getData() {
        return data;
    }
}

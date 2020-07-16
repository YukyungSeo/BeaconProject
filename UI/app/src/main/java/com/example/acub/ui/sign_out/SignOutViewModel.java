package com.example.acub.ui.sign_out;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.acub.MainActivity;
import com.example.acub.R;

public class SignOutViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SignOutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("아ㅏㅏ아아아아ㅏㅇ아ㅏ아");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.example.acub.ui.sign_out;

import android.content.Intent;
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

import com.example.acub.MainActivity;
import com.example.acub.R;

public class SignOutFragment extends Fragment {

    private SignOutViewModel signUpViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        signUpViewModel =
                ViewModelProviders.of(this).get(SignOutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sign_out, container, false);
        final TextView textView = root.findViewById(R.id.text_sign_out);
        signUpViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}

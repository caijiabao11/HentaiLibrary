package com.example.administrator.lztsg.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TimePicker;

import com.example.administrator.lztsg.MyTimePickerDialog;
import com.example.administrator.lztsg.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AsmrSleepMoreFragment extends Fragment{
    private static Context context;
    private TimePicker timepicker;
    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(context);
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_asmr_sleepmore,container,false);
        initView(rootview);
        initData(context);

        return rootview;
    }

    private void initView(View view) {
//        timepicker = view.findViewById(R.id.timepicker);
    }
    private void initData(Context context) {
        MyTimePickerDialog myTimePickerDialog = new MyTimePickerDialog(context);
        Window window = myTimePickerDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM; // 设置对话框的位置为底部
            window.setAttributes(layoutParams);
        }
        myTimePickerDialog.show();
    }
}

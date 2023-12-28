package com.example.administrator.lztsg.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.administrator.lztsg.R;
import com.example.administrator.lztsg.httpjson.AsmrHttpJson;
import com.example.administrator.lztsg.httpjson.HttpAsmr_ChipJsonResolution;
import com.example.administrator.lztsg.items.Asmr_chip;
import com.example.administrator.lztsg.items.MultipleItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AsmrVasFragment extends Fragment{
    private static ChipGroup mChip_Vas;
    private EditText mSearch;
    private static Context context;
    public static List<MultipleItem> mData;
    public static List<MultipleItem> mAllData;
    public static List<String> vas_id,vas_name,vas_count;

    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(context);
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_asmr_vas,container,false);
        initView(rootview);
        initData(context);

        return rootview;
    }

    private void initView(View view) {
        mChip_Vas = view.findViewById(R.id.item_mass);
    }
    private void initData(Context context) {
        this.mData = new ArrayList<>();
        this.mAllData = new ArrayList<>();
        this.vas_id = new ArrayList<>();
        this.vas_name = new ArrayList<>();
        this.vas_count = new ArrayList<>();
        mSearch = AsmrActivity.mSearch;

        Handler handler = new Handler();
        AsmrHttpJson.getAsmr_vasData(new HttpAsmr_ChipJsonResolution() {
            @Override
            public void onFinish(ArrayList<Asmr_chip> asmr_vaschip) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        addTag(asmr_vaschip);
                    }
                });

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void addTag(ArrayList<Asmr_chip> asmr_vaschip) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Asmr_chip value:asmr_vaschip){
                    String id = value.getId();
                    String name = value.getName();
                    String count = value.getCount();
                    Chip chip = createChiptext(name+" ("+count+")",name);

                    mChip_Vas.addView(chip);
                }
            }
        });
    }

    private Chip createChiptext(String str,String name) {

        Chip chip = new Chip(getContext());
        chip.setText(str);
        chip.setCloseIconVisible(false);
        chip.setClickable(true);
        chip.setOnClickListener(new Chip.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearch.setText(name);
            }
        });

        return chip;
    }
}

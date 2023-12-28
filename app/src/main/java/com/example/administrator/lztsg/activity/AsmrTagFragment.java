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

public class AsmrTagFragment extends Fragment{
    private static ChipGroup mChip_Mass;
    private EditText mSearch;
    private static Context context;
    public static List<MultipleItem> mData;
    public static List<MultipleItem> mAllData;
    public static List<String> tag_id,tag_name,tag_count;

    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(context);
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_asmr_tag,container,false);
        initView(rootview);
        initData(context);

        return rootview;
    }

    private void initView(View view) {
        mChip_Mass = view.findViewById(R.id.item_mass);
    }
    private void initData(Context context) {
        this.mData = new ArrayList<>();
        this.mAllData = new ArrayList<>();
        this.tag_id = new ArrayList<>();
        this.tag_name = new ArrayList<>();
        this.tag_count = new ArrayList<>();
        mSearch = AsmrActivity.mSearch;

        Handler handler = new Handler();
        AsmrHttpJson.getAsmr_tagData(new HttpAsmr_ChipJsonResolution() {
            @Override
            public void onFinish(ArrayList<Asmr_chip> asmr_tagchip) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        addTag(asmr_tagchip);
                    }
                });

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void addTag(ArrayList<Asmr_chip> asmr_masschip) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Asmr_chip value:asmr_masschip){
                    String id = value.getId();
                    String name = value.getName();
                    String count = value.getCount();
                    Chip chip = createChiptext(name+" ("+count+")",name);

                    mChip_Mass.addView(chip);
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

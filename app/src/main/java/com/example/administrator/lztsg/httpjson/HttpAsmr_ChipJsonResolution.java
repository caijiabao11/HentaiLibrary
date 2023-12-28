package com.example.administrator.lztsg.httpjson;

import com.example.administrator.lztsg.items.Asmr_chip;

import java.util.ArrayList;

public interface HttpAsmr_ChipJsonResolution {
    void onFinish(ArrayList<Asmr_chip> asmr_masschip);
    void onError(Exception e);
}

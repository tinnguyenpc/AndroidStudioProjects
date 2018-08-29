package com.example.hieul.hismart;

/**
 * Created by hieul on 26-Nov-17.
 */

public class GetsetThanhtoan {

    private String STT;
    private String TENMON;
    private String TIME;
    private String GIA;

    public GetsetThanhtoan(String STT, String TENMON, String TIME, String GIA) {
        this.STT = STT;
        this.TENMON = TENMON;
        this.TIME = TIME;
        this.GIA = GIA;
    }

    public String getSTT() {
        return STT;
    }

    public String getTENMON() {
        return TENMON;
    }

    public String getTIME() {
        return TIME;
    }

    public String getGIA() {
        return GIA;
    }

}
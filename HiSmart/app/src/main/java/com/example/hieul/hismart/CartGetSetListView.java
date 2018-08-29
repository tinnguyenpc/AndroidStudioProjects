package com.example.hieul.hismart;

/**
 * Created by hieul on 29-Oct-17.
 */

public class CartGetSetListView {
    public CartGetSetListView(String imgurlMon, String tenmon, String idmon, String gia, String imgDel) {
        super();
        this.imgurlmon = imgurlMon;
        this.tenmon = tenmon;
        this.idmon = idmon;
        this.gia = gia;
        this.imgdel = imgDel;



    }

    private String imgurlmon;
    private String tenmon;
    private String idmon;
    private String gia;
    private String imgdel;


    public String getImgurlmon() {
        return imgurlmon;
    }

    public void setImgurlmon(String imgurlmon) {
        this.imgurlmon = imgurlmon;
    }

    public String getTenmon() {
        return tenmon;
    }

    public String getIdmon() {
        return idmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getImgdel() {
        return imgdel;
    }

    public void setImgdel(String imgdel) {
        this.imgdel = imgdel;
    }


}

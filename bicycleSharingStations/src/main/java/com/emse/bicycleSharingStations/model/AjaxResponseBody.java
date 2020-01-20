package com.emse.bicycleSharingStations.model;

import java.util.List;

public class AjaxResponseBody {

    String msg;
    List <BicycleStation> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<BicycleStation> getResult() {
        return result;
    }

    public void setResult(List<BicycleStation> result) {
        this.result = result;
    }
}

package com.hjay.easychat.entity.enums;


public enum ResponseCodeEnum {

    CODE_200(200, "200"),

    CODE_404(404, "404"),

    CODE_500(500, "500"),

    CODE_600(600, "600"),

    CODE_601(601, "601");

    private final int code;
    private final String msg;

    public int getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    ResponseCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

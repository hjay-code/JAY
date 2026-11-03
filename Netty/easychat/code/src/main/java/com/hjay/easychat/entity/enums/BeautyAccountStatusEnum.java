package com.hjay.easychat.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum BeautyAccountStatusEnum {

    NO_USE(0, "未使用"),
    USEED(1, "已使用");

    private Integer status;
    private String desc;


    public static BeautyAccountStatusEnum getByStatus(Integer status){
        for(BeautyAccountStatusEnum item: BeautyAccountStatusEnum.values()){
            if(item.getStatus().equals(status)){
                return item;
            }
        }
        return null;
    }
}

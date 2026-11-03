package com.hjay.easychat.entity.enums;

import com.hjay.easychat.utils.StringTools;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum UserContactTypeEnum {

    USER(0, "U", "好友"),
    GROUP(1, "G", "群聊");

    private Integer type;

    private String prefix;

    private String desc;

    UserContactTypeEnum(Integer type, String prefix, String desc){
        this.type = type;
        this.prefix = prefix;
        this.desc = desc;
    }

    public static UserContactTypeEnum getByName(String name){
        try {
            if(StringTools.isEmpty(name)){
                return null;
            }
            return UserContactTypeEnum.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }

    public static UserContactTypeEnum getByPrefix(String prefix){
        try {
            if(prefix.trim().isEmpty() || StringTools.isEmpty(prefix)){
                return null;
            }
            prefix = prefix.substring(0,1);
            for(UserContactTypeEnum typeEnum: UserContactTypeEnum.values()){
                if(typeEnum.getPrefix().equals(prefix)){
                    return typeEnum;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

package com.hjay.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件关联人员
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

   private String username;
   private String password;
}

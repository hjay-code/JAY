package com.hjay.easychat.controller;

import com.hjay.easychat.entity.enums.ResponseCodeEnum;
import com.hjay.easychat.entity.vo.ResponseVO;
import com.hjay.easychat.exception.BusinessException;

public class ABaseController {

    protected static final String STATUC_SUCCESS = "success";

    protected static final String STATUC_ERROR = "error";

    protected <T> ResponseVO<T> getSuccessResponseVO(T t){
        ResponseVO<T> responseVO = new ResponseVO<T>();
        responseVO.setStatus(STATUC_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    protected <T> ResponseVO<T> getBusinessErrorResponseVO(BusinessException e, T t){
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUC_ERROR);
        if(e.getCode() == null){
            responseVO.setCode(ResponseCodeEnum.CODE_600.getCode());
        }else {
            responseVO.setCode(e.getCode());
        }
        responseVO.setInfo(e.getMessage());
        responseVO.setData(t);
        return responseVO;
    }

    protected <T> ResponseVO<T> getServerErrorResponseVO(T t){
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUC_ERROR);
        responseVO.setCode(ResponseCodeEnum.CODE_500.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        responseVO.setData(t);
        return responseVO;
    }
}

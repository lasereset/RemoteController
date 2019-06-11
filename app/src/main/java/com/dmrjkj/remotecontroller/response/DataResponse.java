package com.dmrjkj.remotecontroller.response;

import com.alibaba.fastjson.JSON;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class DataResponse<T> extends ApiResponse {
    T object;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

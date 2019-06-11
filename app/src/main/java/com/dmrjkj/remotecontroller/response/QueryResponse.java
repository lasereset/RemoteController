package com.dmrjkj.remotecontroller.response;

import com.alibaba.fastjson.JSON;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class QueryResponse<T> extends ApiResponse{
    private List<T> items;

    private Pagination page;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

package com.dmrjkj.remotecontroller.response;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse {

    private String result;

    private int code;

    private Object data;
}

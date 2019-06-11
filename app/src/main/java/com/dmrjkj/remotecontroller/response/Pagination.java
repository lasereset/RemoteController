package com.dmrjkj.remotecontroller.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pagination {
    // 当前页,从1开始/INOUT
    private int page;
    // 每页数量/INOUT
    private int pageSize;
    // 是否有下一页
    private boolean hasNext;
    // 是否有上一页
    private boolean hasPrev;

    private int total;

    private int pages;
}

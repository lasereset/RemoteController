package com.dmrjkj.remotecontroller.events;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xinchen on 19-6-10.
 */

@Data
@NoArgsConstructor
public class ChangePwdEvent {
    private boolean ok;

    private String errorMsg;

    public ChangePwdEvent(boolean ok, String errorMsg) {
        this.ok = ok;
        this.errorMsg = errorMsg;
    }
}

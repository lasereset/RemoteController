package com.dmrjkj.remotecontroller.events;

import com.dmrjkj.remotecontroller.enums.TouchFeedbackEnums;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xinchen on 19-5-29.
 */

@Data
@NoArgsConstructor
public class DeviceSettingEvent {

    private String name;

    private boolean changeName;

    private TouchFeedbackEnums feedbackEnums;

    private boolean changeTouchModel;

    public DeviceSettingEvent(String name, boolean changeName) {
        this.name = name;
        this.changeName = changeName;
    }

    public DeviceSettingEvent(boolean changeTouchModel, TouchFeedbackEnums feedbackEnums) {
        this.feedbackEnums = feedbackEnums;
        this.changeTouchModel = changeTouchModel;
    }
}

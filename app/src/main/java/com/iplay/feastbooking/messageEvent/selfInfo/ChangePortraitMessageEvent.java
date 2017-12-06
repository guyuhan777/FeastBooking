package com.iplay.feastbooking.messageEvent.selfInfo;

import java.io.File;

/**
 * Created by gu_y-pc on 2017/12/6.
 */

public class ChangePortraitMessageEvent {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILURE = 1;

    private int type;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    private File file;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}

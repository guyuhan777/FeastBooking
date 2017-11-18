package com.iplay.feastbooking.messageEvent.contract;

import java.io.File;
import java.util.List;

/**
 * Created by Guyuhan on 2017/11/12.
 */

public class PhotoUpdateMessageEvent {

    public enum TYPE {TYPE_FAILURE, TYPE_SUCCESS}

    public TYPE type;

    private String failureResult;

    private List<File> filesToDelete;

    public PhotoUpdateMessageEvent(TYPE type, String... failureResult){
        this.type = type;
        if(this.type == TYPE.TYPE_FAILURE){
            this.failureResult = failureResult[0];
        }
    }

    public List<File> getFilesToDelete() {
        return filesToDelete;
    }

    public void setFilesToDelete(List<File> filesToDelete) {
        this.filesToDelete = filesToDelete;
    }
}

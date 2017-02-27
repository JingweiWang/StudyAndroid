package io.github.jingweiwang.tcppool;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jingweiwang on 2016/10/18.
 */

public class OperationBean {
    @SerializedName("from")
    public String from;
    @SerializedName("content")
    private String content;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

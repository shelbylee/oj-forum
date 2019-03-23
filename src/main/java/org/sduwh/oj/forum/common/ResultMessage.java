package org.sduwh.oj.forum.common;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResultMessage {

    private Integer code;

    private String msg;

    private Object data;

    public ResultMessage(Integer code) {
        this.code = code;
    }

    public static ResultMessage success(Object object, String msg) {
        ResultMessage resultMessage = new ResultMessage(200);
        resultMessage.msg = msg;
        resultMessage.data = object;
        return resultMessage;
    }

    public static ResultMessage success(Object object) {
        ResultMessage resultMessage = new ResultMessage(200);
        resultMessage.data = object;
        return resultMessage;
    }

    public static ResultMessage success() {
        return new ResultMessage(200);
    }

    public static ResultMessage fail(String msg) {
        ResultMessage resultMessage = new ResultMessage(500);
        resultMessage.msg = msg;
        return resultMessage;
    }
}

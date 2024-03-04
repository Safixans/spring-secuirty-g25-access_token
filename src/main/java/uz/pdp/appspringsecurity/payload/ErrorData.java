package uz.pdp.appspringsecurity.payload;

import lombok.Getter;

@Getter
public class ErrorData {

    private final String msg;

    private final int status;

    private ErrorData(String msg, int status) {
        this.msg = msg;
        this.status = status;
    }

    public static ErrorData errorObj(String msg, int status) {
        return new ErrorData(msg, status);
    }
}

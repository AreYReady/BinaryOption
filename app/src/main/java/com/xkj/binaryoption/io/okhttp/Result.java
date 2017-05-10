package com.xkj.binaryoption.io.okhttp;

/**
 * Created by huangsc on 2017-01-24.
 * TODO:
 */

public class Result {
    private String resultcode;
    private String reason;
    private String result;
    private int error_code;
    public Result(String resultcode, String reason, String result, int error_code) {
        this.resultcode = resultcode;
        this.reason = reason;
        this.result = result;
        this.error_code = error_code;
    }
    public Result() {
    }
    @Override
    public String toString() {
        return "Result{" +
                "resultcode='" + resultcode + '\'' +
                ", reason='" + reason + '\'' +
                ", result='" + result + '\'' +
                ", error_code=" + error_code +
                '}';
    }
    public String getResultcode() {
        return resultcode;
    }
    public String getReason() {
        return reason;
    }
    public String getResult() {
        return result;
    }
    public int getError_code() {
        return error_code;
    }
    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}

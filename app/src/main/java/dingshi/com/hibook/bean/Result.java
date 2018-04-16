package dingshi.com.hibook.bean;

import java.io.Serializable;

/**
 * @author wangqi
 * @since 2017/11/9 10:17
 */

public class Result implements Serializable {

    private int error_code;
    private String error_msg;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }


    @Override
    public String toString() {
        return "Result{" +
                "error_code=" + error_code +
                ", error_msg='" + error_msg + '\'' +
                '}';
    }
}

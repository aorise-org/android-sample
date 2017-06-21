package cn.aorise.common.core.exception;


/**
 * 封装Exception的基类
 * Created by tangjy on 2015/11/3.
 */
public class BaseException extends Exception {
    public BaseException(String detailMessage) {
        super(detailMessage);
    }

    public BaseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BaseException(Throwable throwable) {
        super(throwable);
    }
}

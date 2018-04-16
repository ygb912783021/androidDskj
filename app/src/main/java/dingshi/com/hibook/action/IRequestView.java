package dingshi.com.hibook.action;

/**
 * @author wangqi
 * @since 2017/12/11 16:58
 */

public interface IRequestView<T> {
    void onLoad();

    void onSuccess(T t);

    void onError(String error);

    void onEmpty();

}

package dingshi.com.hibook.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * @author wangqi
 * @since 2018/3/9 16:19
 */

public class EventBusHelper {

    public static void register(Object context) {
        EventBus.getDefault().register(context);
    }

    public static void unRegister(Object context) {
        EventBus.getDefault().unregister(context);
    }

   public static void post(Object object){
        EventBus.getDefault().post(object);
   }
}

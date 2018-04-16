package dingshi.com.hibook.push;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import dingshi.com.hibook.bean.Push;
import dingshi.com.hibook.ui.BookDetailsActivity;
import dingshi.com.hibook.ui.Case2BookActivity;
import dingshi.com.hibook.ui.WebActivity;
import dingshi.com.hibook.ui.card.CardDetailsActivity;
import dingshi.com.hibook.ui.library.JoinLibActivity;

/**
 * @author wangqi
 * @since 2018/3/5 16:02
 */

public class PushUtils {
    public void judge(Context context, String strPush) {
        if ("".equals(strPush)) {
            return;
        }
        try {
            Push push = new Gson().fromJson(strPush, Push.class);
            Intent intent = new Intent();
            switch (push.getPayload().getType()) {
                case "1":
                    intent.setClass(context, BookDetailsActivity.class);
                    intent.putExtra("isbn", push.getPayload().getSource_id());
                    context.startActivity(intent);
                    break;
                case "2":
                    intent.setClass(context, Case2BookActivity.class);
                    intent.putExtra("serial_number", push.getPayload().getSource_id());
                    intent.putExtra("radius", "1000");
                    context.startActivity(intent);
                    break;
                case "3":
                    intent.setClass(context, JoinLibActivity.class);
                    intent.putExtra("catalog_id", push.getPayload().getSource_id());
                    context.startActivity(intent);
                    break;
                case "4":
                    intent.setClass(context, CardDetailsActivity.class);
                    intent.putExtra("uid", push.getPayload().getSource_id());
                    intent.putExtra("card_id", "");
                    context.startActivity(intent);
                    break;
                case "5":
                    break;
                case "6":
                    break;
                case "7":
                    intent.setClass(context, WebActivity.class);
                    intent.putExtra("url", push.getPayload().getSource_url());
                    context.startActivity(intent);
                    break;
                default:
            }
        } catch (Exception e) {
        }
    }
}

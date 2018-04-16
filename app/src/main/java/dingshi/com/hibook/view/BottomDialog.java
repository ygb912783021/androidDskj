package dingshi.com.hibook.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import dingshi.com.hibook.R;

/**
 * @author wangqi
 * @since 2017/11/13 下午1:45
 */


public class BottomDialog {
    private Context context;
    private Dialog dialog;
    private Display display;

    public BottomDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        display = windowManager.getDefaultDisplay();
        dialog = new Dialog(context, R.style.AlertDialogStyle);
    }

    public BottomDialog builder() {
        return this;
    }

    public BottomDialog addView(View view) {
        dialog.setContentView(view);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(R.color.tingming);
        view.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth()), LayoutParams.MATCH_PARENT));
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (display
//                .getWidth()), LayoutParams.MATCH_PARENT);
//        layoutParams.layoutAnimationParameters



        return this;
    }

    /**
     * 设置布局的比例
     *
     * @param view
     * @param scale 0到1   0.3
     * @return
     */
    public BottomDialog addView(View view, float scale) {
        dialog.setContentView(view);
        view.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * scale), LayoutParams.WRAP_CONTENT));
        return this;
    }

    public void show() {
        dialog.show();
    }


    public BottomDialog setCancelable(boolean flag) {
        dialog.setCancelable(flag);
        return this;
    }

    public void dissmis() {
        dialog.dismiss();
    }
}

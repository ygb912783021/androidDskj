package dingshi.com.hibook.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;

import dingshi.com.hibook.R;

/**
 * @author wangqi
 * @since 2017/11/13 下午1:45
 */


public class FuckDialog {
    private Context context;
    private Dialog dialog;
    private Display display;

    public FuckDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        dialog = new Dialog(context, R.style.AlertDialogStyle);
    }

    public FuckDialog builder() {
        return this;
    }

    public FuckDialog addView(View view) {
        dialog.setContentView(view);
        view.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
        return this;
    }

    /**
     * 设置布局的比例
     *
     * @param view
     * @param scale 0到1   0.3
     * @return
     */
    public FuckDialog addView(View view, float scale) {
        dialog.setContentView(view);
        view.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * scale), LayoutParams.WRAP_CONTENT));
        return this;
    }

    public void show() {
        dialog.show();
    }


    public FuckDialog setCancelable(boolean flag) {
        dialog.setCancelable(flag);
        return this;
    }

    public void dissmis() {
        dialog.dismiss();
    }
}

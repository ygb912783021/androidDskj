package dingshi.com.hibook.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author wangqi
 * @since 2017/12/12 14:00
 */

public class AbsRecyclerView extends RecyclerView {
    public AbsRecyclerView(Context context) {
        super(context);
    }

    public AbsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

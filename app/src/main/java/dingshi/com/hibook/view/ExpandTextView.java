package dingshi.com.hibook.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dingshi.com.hibook.R;

/**
 * @author wangqi
 *         Created by apple on 2017/11/6.
 */

public class ExpandTextView extends LinearLayout {
    public static final String tip1 = "展开";
    public static final String tip2 = "收起";
    Context context;
    TextView textView;
    /**
     * 内容view
     */
    TextView txContent;
    /**
     * 扩展点击
     */
    ImageView imgExpand;
    /**
     * 设置的内容
     */
    String content = "";

    /**
     * 收缩时的高度
     */
    int shrinkHeight;
    /**
     * 展开时的高度
     */

    int expandHeight;


    /**
     * true需要展开、false需要收起
     */
    boolean flag = true;

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setOrientation(VERTICAL);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        textView = new TextView(context);
        txContent = new TextView(context);
        imgExpand = new ImageView(context);
        imgExpand.setImageResource(R.drawable.expand_img);


        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        txContent.setLayoutParams(params);
        textView.setLayoutParams(params);


        LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.RIGHT;
        imgExpand.setLayoutParams(params2);


        txContent.setTextColor(getResources().getColor(R.color.black_3f));
        txContent.setTextSize(15);
        textView.setTextSize(15);


        addView(txContent);
        addView(imgExpand);
        addView(textView);

        imgExpand.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    txContent.setMaxHeight(expandHeight);
                } else {
                    txContent.setMaxHeight(shrinkHeight);
                }
                flag = !flag;
            }
        });

        txContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    txContent.setMaxHeight(expandHeight);
                } else {
                    txContent.setMaxHeight(shrinkHeight);
                }
                flag = !flag;
            }
        });
        setTextHeight();
    }


    public void setText(String str) {
        content = str;
        setTextHeight();
    }

    /**
     * 设置TextView的文本，根据文本获取TextView实际的高度
     * <p>
     * 如果TextView的高度小于默认"展开"的高度，就没必要设置"展开"，设置visible
     */
    public void setTextHeight() {
        textView.setVisibility(View.VISIBLE);
        textView.setText("我");

        txContent.setText(content);

        txContent.post(new Runnable() {
            @Override
            public void run() {
                //收缩的高度为text的两行
                shrinkHeight = textView.getMeasuredHeight() * 6;
                expandHeight = txContent.getMeasuredHeight();
                textView.setVisibility(View.GONE);

                //如果文本的高度小于
                if (shrinkHeight >= expandHeight) {
                    imgExpand.setVisibility(View.GONE);
                } else {
                    txContent.setMaxHeight(shrinkHeight);
                    imgExpand.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}

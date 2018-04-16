package dingshi.com.hibook.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dingshi.com.hibook.R;

/**
 * @author wangqi
 * @since 2017/11/3.
 */

public class QuantityView extends LinearLayout {


    int quantitySize = 30;
    /**
     * 字体的尺寸
     */
    int textSize = 13;
    /**
     * 字体颜色
     */
    int textColor;

    /**
     * value值
     */
    int value = 10;

    /**
     * 控件的宽高
     */
    int width = 100;

    /**
     * 最小的值
     */
    int minValue = 0;

    /**
     * 最大的值
     */
    int maxValue = 30;

    /**
     * 增减控件的背景
     */
    int src;
    /**
     * 控件的提示文字
     */
    String tip = "";

    TextView minusText;
    TextView valueText;
    TextView plusText;
    TextView tipText;


    public QuantityView(Context context) {
        this(context, null);
    }

    public QuantityView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuantityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.QuantityView);
        textColor = ta.getColor(R.styleable.QuantityView_quan_color, 0xff000000);
        minValue = ta.getInteger(R.styleable.QuantityView_quan_min, 0);
        maxValue = ta.getInteger(R.styleable.QuantityView_quan_max, 100);
        src = ta.getResourceId(R.styleable.QuantityView_quan_src, R.drawable.quantity_gray);
        width = ta.getDimensionPixelOffset(R.styleable.QuantityView_quan_width, 80);
        tip = ta.getString(R.styleable.QuantityView_quan_tip);
        ta.recycle();

        initView(context);
    }

    private void initView(Context context) {
        tipText = new TextView(context);
        minusText = new TextView(context);
        valueText = new TextView(context);
        plusText = new TextView(context);

        tipText.setText(tip + "    ");
        minusText.setBackgroundResource(R.drawable.quan_minus);
//        minusText.setText("-");
        valueText.setText(String.valueOf(value));
//        plusText.setText("+");
        plusText.setBackgroundResource(R.drawable.quan_plus);
        tipText.setGravity(Gravity.CENTER);
        minusText.setGravity(Gravity.CENTER);
        valueText.setGravity(Gravity.CENTER);
        plusText.setGravity(Gravity.CENTER);


        valueText.setTextSize(textSize);

//        minusText.setBackgroundResource(src);
//        plusText.setBackgroundResource(src);


        LayoutParams params = new LayoutParams(width, width);
        minusText.setLayoutParams(params);
        valueText.setLayoutParams(params);
        plusText.setLayoutParams(params);


        valueText.setTextColor(getResources().getColor(R.color.black_90));
        addView(tipText);
        addView(minusText);
        addView(valueText);
        addView(plusText);


        minusText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                minus();
            }
        });

        plusText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                plus();
            }
        });
    }


    private void minus() {
        if (value != minValue) {
            value--;
            updateValue();
        }
    }


    private void plus() {
        if (value != maxValue) {
            value++;
            updateValue();
        }
    }


    private void updateValue() {
        if (onBackValue != null) {
            onBackValue.day(value);
        }
        valueText.setText(String.valueOf(value));
    }

    public int getValue() {
        return value;
    }


    OnBackValue onBackValue;

    public void setOnBackDay(OnBackValue onBackDay) {
        this.onBackValue = onBackDay;
    }

    public interface OnBackValue {
        void day(int day);
    }
}

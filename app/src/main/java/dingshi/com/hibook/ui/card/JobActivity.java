package dingshi.com.hibook.ui.card;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;

/**
 * @author wangqi
 * @since 2017/12/19 下午1:57
 */


public class JobActivity extends BaseActivity {
    public static final int JOB_REQUEST_CODE = 0x1027;
    @BindView(R.id.job_start_time)
    TextView txStartTime;
    @BindView(R.id.job_end_time)
    TextView txEndTime;
    @BindView(R.id.job_company)
    EditText editCompany;
    @BindView(R.id.job_post)
    EditText editPost;
    @BindView(R.id.job_content)
    EditText editContent;

    private String strStartTime="";
    private String strEndTime="";
    private String strCompany="";
    private String strPost="";
    private String strContent="";

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "工作经历", "");
        HashMap<String, String> map;
        if ((map = (HashMap<String, String>) getIntent().getSerializableExtra("map")) != null) {
            if ((strStartTime = map.get("w_start")) != null) {
                txStartTime.setText(strStartTime);
            }
            if ((strEndTime = map.get("w_end")) != null) {
                txEndTime.setText(strEndTime);
            }
            if ((strCompany = map.get("w_company")) != null) {
                editCompany.setText(strCompany);
            }
            if ((strPost = map.get("w_position")) != null) {
                editPost.setText(strPost);
            }

            if ((strContent = map.get("w_describe")) != null) {
                editContent.setText(strContent);
            }
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_job;
    }


    @OnClick({R.id.job_start_time_layout, R.id.job_end_time_layout, R.id.job_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.job_start_time_layout:
                getStartTime();
                break;
            case R.id.job_end_time_layout:
                getEndTime();
                break;
            case R.id.job_save:
                submit();
                break;
            default:
        }
    }

    Calendar c = Calendar.getInstance();

    /**
     * 获取时间
     */
    public void getStartTime() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                strStartTime = DateFormat.format("yyyy-MM-dd", c).toString();
                txStartTime.setText(strStartTime);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    /**
     * 获取时间
     */
    public void getEndTime() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                strEndTime = DateFormat.format("yyyy-MM-dd", c).toString();
                txEndTime.setText(strEndTime);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    private void submit() {
        strCompany = editCompany.getText().toString();
        strPost = editPost.getText().toString();
        strContent = editContent.getText().toString();


        HashMap<String, String> map = new HashMap<>(2);
        map.put("w_start", strStartTime);
        map.put("w_end", strEndTime);
        map.put("w_company", strCompany);
        map.put("w_position", strPost);
        map.put("w_describe", strContent);

        Intent intent = getIntent();
        intent.putExtra("map", map);
        setResult(JOB_REQUEST_CODE, intent);
        finish();
    }


}

package dingshi.com.hibook.ui.card;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.utils.CheckEmoJi;

/**
 * @author wangqi
 * @since 2017/12/19 下午1:42
 */


public class EduActivity extends BaseActivity {
    public static final int EDU_REQUEST_CODE = 0x1129;
    @BindView(R.id.edu_start_time)
    TextView txStartTime;
    @BindView(R.id.edu_end_time)
    TextView txEndTime;
    @BindView(R.id.edu_school)
    EditText editSchool;
    @BindView(R.id.edu_major)
    EditText editMajor;
    @BindView(R.id.edu_background)
    TextView txCollege;
    @BindView(R.id.edu_content)
    EditText editContent;


    private String strStartTime = "";
    private String strEndTime = "";
    private String strSchool = "";
    private String strMajor = "";
    private String strLevel = "";
    private String strContent = "";
//    获取当前时间
    int cdate=Integer.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())).replace("-", ""));
    int stTime=0;
    int entime=0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edu;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "教育经历", "");
        HashMap<String, String> map;

        if ((map = (HashMap<String, String>) getIntent().getSerializableExtra("map")) != null) {
            if ((strStartTime = map.get("e_start")) != null) {
                txStartTime.setText(strStartTime);
            }
            if ((strEndTime = map.get("e_end")) != null) {
                txEndTime.setText(strEndTime);
            }
            if ((strSchool = map.get("college")) != null) {
                editSchool.setText(strSchool);
            }
            if ((strMajor = map.get("major")) != null) {
                editMajor.setText(strMajor);
            }
            if ((strLevel = map.get("level")) != null && Integer.valueOf(strLevel) > 0) {

                txCollege.setText(pros[Integer.parseInt(strLevel) - 1]);
            }
            if ((strContent = map.get("e_describe")) != null) {
                editContent.setText(strContent);
            }
        }


    }

    @OnClick({R.id.edu_start_time_layout,
            R.id.edu_end_time_layout,
            R.id.edu_background_layout,
            R.id.edu_save
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edu_start_time_layout:
                getStartTime();
                break;
            case R.id.edu_end_time_layout:
                getEndTime();
                break;
            case R.id.edu_background_layout:
                getPro();
                break;
            case R.id.edu_save:
                save();
                break;
            default:
        }
    }

    /**
     * 保存
     */
    private void save() {
        strSchool = editSchool.getText().toString();
        strContent = editContent.getText().toString();
        strMajor = editMajor.getText().toString();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String currentDate = simpleDateFormat.format(new Date(System.currentTimeMillis()));
//        String startime = strStartTime.replace("-", "");
//        String endtime = strEndTime.replace("-", "");
//        currentDate = currentDate.replace("-", "");
        if(!CheckEmoJi.containsEmoji(strSchool)&&
                !CheckEmoJi.containsEmoji(strContent)&&
                !CheckEmoJi.containsEmoji(strMajor)){
            HashMap<String, String> map = new HashMap<>(2);
            map.put("e_start", strStartTime);
            map.put("e_end", strEndTime);
            map.put("college", strSchool);
            map.put("major", strMajor);
            map.put("level", strLevel);
            map.put("e_describe", strContent);


            Intent intent = getIntent();
            intent.putExtra("map", map);
            setResult(EDU_REQUEST_CODE, intent);
            finish();
        }else {
//            Toast.makeText(this,"不支持Emoji表情",Toast.LENGTH_SHORT).show();
        }

    }


    Calendar c = Calendar.getInstance();

    /**
     * 获取开始时间
     */
    public void getStartTime() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                strStartTime = DateFormat.format("yyyy-MM-dd", c).toString();
                stTime=Integer.valueOf(strStartTime.replace("-",""));
               if(cdate-stTime>0){
                   txStartTime.setText(strStartTime);
               }else {
//                   Toast.makeText(EduActivity.this,"时间选择错误",Toast.LENGTH_SHORT).show();
               }

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    /**
     * 获取结束时间
     */
    public void getEndTime() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                strEndTime = DateFormat.format("yyyy-MM-dd", c).toString();
                entime=Integer.valueOf(strEndTime.replace("-",""));
                if(cdate-entime>0){
                    if(entime-stTime>0){
                        txEndTime.setText(strEndTime);
                    }

                }else {
//                    Toast.makeText(EduActivity.this,"时间选择错误",Toast.LENGTH_SHORT).show();
                }

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    /**
     * 获取学历
     */
    String[] pros = {"专科", "本科", "硕士", "博士"};
    AlertDialog.Builder dialog;

    public void getPro() {
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("学历");
        dialog.setSingleChoiceItems(pros, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        strLevel = String.valueOf(which + 1);
                        txCollege.setText(pros[which]);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }


}

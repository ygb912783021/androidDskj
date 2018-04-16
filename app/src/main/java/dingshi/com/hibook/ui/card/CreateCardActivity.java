package dingshi.com.hibook.ui.card;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuyh.library.imgsel.ISNav;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ICreateCardView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.card.CardDetails;
import dingshi.com.hibook.bean.card.CardList;
import dingshi.com.hibook.db.City;
import dingshi.com.hibook.db.CityDao;
import dingshi.com.hibook.db.SQLTools;
import dingshi.com.hibook.present.CreateCardPresent;
import dingshi.com.hibook.ui.CityActivity;
import dingshi.com.hibook.utils.CheckEmoJi;
import dingshi.com.hibook.utils.EmailUtils;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.ImageSelector;
import dingshi.com.hibook.utils.PhoneUtils;

/**
 * @author wangqi
 * @since 2017/12/19 上午11:36
 */

public class CreateCardActivity extends BaseActivity implements ICreateCardView {
    private static final int REQUEST_CAMERA_CODE = 0x0429;
    /**
     * 更多信息按钮
     */
    @BindView(R.id.create_card_more)
    TextView txMore;
    /**
     * 更多布局
     */
    @BindView(R.id.create_card_more_layout)
    LinearLayout moreLayout;
    /**
     * 头像
     */
    @BindView(R.id.create_card_photo)
    ImageView imgPhoto;

    /**
     * 姓名
     */
    @BindView(R.id.create_card_nick)
    EditText editNick;
    /**
     * 手机号
     */
    @BindView(R.id.create_card_mobile)
    EditText editMobile;
    /**
     * 名片公开
     */
    @BindView(R.id.create_card_type)
    TextView txCardType;
    /**
     * 性别
     */
    @BindView(R.id.create_card_gender)
    TextView txGender;
    /**
     * 公司单位
     */
    @BindView(R.id.create_card_company)
    EditText editCompany;
    /**
     * 职位
     */
    @BindView(R.id.create_card_job)
    EditText editJob;
    /**
     * 常住城市
     */
    @BindView(R.id.create_card_city)
    TextView txCity;
    /**
     * 邮箱
     */
    @BindView(R.id.create_card_email)
    EditText editEmail;

    /**
     * 资源优势
     */
    @BindView(R.id.create_card_resource)
    EditText editRes;

    /**
     * 生日
     */
    @BindView(R.id.create_card_birth)
    TextView txBirth;
    /**
     * 地址
     */
    @BindView(R.id.create_card_address)
    EditText editAddress;
    /**
     * 个人介绍
     */
    @BindView(R.id.create_card_intro)
    EditText editIntro;
    /**
     * 教育选项
     */
    @BindView(R.id.create_card_edu_txt)
    TextView txEdu;
    /**
     * 职位选项
     */
    @BindView(R.id.create_card_job_txt)
    TextView txJob;

    String strPhoto;

    String strNick;
    String strMobile;
    String strCardType;
    String strGender;
    String strCompany;
    String strRes = "";
    String strJob = "";
    String strCity = "";
    String strEmail = "";
    String strBirth = "";
    String strAddress = "";
    String strContent = "";
    /**
     * 教育的id
     */
    String educationId = "0";
    /**
     * 工作id
     */
    String workId = "0";


    /**
     * 卡片id
     */
    private String cardId = "";

    /**
     * 判断当前是编辑页面还创建见面
     */
    private boolean isEdit;

    CardDetails.JsonDataBean jsonData;

    CreateCardPresent present = new CreateCardPresent(this, this);


    @Override
    public int getLayoutId() {
        return R.layout.activity_create_card;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        jsonData = (CardDetails.JsonDataBean) getIntent().getSerializableExtra("bean");

        if (jsonData != null) {
            isEdit = true;
            requestActionBarStyle(true, "编辑名片", "完成");
            initData();
        } else {
            isEdit = false;
            requestActionBarStyle(true, "创建名片", "创建");
        }
    }


    private void initData() {
        cardId = jsonData.getId();
        editNick.setText(jsonData.getName());
        editMobile.setText(jsonData.getPhone());
        editCompany.setText(jsonData.getCompany());
        editJob.setText(jsonData.getPosition());
        editEmail.setText(jsonData.getEmail());
        editAddress.setText(jsonData.getAddress());
        editIntro.setText(jsonData.getIntroduce());
        editRes.setText(jsonData.getAdvantage());

        GlideUtils.loadCircleImage(this, jsonData.getAvatar(), imgPhoto);


        if (jsonData.getIs_open() > 0) {
            strCardType = String.valueOf(jsonData.getIs_open());
            txCardType.setText(present.pros[jsonData.getIs_open() - 1]);
        }

        if (jsonData.getSex() > 0) {
            strGender = String.valueOf(jsonData.getSex());
            txGender.setText(jsonData.getSex() == 1 ? "男" : "女");
        }

        City city = CityDao.queryCity(SQLTools.opendatabase(this), jsonData.getCity());
        setCity(city);

        strBirth = jsonData.getBirthday();
        txBirth.setText(strBirth);


        CardDetails.JsonDataBean.EducationBean eduBean = jsonData.getEducation().get(0);

        eduMap.put("e_start", eduBean.getStart() == null ? "" : eduBean.getStart());
        eduMap.put("e_end", eduBean.getEnd() == null ? "" : eduBean.getEnd());
        eduMap.put("college", eduBean.getCollege() == null ? "" : eduBean.getCollege());
        eduMap.put("major", eduBean.getMajor() == null ? "" : eduBean.getMajor());
        eduMap.put("level", String.valueOf(eduBean.getLevel()));
        eduMap.put("e_describe", eduBean.getDescribe() == null ? "" : eduBean.getDescribe());
        educationId = eduBean.getId();

        CardDetails.JsonDataBean.WorkBean workBean = jsonData.getWork().get(0);

        jobMap.put("w_start", workBean.getStart() == null ? "" : workBean.getStart());
        jobMap.put("w_end", workBean.getEnd() == null ? "" : workBean.getEnd());
        jobMap.put("w_company", workBean.getCompany() == null ? "" : workBean.getCompany());
        jobMap.put("w_position", workBean.getPosition() == null ? "" : workBean.getPosition());
        jobMap.put("w_describe", workBean.getDescribe() == null ? "" : workBean.getDescribe());
        workId = workBean.getId();

    }


    @OnClick({R.id.create_card_edu_txt, R.id.create_card_job_txt,
            R.id.create_card_more, R.id.create_card_photo, R.id.create_card_type,
            R.id.create_card_gender, R.id.create_card_birth, R.id.create_card_city
    })
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.create_card_edu_txt:
                intent.setClass(this, EduActivity.class);
                intent.putExtra("map", eduMap);
                startActivityForResult(intent, EduActivity.EDU_REQUEST_CODE);
                break;
            case R.id.create_card_job_txt:
                intent.setClass(this, JobActivity.class);
                intent.putExtra("map", jobMap);
                startActivityForResult(intent, JobActivity.JOB_REQUEST_CODE);
                break;
            case R.id.create_card_more:
                txMore.setVisibility(View.GONE);
                moreLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.create_card_photo:
                ISNav.getInstance().toListActivity(this, ImageSelector.getSingleConfig(), REQUEST_CAMERA_CODE);
                break;
            case R.id.create_card_type:
                present.openCardType();
                break;
            case R.id.create_card_gender:
                present.openGender();
                break;
            case R.id.create_card_birth:
                present.openBirth();
                break;
            case R.id.create_card_city:
                startActivityForResult(new Intent(this, CityActivity.class), CityActivity.CITY_REQUEST_CODE);
                break;
            default:
        }
    }


    @Override
    public void onRightClick() {
        super.onRightClick();
        strNick = editNick.getText().toString();
        strMobile = editMobile.getText().toString();
        strCompany = editCompany.getText().toString();
        strJob = editJob.getText().toString();
        strEmail = editEmail.getText().toString();
        strAddress = editAddress.getText().toString();
        strContent = editIntro.getText().toString();
        strRes = editRes.getText().toString();

        if (TextUtils.isEmpty(strNick)) {
            showToast("请输入昵称");
            return;
        }
        if (!PhoneUtils.isPhoneNumber(strMobile)) {
            showToast("请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(strCardType)) {
            showToast("请选择名片公开方式");
            return;
        }
        if (TextUtils.isEmpty(strGender)) {
            showToast("请选择性别");
            return;
        }
        if (TextUtils.isEmpty(strCompany)) {
            showToast("请输入公司单位");
            return;
        }
        if (TextUtils.isEmpty(strJob)) {
            showToast("请输入部门职位");
            return;
        }
        if (TextUtils.isEmpty(strCity)) {
            showToast("请选择城市");
            return;
        }
        if (TextUtils.isEmpty(strRes)) {
            showToast("请填写资源优势");
            return;
        }

        if (!isEdit) {
            if (TextUtils.isEmpty(strPhoto)) {
                showToast("缺少名片头像");
                return;
            }
        }

        //如果邮箱不为空，判断邮箱是否正确
        if (!TextUtils.isEmpty(strEmail)) {
            if (!EmailUtils.isEmail(strEmail)) {
                showToast("邮箱填写错误");
                return;
            }
        }
        if (!CheckEmoJi.containsEmoji(strNick) &&
                !CheckEmoJi.containsEmoji(strCompany) &&
                !CheckEmoJi.containsEmoji(strJob) &&
                !CheckEmoJi.containsEmoji(strEmail) &&
                !CheckEmoJi.containsEmoji(strAddress) &&
                !CheckEmoJi.containsEmoji(strContent) &&
                !CheckEmoJi.containsEmoji(strRes)) {
            HashMap<String, String> map = new HashMap<>(2);
            map.put("card_id", cardId);
            map.put("name", strNick);
            map.put("phone", strMobile);
            map.put("company", strCompany);
            map.put("position", strJob);
            map.put("sex", strGender);
            map.put("is_open", strCardType);
            map.put("city", strCity);
            map.put("advantage", strRes);
            map.put("email", strEmail);
            map.put("birthday", strBirth);
            map.put("address", strAddress);
            map.put("introduce", strContent);
            map.put("work_id", workId);
            map.put("education_id", educationId);

            map.putAll(jobMap);
            map.putAll(eduMap);
            present.submit(map, isEdit, strPhoto);
        }


    }


    @Override
    public void onCardType(String content, String id) {
        txCardType.setText(content);
        strCardType = id;
    }

    @Override
    public void onGender(String content, String id) {
        txGender.setText(content);
        strGender = id;
    }


    @Override
    public void onBirth(String birth) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        String bircheck = birth.replace("-", "");
        currentDate = currentDate.replace("-", "");
        if (Integer.valueOf(currentDate) - Integer.valueOf(bircheck) > 0) {
            txBirth.setText(birth);
            strBirth = birth;
        } else {
            Toast.makeText(this, "生日不能超出当前时间", Toast.LENGTH_SHORT).show();
        }


    }

    public void setCity(City city) {
        txCity.setText(city.getName());
        strCity = city.getCode();
    }


    @Override
    public void start() {
        showProgressDialog("请求中", true);
    }

    HashMap<String, String> jobMap = new HashMap<>();
    HashMap<String, String> eduMap = new HashMap<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        if (requestCode == CityActivity.CITY_REQUEST_CODE) {
            City city = (City) data.getSerializableExtra("city");
            setCity(city);
        }


        // 图片选择结果回调
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK) {
            List<String> pathList = data.getStringArrayListExtra("result");
            GlideUtils.loadCircleImage(this, pathList.get(0), imgPhoto);
            strPhoto = pathList.get(0);

            //如果当前是编辑状态修改的头像，需要上传到服务器
            if (isEdit) {
                present.uploadAvatar(strPhoto, cardId);
            }

        }

        if (requestCode == JobActivity.JOB_REQUEST_CODE) {
            jobMap = (HashMap<String, String>) data.getSerializableExtra("map");
            if (jobMap != null && jobMap.size() > 0) {
                txJob.setText("已填写");
            }
        }

        if (requestCode == EduActivity.EDU_REQUEST_CODE) {
            eduMap = (HashMap<String, String>) data.getSerializableExtra("map");
            if (eduMap != null && eduMap.size() > 0) {
                txEdu.setText("已填写");
            }
        }
    }

    @Override
    public void onLoad() {

    }

    /**
     * 返回成功
     *
     * @param result
     */
    @Override
    public void onSuccess(Result result) {
        dismissProgressDialog();

        if (isEdit) {
            showToast("修改成功");
        } else {
            showToast("创建成功");
        }
        finish();
        EventBus.getDefault().post(new CardList());
    }

    /**
     * 返回失败
     *
     * @param error
     */
    @Override
    public void onError(String error) {
        showToast(error);
        dismissProgressDialog();
    }

    @Override
    public void onEmpty() {

    }


}

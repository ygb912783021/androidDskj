package dingshi.com.hibook.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuyh.library.imgsel.ISNav;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ICreateLibView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.lib.LibCreate;
import dingshi.com.hibook.bean.lib.LibList;
import dingshi.com.hibook.eventbus.LibRefreshEvent;
import dingshi.com.hibook.present.CreateLibPresent;
import dingshi.com.hibook.utils.CheckEmoJi;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.utils.ImageSelector;

/**
 * @author wangqi
 * @since 2017/12/20 下午3:49
 */

public class CreateLibActivity extends BaseActivity implements ICreateLibView {
    public static final int LIB_DATA_REQUEST = 0x1129;
    private static final int REQUEST_CAMERA_CODE = 0x0429;
    @BindView(R.id.create_lib_type)
    TextView txType;
    @BindView(R.id.create_lib_name)
    EditText editName;
    @BindView(R.id.create_lib_check)
    TextView txCheck;
    @BindView(R.id.create_lib_describe)
    EditText editDescribe;
    @BindView(R.id.create_lib_psw)
    TextView txPsw;
    @BindView(R.id.create_lib_photo)
    ImageView imgPhoto;
    @BindView(R.id.create_lib_create)
    TextView txCreate;

    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_myName)
    TextView tv_myName;
    @BindView(R.id.tv_needCheck)
    TextView tv_needCheck;
    @BindView(R.id.tv_decribe)
    TextView tv_decribe;
    @BindView(R.id.tv_pickIcon)
    TextView tv_pickIcon;


    LibList.JsonDataBean libBean;

    public boolean isEdit;

    CreateLibPresent present = new CreateLibPresent(this, this);

    /**
     * 默认值为1
     */
    private String strType = "1";
    private String strName;
    private String strCheck = "1";
    private String strDescribe;
    private String strPsw = "1";
    private String strPhoto;

    /**
     * 图书馆id
     */
    private String catalogId = "";
    /**
     * 当前创建书友会
     */
    private boolean isRally;

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_lib;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        libBean = (LibList.JsonDataBean) getIntent().getSerializableExtra("bean");
        isRally = getIntent().getBooleanExtra("isRally", false);
        if (isRally) {
//            txType.setText(present.rallys[libBean.getType() - 6]);
            tv_myName.setText("书友会名称");
            tv_type.setText("书友会类别");
            tv_needCheck.setText("加入书友会填写项");
            tv_decribe.setText("书友会描述");
            tv_pickIcon.setText("书友会图标");

        } else {
//            txType.setText(present.types[libBean.getType() - 1]);
            tv_myName.setText("图书馆名称");
            tv_type.setText("图书馆类别");
            tv_needCheck.setText("加入图书馆审核");
            tv_decribe.setText("图书馆描述");
            tv_pickIcon.setText("图书馆图标");
        }
        if (libBean == null) {
            isEdit = false;
            requestActionBarStyle(true, isRally ? "创建书友会" : "创建图书馆", "确定");
            txCreate.setText(isRally ? "创建书友会" : "创建图书馆");
        } else {
            isEdit = true;
            requestActionBarStyle(true, isRally ? "修改书友会" : "修改图书馆", "确定");
            txCreate.setText(isRally ? "修改书友会" : "修改图书馆");
            GlideUtils.loadCircleImage(this, libBean.getIcon(), imgPhoto);

            catalogId = libBean.getCatalog_id();

            if (libBean.getType() > 0) {
                strType = String.valueOf(libBean.getType() - 1);

                if (isRally) {
                    txType.setText(present.rallys[libBean.getType() - 6]);

                } else {
                    txType.setText(present.types[libBean.getType() - 1]);
                }
            }

            if (libBean.getIs_check() > 0) {
                strCheck = String.valueOf(libBean.getIs_check() - 1);
                txCheck.setText(present.checks[libBean.getIs_check() - 1]);
            }

            if (libBean.getIs_encrypt() > 0) {
                strPsw = String.valueOf(libBean.getIs_encrypt() - 1);
                txPsw.setText(present.psws[libBean.getIs_encrypt() - 1]);
            }

            strName = libBean.getName();
            strDescribe = libBean.getDescribe();
            editName.setText(libBean.getName());
            editDescribe.setText(libBean.getDescribe());
        }

    }

    @OnClick({R.id.create_lib_type, R.id.create_lib_create, R.id.create_lib_check, R.id.create_lib_psw, R.id.create_lib_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_lib_type:
                present.onSelectType(isRally);
                break;
            case R.id.create_lib_check:
                present.onSelectCheck();
                break;
            case R.id.create_lib_psw:
                present.onSelectPsw();
                break;
            case R.id.create_lib_photo:
                ISNav.getInstance().toListActivity(this, ImageSelector.getSingleConfig(), REQUEST_CAMERA_CODE);
                break;
            case R.id.create_lib_create:
                createLib();
            default:
        }
    }


    @Override
    public void onRightClick() {
        super.onRightClick();
        createLib();
    }

    public void createLib() {
        strName = editName.getText().toString().trim();
        strDescribe = editDescribe.getText().toString().trim();
        //需要根据isEdit参数判断当前是创建还是修改
        if (!CheckEmoJi.containsEmoji(strName)&&
                !CheckEmoJi.containsEmoji(strDescribe)&&
                !CheckEmoJi.containsEmoji(strName)){
            present.onSubmit(strType, strName, strCheck, strDescribe, strPsw, strPhoto, catalogId, isEdit);
        }else {
            Toast.makeText(this,"不支持Emoji表情",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            strPhoto = pathList.get(0);
            GlideUtils.load(this, strPhoto, imgPhoto);

            if (isEdit) {
                present.updateAvatar(strPhoto, catalogId);
            }

        }
    }

    @Override
    public void onSuccess(LibCreate result) {
        dismissProgressDialog();
        if (isEdit) {
            showToast("编辑成功");
        } else {
            Intent intent = getIntent();
            intent.putExtra("bean", result.getJsonData());
            setResult(LIB_DATA_REQUEST, intent);
        }

        EventBus.getDefault().post(new LibRefreshEvent());
        finish();
    }

    @Override
    public void onSelectType(String type, String content) {
        strType = type;
        txType.setText(content);
    }

    @Override
    public void onSelectCheck(String type, String content) {
        strCheck = type;
        txCheck.setText(content);
    }

    @Override
    public void onSelectPsw(String type, String content) {
        strPsw = type;
        txPsw.setText(content);

    }

    @Override
    public void onError(String str) {
        showToast(str);
        dismissProgressDialog();
    }

    @Override
    public void start() {
        showProgressDialog("请求中...", true);
    }
}

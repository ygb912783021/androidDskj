package dingshi.com.hibook.ui.library;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import dingshi.com.hibook.Constant;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ILibManagerView;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.lib.LibList;
import dingshi.com.hibook.eventbus.LibDeleteEvent;
import dingshi.com.hibook.eventbus.LibRefreshEvent;
import dingshi.com.hibook.present.LibManagerPresent;
import dingshi.com.hibook.ui.LibZxingActivity;
import dingshi.com.hibook.utils.JoinBitmapUtils;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wangqi
 * @since 2017/12/22 下午2:00
 */


public class LibManagerActivity extends BaseActivity implements ILibManagerView {
    @BindView(R.id.lib_manager_persons)
    TextView txPersons;
    @BindView(R.id.lib_manager_books)
    TextView txBooks;
    @BindView(R.id.lib_manager_shares)
    TextView txShares;
    @BindView(R.id.lib_manager_delete)
    TextView txSubmit;

    //编辑
    @BindView(R.id.lib_manager_edit)
    TextView txEdit;
    //查看
    @BindView(R.id.lib_manager_query)
    TextView txQuery;
    //名单
    @BindView(R.id.lib_manager_menu)
    TextView tvList;

    LibList.JsonDataBean libBean;

    LibManagerPresent present = new LibManagerPresent(this, this);

    boolean isRally;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lib_manager;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        libBean = (LibList.JsonDataBean) getIntent().getSerializableExtra("bean");
        isRally = getIntent().getBooleanExtra("isRally", false);
        requestActionBarStyle(true, libBean.getName(), "", R.drawable.invite_share);

        txPersons.setText("图书成员\n" + libBean.getUser_total() + "人");
        txBooks.setText("藏书量\n" + libBean.getBook_sum() + "本");
        txShares.setText("已共享\n" + libBean.getBook_share_sum() + "本");


        if (isRally) {
            txSubmit.setText("删除书友会");
            txEdit.setText("编辑书友会");
            txQuery.setText("查看书友会");
            tvList.setText("书友会名单");
        } else {
            txEdit.setText("编辑图书馆");
            txSubmit.setText("删除图书馆");
            txQuery.setText("查看图书馆");
            tvList.setText("图书馆名单");

        }

    }

    @OnClick({R.id.lib_manager_query, R.id.lib_manager_menu, R.id.lib_manager_msg,
            R.id.lib_manager_edit, R.id.lib_manager_table, R.id.lib_manager_export,
            R.id.lib_manager_zxing, R.id.lib_manager_setting, R.id.lib_manager_delete
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lib_manager_query:
                finish();
                break;
            case R.id.lib_manager_menu:
                Intent menuIntent = new Intent(this, LibMenuActivity.class);
                menuIntent.putExtra("catalog_id", libBean.getCatalog_id());
                startActivity(menuIntent);
                break;
            case R.id.lib_manager_msg:
                //导出名单
                break;
            case R.id.lib_manager_edit:
                Intent editIntent = new Intent(this, CreateLibActivity.class);
                editIntent.putExtra("bean", libBean);
                editIntent.putExtra("isRally", isRally);
                startActivity(editIntent);
                finish();
                break;
            case R.id.lib_manager_table:

                break;
            case R.id.lib_manager_export:
                present.exportTable(libBean.getCatalog_id());
                break;
            case R.id.lib_manager_zxing:
                //弹出二维码对话框
                Intent zxingIntent = new Intent(this, LibZxingActivity.class);
                zxingIntent.putExtra("libBean", libBean);
                zxingIntent.putExtra("isRally", isRally);
                startActivity(zxingIntent);
                break;
            case R.id.lib_manager_setting:
                //高级设置
                Intent managerIntent = new Intent(this, LibSettingActivity.class);
                managerIntent.putExtra("libBean", libBean);
                startActivity(managerIntent);
                break;
            case R.id.lib_manager_delete:
                present.deleteLib(libBean.getCatalog_id(), isRally ?"确认删除书友会？":"确认删除图书馆？");
                break;
            default:
        }
    }

    @Override
    public void onRightClick() {
        super.onRightClick();

        Flowable.create(new FlowableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(FlowableEmitter<Bitmap> e) throws Exception {
                Bitmap logoBitmap =
                        QRCodeEncoder.syncEncodeQRCode(
                                Constant.getCatalogUrl(user.getJsonData().getUser_id(), libBean.getCatalog_id(), isRally ? 2 : 1, libBean.getBook_sum(), libBean.getBook_share_sum(), libBean.getUser_total()),
                                BGAQRCodeUtil.dp2px(LibManagerActivity.this, 150),
                                Color.BLACK);
                e.onNext(logoBitmap);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        JoinBitmapUtils bitmapUtils = new JoinBitmapUtils();

                        bitmap = bitmapUtils.getLibraryBitmap(LibManagerActivity.this, bitmap, isRally, user.getJsonData().getNick_name());
                        showShareBitmapDialog(bitmap);
                    }
                });
    }

    /**
     * 删除图书馆成功
     */
    @Override
    public void onDeleteLib() {
        finish();
        EventBus.getDefault().post(new LibDeleteEvent());
    }

    /**
     * 导出名单成功
     */
    @Override
    public void onExport() {
        showToast("导出成功");
    }

    @Override
    public void onError(String error) {
        showToast(error);
    }

    /**
     * 刷新图书馆
     */
    @Subscribe
    public void libRefresh(LibRefreshEvent libRefreshEvent) {
        libBean.setUser_total(libBean.getUser_total() - 1);
        txPersons.setText("目录成员\n" + libBean.getUser_total() + "人");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

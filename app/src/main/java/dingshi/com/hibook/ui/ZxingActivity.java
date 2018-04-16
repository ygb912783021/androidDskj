package dingshi.com.hibook.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.action.ICreateBook;
import dingshi.com.hibook.adapter.FuckYouAdapter;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.bean.Book;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.bean.Douban;
import dingshi.com.hibook.eventbus.EventBusHelper;
import dingshi.com.hibook.present.CreateBookPresent;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.FuckDialog;

/**
 * @author wangqi
 * @since 2017/12/11 下午3:46
 */

public class ZxingActivity extends BaseActivity implements QRCodeView.Delegate, ICreateBook {

    @BindView(R.id.zxing_zxing)
    ZXingView zxingView;
    @BindView(R.id.zxing_recycle)
    RecyclerView recyclerView;
    @BindView(R.id.zxing_add_book_label)
    TextView txLabel;

    FuckYouAdapter fuckYouAdapter;
    List<Book> list = new ArrayList<>();

    private String currentIsbn;


    CreateBookPresent present = new CreateBookPresent(this, this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_zxing;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "扫码加书", "使用帮助");
        zxingView.setDelegate(this);
        zxingView.startSpot();

        fuckYouAdapter = new FuckYouAdapter<>(R.layout.view_store_item_centre_item, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(fuckYouAdapter);

        fuckYouAdapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                Book bean = (Book) item;
                ImageView photo = helper.getView(R.id.item_centre_photo);
                GlideUtils.load(ZxingActivity.this, bean.getCover(), photo);
                helper.setText(R.id.item_centre_book, bean.getName());
                helper.setTextColor(R.id.item_centre_book, 0xffffffff);
            }
        });
    }


    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        //扫码到的话就停止扫码
        zxingView.stopSpot();
        currentIsbn = result;
        if (!isExitBook(result)) {
            //请求获取图书信息
            present.getBook(result);

        } else {
            zxingView.startSpot();
            showToast("已添加过该书");
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    /**
     * 判断是否存在该书了
     *
     * @param isbn
     * @return
     */
    public boolean isExitBook(String isbn) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        zxingView.startCamera();
        zxingView.showScanRect();
    }

    @Override
    protected void onStop() {
        zxingView.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        zxingView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    /**
     * 从服务器获取图书
     *
     * @param bookDetails
     */
    @Override
    public void getBook(BookDetails bookDetails) {
        BookDetails.JsonDataBean books = bookDetails.getJsonData();
        Book bean = new Book();
        bean.setIsbn(books.getIsbn());
        bean.setIsbn10(books.getIsbn10());
        bean.setCover(books.getCover());
        bean.setName(books.getName());
        list.add(bean);
        zxingView.startSpot();
        fuckYouAdapter.notifyDataSetChanged();
        txLabel.setText("已扫描" + list.size() + "本");
    }

    /**
     * 从豆瓣获取图书
     *
     * @param douban 0 正确   6000错误
     */
    @Override
    public void getDouban(Douban douban) {
        if (douban != null && douban.getCode() == 0) {
            Book bean = new Book();
            bean.setCover(douban.getImages().getMedium());
            bean.setIsbn(douban.getIsbn13());
            bean.setIsbn10(douban.getIsbn10());
            bean.setName(douban.getTitle());
            bean.setOrigin_name(douban.getOrigin_title());

            String author = "";
            for (int i = 0; i < douban.getAuthor().size(); i++) {
                author += douban.getAuthor().get(i);
            }
            bean.setAuthor(author);

            String trans = "";
            for (int i = 0; i < douban.getTranslator().size(); i++) {
                trans += douban.getTranslator().get(i);
            }
            bean.setTranslator(trans);

            bean.setPress(douban.getPublisher());
            bean.setPublish_time(douban.getPubdate());
            bean.setSummary(douban.getSummary());
            bean.setPrice(douban.getPrice().replace("元", ""));
            bean.setPage_number(douban.getPages());
            bean.setDouban_grade(douban.getRating().getAverage());
            bean.setDouban_grade_number(douban.getRating().getNumRaters());
            list.add(bean);
            fuckYouAdapter.notifyDataSetChanged();
            txLabel.setText("已扫描" + list.size() + "本");
            zxingView.startSpot();
        } else {
            //跳对话框
            View view = LayoutInflater.from(this).inflate(R.layout.zxing_book_dialog, null, false);
            final FuckDialog dialog = new FuckDialog(this).addView(view).builder();
            dialog.show();

            view.findViewById(R.id.zxing_book_dialog_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ZxingActivity.this, CreateBookActivity.class);
                    startActivity(intent);
                    zxingView.startSpot();
                    dialog.dissmis();
                }
            });
            view.findViewById(R.id.zxing_book_dialog_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dissmis();
                    zxingView.startSpot();
                }
            });
        }
    }


    @Override
    public void onRightClick() {
        super.onRightClick();
        showTip();
    }



    public void showTip(){
        dialog = new AlertDialog.Builder(this).setTitle("一键扫码，快速开启").setMessage("在共享图书的自动借书机上都有对应的二维码，只需用手机扫码，便可快速借书")
                .create();
        dialog.show();
    }



    @OnClick({R.id.zxing_add_book})
    public void onClick(View v) {
        if (list.size() > 0) {
            present.addBooks(new Gson().toJson(list));
        } else {
            showToast("当前没有添加书");
        }
    }

    @Override
    public void onLoad() {

    }

    /**
     * 添加成功
     *
     * @param o
     */
    @Override
    public void onSuccess(Object o) {
        showToast("添加成功");
       EventBusHelper.post(new BookList());

        finish();

    }

    /**
     * 失败信息
     *
     * @param error
     */
    @Override
    public void onError(String error) {
        zxingView.startSpot();
        showDialog();
    }


    AlertDialog dialog;

    public void showDialog() {
        dialog = new AlertDialog.Builder(this).setTitle("提示").setMessage("当前没有扫描到该书,是否手动添加")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ZxingActivity.this, CreateBookActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }


    @Override
    public void onEmpty() {

    }
}

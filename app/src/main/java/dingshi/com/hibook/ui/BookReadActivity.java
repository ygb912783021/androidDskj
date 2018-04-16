package dingshi.com.hibook.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseUmengActivity;
import dingshi.com.hibook.read.bean.TxtMsg;
import dingshi.com.hibook.read.interfaces.ILoadListener;
import dingshi.com.hibook.read.main.TxtReaderView;
import dingshi.com.hibook.view.BottomDialog;
import dingshi.com.hibook.view.FuckDialog;

public class BookReadActivity extends Activity implements View.OnClickListener {
//    @BindView(R.id.book_read_chapter)
//    ImageView book_read_chapter;
//    @BindView(R.id.read_title_back)
//    ImageView read_title_back;
    DrawerLayout dl_left;
    TxtReaderView mTxtReaderView;
    LinearLayout line_bottom;
    private ListView lv_left_menu;
    FuckDialog fuckDialog;
    View mStyle1,mStyle2,mStyle3,mStyle4,mStyle5;
    RelativeLayout relative_r;
    private LinearLayout read_sidebar;
    private ArrayList<String > arrayList = new ArrayList<>();
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_read);

        arrayList.add("第一章。。。。。。");
        arrayList.add("第一章。。。。。。");
        arrayList.add("第一章。。。。。。");
        arrayList.add("第一章。。。。。。");
        arrayList.add("第一章。。。。。。");
        arrayList.add("第一章。。。。。。");
        arrayList.add("第一章。。。。。。");
        lv_left_menu = findViewById(R.id.lv_left_menu);
        MySidebarAdapter mySidebarAdapter = new MySidebarAdapter(this);
        lv_left_menu.setAdapter(mySidebarAdapter);
        read_sidebar = findViewById(R.id.read_sidebar);
        dl_left = findViewById(R.id.dl_left);
        mTxtReaderView = (TxtReaderView) findViewById(R.id.activity_hwtxtplay_readerView);
        line_bottom = findViewById(R.id.line_bottom);
        relative_r = findViewById(R.id.relative_r);
        loadOurFile();
        mStyle1 = findViewById(R.id.hwtxtreader_menu_style1);
        mStyle2 = findViewById(R.id.hwtxtreader_menu_style2);
        mStyle3 = findViewById(R.id.hwtxtreader_menu_style3);
        mStyle4 = findViewById(R.id.hwtxtreader_menu_style4);
        mStyle5 = findViewById(R.id.hwtxtreader_menu_style5);
//        mStyle1.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor1), StyleTextColors[0]));
//        mStyle2.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor2), StyleTextColors[1]));
//        mStyle3.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor3), StyleTextColors[2]));
//        mStyle4.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor4), StyleTextColors[3]));
//        mStyle5.setOnClickListener(new StyleChangeClickListener(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor5), StyleTextColors[4]));
    }
    private final int[] StyleTextColors = new int[]{
            Color.parseColor("#4a453a"),
            Color.parseColor("#505550"),
            Color.parseColor("#453e33"),
            Color.parseColor("#8f8e88"),
            Color.parseColor("#27576c")
    };
    BottomDialog bottomDialog;
    public void onClick(View v){
        switch (v.getId()){
            case R.id.read_img_purchase:
                View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_read, null, false);
                fuckDialog = new FuckDialog(this).addView(view).builder();
                fuckDialog.show();
                view.findViewById(R.id.read_give).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fuckDialog.dissmis();
                        goGive();
                    }
                });
                break;
            case R.id.read_title_back:
                finish();
                break;
            case R.id.read_text_fenxiang:
                showShareDialog("12131","313131","31313131");
                break;
            case R.id.book_read_chapter:
                dl_left.openDrawer(GravityCompat.START);
                bottomDialog.dissmis();
                break;
            case R.id.read_view_middle:
                View view1 = LayoutInflater.from(this).inflate(R.layout.view_dialog_bottom, null, false);
                bottomDialog = new BottomDialog(this).addView(view1).builder();
                bottomDialog.show();
                relative_r.setVisibility(View.VISIBLE);
                break;
            case R.id.relative_1:
                line_bottom.setVisibility(View.GONE);
                break;
            case R.id.hwtxtreader_menu_style1:
                mTxtReaderView.setStyle(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor1), StyleTextColors[0]);
                break;
            case R.id.hwtxtreader_menu_style2:
                mTxtReaderView.setStyle(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor2), StyleTextColors[1]);
                break;
            case R.id.hwtxtreader_menu_style3:
                mTxtReaderView.setStyle(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor3), StyleTextColors[2]);
                break;
            case R.id.hwtxtreader_menu_style4:
                mTxtReaderView.setStyle(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor4), StyleTextColors[3]);
                break;
            case R.id.hwtxtreader_menu_style5:
                mTxtReaderView.setStyle(ContextCompat.getColor(this, R.color.hwtxtreader_styleclor5), StyleTextColors[4]);
                break;
            case R.id.read_bottom_jia:
                if (mTxtReaderView.getTextSize()<80) {
                    mTxtReaderView.setTextSize(mTxtReaderView.getTextSize() + 5);
                }
                break;
            case R.id.read_bottom_jian:
                if (mTxtReaderView.getTextSize()>30) {
                    mTxtReaderView.setTextSize(mTxtReaderView.getTextSize() - 5);
                }
                break;
        }
    }
    /**
     * 显示分享dialog
     *
     * @param title
     * @param describe
     * @param url
     */
    FuckDialog shareDialog;

    /**
     * 分享内容
     *
     * @param title
     * @param describe
     * @param url
     */
    public void showShareDialog(final String title, final String describe, final String url) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_bottom_share, null, false);
        shareDialog = new FuckDialog(this).addView(view).builder();
        shareDialog.show();
//        view.findViewById(R.id.dialog_share_wx).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                share(title, describe, url, SHARE_MEDIA.WEIXIN.toSnsPlatform().mPlatform);
//                shareDialog.dissmis();
//            }
//        });
//        view.findViewById(R.id.dialog_share_friend).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                share(title, describe, url, SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform().mPlatform);
//                shareDialog.dissmis();
//            }
//        });

    }

    private void goGive(){
        View viewb = LayoutInflater.from(this).inflate(R.layout.view_item_give, null, false);
        fuckDialog = new FuckDialog(this).addView(viewb).builder();
        fuckDialog.show();
    }
    class MySidebarAdapter extends BaseAdapter{
        Context context;
        MySidebarAdapter(Context context){
            this.context = context;
        }
        @Override
        public int getCount() {
            return arrayList.size()>0?arrayList.size():0;
        }

        @Override
        public Object getItem(int i) {
            return arrayList.size()>0?arrayList.get(i):null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.view_list_item_string, null);
            TextView textView = view.findViewById(R.id.item_list_read);
            textView.setText(arrayList.get(i));
            return view;
        }
    }

    private void loadOurFile() {
        mTxtReaderView.loadTxtFile("/storage/emulated/0/Text.txt", new ILoadListener() {
            @Override
            public void onSuccess() {

                initWhenLoadDone();
            }

            @Override
            public void onFail(TxtMsg txtMsg) {
                //加载失败信息

            }

            @Override
            public void onMessage(String message) {
                //加载过程信息
            }
        });
    }


    private void initWhenLoadDone() {
//        if (mTxtReaderView.getTxtReaderContext().getFileMsg() != null) {
//            FileName = mTxtReaderView.getTxtReaderContext().getFileMsg().FileName;
//        }
//        mMenuHolder.mTextSize.setText(mTxtReaderView.getTextSize() + "");
//        mTopDecoration.setBackgroundColor(mTxtReaderView.getBackgroundColor());
//        mBottomDecoration.setBackgroundColor(mTxtReaderView.getBackgroundColor());
//        //字体初始化
//        onTextSettingUi(mTxtReaderView.getTxtReaderContext().getTxtConfig().Bold);
//        //翻页初始化
//        onPageSwitchSettingUi(mTxtReaderView.getTxtReaderContext().getTxtConfig().SwitchByTranslate);
        if (mTxtReaderView.getTxtReaderContext().getTxtConfig().SwitchByTranslate) {
            mTxtReaderView.setPageSwitchByTranslate();
        } else {
            mTxtReaderView.setPageSwitchByCover();
        }
        //章节初始化
        if (mTxtReaderView.getChapters() != null) {
            WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            m.getDefaultDisplay().getMetrics(metrics);
//            int ViewHeight = metrics.heightPixels - mTopDecoration.getHeight();
//            mChapterListPop = new ChapterList(this, ViewHeight, mTxtReaderView.getChapters(), mTxtReaderView.getTxtReaderContext().getParagraphData().getCharNum());
//            mChapterListPop.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    IChapter chapter = (IChapter) mChapterListPop.getAdapter().getItem(i);
//                    mChapterListPop.dismiss();
//                    mTxtReaderView.loadFromProgress(chapter.getStartParagraphIndex(), 0);
//                }
//            });
//            mChapterListPop.setBackGroundColor(mTxtReaderView.getBackgroundColor());
        }
    }
    private class StyleChangeClickListener implements View.OnClickListener {
        private int BgColor;
        private int TextColor;

        public StyleChangeClickListener(int bgColor, int textColor) {
            BgColor = bgColor;
            TextColor = textColor;
        }

        @Override
        public void onClick(View view) {
            mTxtReaderView.setStyle(BgColor, TextColor);
//            mTopDecoration.setBackgroundColor(BgColor);
//            mBottomDecoration.setBackgroundColor(BgColor);
//            if (mChapterListPop != null) {
//                mChapterListPop.setBackGroundColor(BgColor);
//            }
        }
    }
}

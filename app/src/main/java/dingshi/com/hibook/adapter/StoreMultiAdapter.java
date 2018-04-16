package dingshi.com.hibook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import dingshi.com.hibook.R;
import dingshi.com.hibook.bean.Home;
import dingshi.com.hibook.ui.BookCaseActivity;
import dingshi.com.hibook.ui.BookDetailsActivity;
import dingshi.com.hibook.ui.BookElectronicActivity;
import dingshi.com.hibook.ui.BookListActivity;
import dingshi.com.hibook.ui.Case2BookActivity;
import dingshi.com.hibook.ui.RallyActivity;
import dingshi.com.hibook.ui.WebActivity;
import dingshi.com.hibook.utils.BannerImageLoader;
import dingshi.com.hibook.utils.BannerRoundLoader;
import dingshi.com.hibook.utils.GlideUtils;
import dingshi.com.hibook.view.UpDownTextView;

/**
 * @author wangqi
 *         Created by apple on 2017/10/26.
 */

public class StoreMultiAdapter extends BaseMultiItemQuickAdapter<StoreMultipleItem, BaseViewHolder> {

    private Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public StoreMultiAdapter(Context context, List<StoreMultipleItem> data) {
        super(data);
        this.context = context;
        addItemType(StoreMultipleItem.BOOK_BANNER, R.layout.view_store_item_banner);
        addItemType(StoreMultipleItem.BOOK_NEWS, R.layout.view_store_item_news);
        addItemType(StoreMultipleItem.BOOK_CASE, R.layout.view_store_item_case);
        addItemType(StoreMultipleItem.BOOK_ADVERTISE, R.layout.view_store_item_advertise);
        addItemType(StoreMultipleItem.BOOK_TALENT, R.layout.view_store_item_talent);
        addItemType(StoreMultipleItem.BOOK_CENTRE, R.layout.view_store_item_centre);
        addItemType(StoreMultipleItem.BOOK_SELLING, R.layout.view_store_item_selling);
        addItemType(StoreMultipleItem.BOOK_TASTE, R.layout.view_store_item_taste);
        addItemType(StoreMultipleItem.BOOK_RALLY, R.layout.view_store_item_rally);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreMultipleItem item) {
        switch (item.getItemType()) {
            case StoreMultipleItem.BOOK_BANNER:
                setBookBanner(helper, (ArrayList<Home.JsonDataBean.CarouselBean>) item.getData());
                break;
            case StoreMultipleItem.BOOK_NEWS:
                setBookNews(helper, (ArrayList<Home.JsonDataBean.HeadlineBean>) item.getData());
                break;
            case StoreMultipleItem.BOOK_CASE:
                setBookCase(helper, (ArrayList<Home.JsonDataBean.NearbyCasesBean>) item.getData());
                break;
            case StoreMultipleItem.BOOK_TALENT:
                break;
            case StoreMultipleItem.BOOK_CENTRE:
                setBookCentre(helper, (ArrayList<Home.JsonDataBean.ConcernBooksBean>) item.getData());
                break;
            case StoreMultipleItem.BOOK_SELLING:
                setBookSelling(helper, (ArrayList<Home.JsonDataBean.SellWellBooksBean>) item.getData());
                break;
            case StoreMultipleItem.BOOK_TASTE:
                setBookTaste(helper, (ArrayList<Home.JsonDataBean.RecommendBooksBean>) item.getData());
                break;
            case StoreMultipleItem.BOOK_ADVERTISE:
                setBookAdv(helper, (ArrayList<Home.JsonDataBean.CarouselBean>) item.getData());
                break;
            case StoreMultipleItem.BOOK_RALLY:
                setBookRally(helper);
                break;
            default:
        }
    }


    /**
     * banner滚动
     *
     * @param helper
     * @param data
     */
    private void setBookBanner(BaseViewHolder helper, final ArrayList<Home.JsonDataBean.CarouselBean> data) {
        Banner banner = helper.getView(R.id.item_store_banner);

        banner.setImages(data)
                .setImageLoader(new BannerImageLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("url", data.get(position).getShare_link());
                        intent.putExtra("share_title",data.get(position).getTitle());
                        intent.putExtra("share_content",data.get(position).getChaining());
                        context.startActivity(intent);
                    }
                })
                .start();

    }

    /**
     * 首页滚动新闻
     *
     * @param helper
     * @param data
     */
    private void setBookNews(BaseViewHolder helper, ArrayList<Home.JsonDataBean.HeadlineBean> data) {

        UpDownTextView downTextView = helper.getView(R.id.item_news_title);
        downTextView.setTextList(data);
        downTextView.startAutoScroll();
        downTextView.setOnTextClickListener(new UpDownTextView.OnTextClickListener() {
            @Override
            public void onClick(String url) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
        });

    }

    /**
     * 附近书柜
     *
     * @param helper
     * @param data
     */
    private void setBookCase(BaseViewHolder helper, final ArrayList<Home.JsonDataBean.NearbyCasesBean> data) {
        helper.getView(R.id.item_case_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, BookCaseActivity.class));
            }
        });


        RecyclerView recyclerView = helper.getView(R.id.item_case_recycle);
        FuckYouAdapter adapter = new FuckYouAdapter<Home.JsonDataBean.NearbyCasesBean>(R.layout.view_store_item_case_item, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                Home.JsonDataBean.NearbyCasesBean bean = (Home.JsonDataBean.NearbyCasesBean) item;
                helper.setText(R.id.item_case_address, bean.getAddress());
                helper.setText(R.id.item_case_name, bean.getName());
                helper.setText(R.id.item_case_distance, bean.getRadius() + "km");
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context, Case2BookActivity.class);
                intent.putExtra("radius", data.get(position).getRadius());
                intent.putExtra("serial_number", data.get(position).getSerial_number());
                context.startActivity(intent);
            }
        });
    }

    /**
     * 广告
     *
     * @param helper
     * @param data
     */
    private void setBookAdv(BaseViewHolder helper, final ArrayList<Home.JsonDataBean.CarouselBean> data) {
        Banner banner = helper.getView(R.id.item_adv_banner);
        banner.setImages(data)
                .setImageLoader(new BannerRoundLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("url", data.get(position).getShare_link());
                        context.startActivity(intent);

                    }
                })
                .start();


    }

    /**
     * 书房达人
     */
    private void setBookTalent(BaseViewHolder helper) {
        RecyclerView recyclerView = helper.getView(R.id.item_talent_recycle);
        FuckYouAdapter adapter = new FuckYouAdapter<String>(R.layout.view_store_item_talent_item, Arrays.asList("", "", "", ""));
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                ImageView img = helper.getView(R.id.item_talent_item_photo);
                img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                GlideUtils.loadCircleImage(context, R.drawable.icon_refund, img);
            }
        });
    }

    /**
     * 最受关注的图书
     */
    private void setBookCentre(BaseViewHolder helper, final ArrayList<Home.JsonDataBean.ConcernBooksBean> data) {

        helper.getView(R.id.item_centre_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("is_concern", "0");
                Intent intent = new Intent(context, BookListActivity.class);
                intent.putExtra("map", map);
                context.startActivity(intent);
            }
        });


        RecyclerView recyclerView = helper.getView(R.id.item_centre_recycle);
        FuckYouAdapter adapter = new FuckYouAdapter<Home.JsonDataBean.ConcernBooksBean>(R.layout.view_store_item_centre_item, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                Home.JsonDataBean.ConcernBooksBean bean = (Home.JsonDataBean.ConcernBooksBean) item;
                ImageView photo = helper.getView(R.id.item_centre_photo);
                GlideUtils.load(context, bean.getCover(), photo);
                helper.setText(R.id.item_centre_book, bean.getName());
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("isbn", data.get(position).getIsbn());
                context.startActivity(intent);
            }
        });

    }

    /**
     * 畅销图书榜
     */
    private void setBookSelling(BaseViewHolder helper, final ArrayList<Home.JsonDataBean.SellWellBooksBean> data) {

        helper.getView(R.id.item_selling_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("is_sell_well", "0");
                Intent intent = new Intent(context, BookListActivity.class);
                intent.putExtra("map", map);
                context.startActivity(intent);
            }
        });


        RecyclerView recyclerView = helper.getView(R.id.item_selling_recycle);
        FuckYouAdapter adapter = new FuckYouAdapter<>(R.layout.view_store_item_centre_item, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                Home.JsonDataBean.SellWellBooksBean bean = (Home.JsonDataBean.SellWellBooksBean) item;
                ImageView photo = helper.getView(R.id.item_centre_photo);
                GlideUtils.load(context, bean.getCover(), photo);
                helper.setText(R.id.item_centre_book, bean.getName());
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("isbn", data.get(position).getIsbn());
                context.startActivity(intent);
            }
        });
    }

    /**
     * 推荐、感兴趣的书籍
     *
     * @param helper
     * @param data
     */
    private void setBookTaste(BaseViewHolder helper, final ArrayList<Home.JsonDataBean.RecommendBooksBean> data) {
        RecyclerView recyclerView = helper.getView(R.id.item_taste_recycle);
        FuckYouAdapter adapter = new FuckYouAdapter<>(R.layout.view_list_item, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        recyclerView.setAdapter(adapter);
        adapter.setOnCallBackData(new FuckYouAdapter.OnCallBackData() {
            @Override
            public void convertView(BaseViewHolder helper, Object item) {
                Home.JsonDataBean.RecommendBooksBean bean = (Home.JsonDataBean.RecommendBooksBean) item;
                ImageView photo = helper.getView(R.id.item_list_photo);
                GlideUtils.load(context, bean.getCover(), photo);
                helper.setText(R.id.item_list_book, bean.getName());
                helper.setText(R.id.item_list_author, "作者: " + bean.getAuthor());
                if (bean!=null&&bean.getPress()!=null){
                    if (bean.getPress().contains("null")||bean.getPress()==""){
                        helper.setText(R.id.item_list_concern, "出版社: 暂无");
                    }else {
                        helper.setText(R.id.item_list_concern, "出版社: " + bean.getPress());
                    }
                }
                helper.setText(R.id.item_list_concern_time, "出版时间: " + bean.getPublish_time());

            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("isbn", data.get(position).getIsbn());
                context.startActivity(intent);
            }
        });
    }

    /**
     * 书友会
     *
     * @param helper
     */
    private void setBookRally(BaseViewHolder helper) {
        helper.getView(R.id.view_rally_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpRally(0);
            }
        });
        helper.getView(R.id.view_rally_university).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpRally(1);
            }
        });
        helper.getView(R.id.view_rally_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpRally(2);
            }
        });
        helper.getView(R.id.view_rally_electronicshelf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context,BookElectronicActivity.class));
            }
        });
    }

    /**
     * 跳转书友会
     *
     * @param index
     */
    public void jumpRally(int index) {
        Intent intent = new Intent(context, RallyActivity.class);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }
}

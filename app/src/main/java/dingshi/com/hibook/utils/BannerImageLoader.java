package dingshi.com.hibook.utils;

import android.content.Context;
import android.media.MediaMetadata;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

import dingshi.com.hibook.bean.Home;

/**
 * @author wangqi
 * @since 2017/12/12 13:17
 */

public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object object, ImageView imageView) {

        Home.JsonDataBean.CarouselBean bean= (Home.JsonDataBean.CarouselBean) object;
//        Log.i("TEXT", "displayImage: imageView = "+bean.getChaining());
        GlideUtils.load(context,bean.getFile() ,imageView);

    }
}

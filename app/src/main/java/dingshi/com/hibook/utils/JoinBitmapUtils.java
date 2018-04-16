package dingshi.com.hibook.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import dingshi.com.hibook.R;

/**
 * @author wangqi
 * @since 2018/1/26 17:38
 */

public class JoinBitmapUtils {
    int height = 2308;
    int width = 1440;
    String friendText = "";
    String inviteText = "";


    Bitmap bitmap;

    float phoneHeight;
    float phoneWidth;
    float phoneTop;
    float phoneBottom;


    float roundHeight;
    float roundLeft;
    float roundTop;
    float roundRight;
    float roundBottom;

    float circleRadius;
    float circleLeft;
    float circleTop;

    float zxingHeight;
    float zxingWdith;
    float zxingLeft;
    float zxingRight;
    float zxingTop;

    float offsetY = 50;

    /**
     * 创建  邀请好友的图片
     *
     * @param context
     * @param zxingBitmap
     * @param avatarBitmap
     * @return
     */
    public Bitmap getInviteBitmap(Context context, Bitmap zxingBitmap, Bitmap avatarBitmap, String nick, String inviteCode) {
        this.friendText = nick;
        this.inviteText = inviteCode;

        Log.i("inviteText", inviteText);


        bitmap = createBitmap(context, zxingBitmap, avatarBitmap);
        return bitmap;
    }

    /**
     * 创建卡片的图片
     *
     * @param context
     * @param zxingBitmap
     * @param avatarPhoto
     * @param mobile
     * @param nick
     * @return
     */
    public Bitmap getCardBitmap(Context context, Bitmap zxingBitmap, Bitmap avatarPhoto, String mobile, String nick) {
        this.friendText = mobile;
        this.inviteText = nick;
        bitmap = createCardBitmap(context, zxingBitmap, avatarPhoto);
        return bitmap;
    }

    /**
     * 获取图书馆图片
     *
     * @param context
     * @param zxingBitmap
     * @param nick_name
     * @return
     */
    public Bitmap getLibraryBitmap(Context context, Bitmap zxingBitmap, boolean isRally, String nick_name) {
        height = 1920;
        width = 1080;

        if (isRally) {
            bitmap = createRallyBitmap(context, zxingBitmap, nick_name);
        } else {
            bitmap = createLibraryBitmap(context, zxingBitmap);
        }
        return bitmap;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Bitmap createBitmap(Context context, Bitmap zxingBitmap, Bitmap avatarBitmap) {

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Bitmap bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.share_bitmap_bg);
        bg = setImgSize(bg, width, height);
        canvas.drawBitmap(bg, 0, 0, null);


        Bitmap phone = BitmapFactory.decodeResource(context.getResources(), R.drawable.share_bitmap_phone);
        phone = setImgSize(phone, width / 2, height / 3);
        phoneHeight = phone.getHeight();
        phoneWidth = phone.getWidth();
        phoneTop = height / 2 - phoneHeight + offsetY * 3;
        phoneBottom = phoneTop + phoneHeight;


        canvas.drawBitmap(phone, width / 2 - phoneWidth / 2, phoneTop, null);


        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(height / 25);


        TextPaint textPatin = new TextPaint();
        textPatin.setColor(Color.BLACK);
        textPatin.setTextSize(height / 30);
        textPatin.setTypeface(Typeface.SANS_SERIF);


        if (friendText.length() > 3) {
            friendText = friendText.substring(0, 3) + "..";
        }
        String text2 = "找到1000个走心朋友\n您的好友" + friendText + ",邀请你一起读书";
        StaticLayout staticLayout = new StaticLayout(text2, textPatin, width, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true);
        canvas.save();
        canvas.translate(0, phoneTop - getTextHeight(textPatin) * 2.5f);
        staticLayout.draw(canvas);
        canvas.restore();


        paint.setColor(0xffed552b);
        roundHeight = height / 8;
        roundLeft = width / 6;
        roundTop = phoneBottom + offsetY;
        roundRight = width - roundLeft;
        roundBottom = roundTop + roundHeight;
        canvas.drawRoundRect(roundLeft, roundTop, roundRight, roundBottom, roundHeight, roundHeight, paint);


        circleRadius = roundHeight / 3;
        circleLeft = width / 3.5f;
        circleTop = roundTop + roundHeight / 2;


        paint.setColor(Color.WHITE);
        paint.setTextSize(height / 35);
        canvas.drawText("使用我的邀请码", circleLeft + circleRadius * 1.5f, circleTop - circleRadius / 2, paint);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        paint.setTextSize(height / 20);
        canvas.drawText(inviteText, circleLeft + circleRadius * 1.5f, circleTop + circleRadius, paint);


        textPatin.setColor(Color.RED);
        textPatin.setTextSize(height / 40);

        text2 = "输入邀请码，你和我都将获得一本书";
        staticLayout = new StaticLayout(text2, textPatin, width, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true);
        canvas.save();
        canvas.translate(0, roundBottom + offsetY / 2);
        staticLayout.draw(canvas);
        canvas.restore();


        zxingBitmap = setImgSize(zxingBitmap, width / 4.5f, height / 7);
        zxingWdith = zxingBitmap.getWidth();
        zxingHeight = zxingBitmap.getHeight();
        zxingLeft = zxingWdith / 4;
        zxingTop = height - zxingHeight - zxingHeight / 4;
        zxingRight = zxingLeft + zxingWdith;
        canvas.drawBitmap(zxingBitmap, zxingLeft, zxingTop, null);


        textPatin.setColor(Color.BLACK);
        textPatin.setTextSize(height / 38);

        text2 = "长按识别二维码 下载APP\n疯狂读书 一个读书人走心聚焦的圈子";
        staticLayout = new StaticLayout(text2, textPatin, width, Layout.Alignment.ALIGN_NORMAL, 1f, 40f, true);
        canvas.save();
        canvas.translate(zxingRight + offsetY, zxingTop + zxingHeight / 6);
        staticLayout.draw(canvas);
        canvas.restore();


        canvas.save();
        canvas.translate(circleLeft - circleRadius, circleTop - circleRadius);

        avatarBitmap = setImgSize(avatarBitmap, width / 7, height / 12);

        BitmapShader bitmapShader = new BitmapShader(avatarBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);
        float mScale = (circleRadius * 2.0f) / Math.min(avatarBitmap.getHeight(), avatarBitmap.getWidth());
        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
        bitmapShader.setLocalMatrix(matrix);
        canvas.drawCircle(circleRadius, circleRadius, circleRadius, paint);

        canvas.restore();


        return bitmap;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Bitmap createCardBitmap(Context context, Bitmap zxingBitmap, Bitmap avatarBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Bitmap bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.share_card_bg);
        bg = setImgSize(bg, width, height);
        canvas.drawBitmap(bg, 0, 0, null);


        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        roundHeight = height / 5.3f;
        roundTop = height / 2 - roundHeight / 2;
        roundBottom = roundTop + roundHeight;
        roundLeft = width / 7;
        roundRight = width - roundLeft;

        canvas.drawRoundRect(roundLeft, roundTop, roundRight, roundBottom, 30, 30, paint);


        float cicleRadius = roundHeight / 3f;

        canvas.drawCircle(width / 2, roundTop, cicleRadius, paint);


        float photosRadius = roundHeight / 4;


        canvas.save();

        canvas.translate(width / 2 - photosRadius, roundTop - photosRadius);

        BitmapShader bitmapShader = new BitmapShader(avatarBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);
        float mScale = (photosRadius * 2.0f) / Math.min(avatarBitmap.getHeight(), avatarBitmap.getWidth());
        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
        bitmapShader.setLocalMatrix(matrix);
        canvas.drawCircle(photosRadius, photosRadius, photosRadius, paint);

        canvas.restore();


        TextPaint textPatin = new TextPaint();
        textPatin.setColor(Color.BLACK);
        textPatin.setTextSize(height / 40);
        textPatin.setTypeface(Typeface.SANS_SERIF);

        friendText = friendText + "\n我是" + inviteText + "，想与你相识";
        StaticLayout staticLayout = new StaticLayout(friendText, textPatin, width, Layout.Alignment.ALIGN_CENTER, 1f, 40f, true);
        canvas.save();
        canvas.translate(0, roundTop + photosRadius * 2);
        staticLayout.draw(canvas);
        canvas.restore();


        float zxingWidth;

        float zixngTop = roundBottom + photosRadius / 2;
        float zxingBottom;


        zxingBitmap = setImgSize(zxingBitmap, width / 4.5f, height / 7f);
        zxingWidth = zxingBitmap.getWidth();
        zxingBottom = zixngTop + zxingBitmap.getHeight();
        canvas.drawBitmap(zxingBitmap, width / 2 - zxingWidth / 2, zixngTop, null);


        float logoTop = zxingBottom + photosRadius / 2;

        float logoBottom;

        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        logoBitmap = setImgSize(logoBitmap, width / 11, height / 18);

        logoBottom = logoTop + logoBitmap.getHeight();

        canvas.drawBitmap(logoBitmap, width / 2 - logoBitmap.getWidth() - 50, logoTop, null);


        paint.setTextSize(height / 35);
//        paint.setColor(Color.BLACK);
        canvas.drawText("疯狂图书", width / 2 + 10, logoTop + logoBitmap.getHeight() / 1.5f, paint);


        String text = "长按识别获得电话 立即与我沟通\n疯狂图书 一个读书人走心聚焦的圈子";
        staticLayout = new StaticLayout(text, textPatin, (int) width, Layout.Alignment.ALIGN_CENTER, 1f, 40f, true);
        canvas.save();
        canvas.translate(0, logoBottom + photosRadius / 2);
        staticLayout.draw(canvas);
        canvas.restore();


        textPatin.setTypeface(Typeface.SERIF);
        textPatin.setTextSize(height / 20);
        text = "找到1000个走心朋友";
        staticLayout = new StaticLayout(text, textPatin, width, Layout.Alignment.ALIGN_CENTER, 1f, 40f, true);
        canvas.save();
        canvas.translate(0, roundTop - cicleRadius - getTextHeight(textPatin) * 3 / 2);
        staticLayout.draw(canvas);
        canvas.restore();
        return bitmap;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Bitmap createLibraryBitmap(Context context, Bitmap zxingBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Bitmap bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.library_bitmap_bg);
        bg = setImgSize(bg, width, height);
        canvas.drawBitmap(bg, 0, 0, null);

        zxingBitmap = setImgSize(zxingBitmap, width / 2.6f, height / 4.26f);
        canvas.drawBitmap(zxingBitmap, width / 2 - zxingBitmap.getWidth() / 2, 470, null);

        return bitmap;

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Bitmap createRallyBitmap(Context context, Bitmap zxingBitmap, String nick) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Bitmap bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.rally_bitmap_bg);
        bg = setImgSize(bg, width, height);
        canvas.drawBitmap(bg, 0, 0, null);

        zxingBitmap = setImgSize(zxingBitmap, width / 2.85f, height / 4.95f);
        canvas.drawBitmap(zxingBitmap, width / 2 - zxingBitmap.getWidth() / 2, 515, null);

        if (nick.length() > 3) {
            nick = nick.substring(0, 4) + "..";
        }
        nick = "您的好友" + nick + "，邀请您加入书友会";

        TextPaint textPatin = new TextPaint();
        textPatin.setColor(Color.BLACK);
        textPatin.setTextSize(height / 50);
        textPatin.setTypeface(Typeface.SANS_SERIF);
        StaticLayout staticLayout = new StaticLayout(nick, textPatin, width, Layout.Alignment.ALIGN_CENTER, 1f, 40f, true);

        canvas.save();
        canvas.translate(0, 515 - getTextHeight(textPatin) * 3);
        staticLayout.draw(canvas);
        canvas.restore();

        return bitmap;

    }


    public Bitmap setImgSize(Bitmap bm, float newWidth, float newHeight) {
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeight / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }


    public float getTextHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) (Math.ceil(fm.descent - fm.ascent) + 2);
    }
}

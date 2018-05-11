package dingshi.com.hibook.read.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;


import java.util.List;

import dingshi.com.hibook.read.bean.EnChar;
import dingshi.com.hibook.read.bean.NumChar;
import dingshi.com.hibook.read.bean.TxtChar;
import dingshi.com.hibook.read.interfaces.IPage;
import dingshi.com.hibook.read.interfaces.ITxtLine;
import dingshi.com.hibook.read.main.PageParam;
import dingshi.com.hibook.read.main.PaintContext;
import dingshi.com.hibook.read.main.TxtConfig;

/**
 * Created by bifan-wei
 * on 2017/11/27.
 */

public class TxtBitmapUtil {
    public static Bitmap createHorizontalPage(Bitmap bg, PaintContext paintContext, PageParam pageParam, TxtConfig txtConfig, IPage page) {
        if (page == null || !page.HasData()) {
            return null;
        }
        Bitmap bitmap = bg.copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bitmap);
        List<ITxtLine> lines = page.getLines();
        int textHeight = txtConfig.textSize;
        int lineHeight = textHeight + pageParam.LinePadding;
        int topL = (int) (pageParam.PaddingLeft + pageParam.TextPadding) + 3;
        int bottom = pageParam.PaddingTop + textHeight;
        int bomL = bottom;
        float CharPadding = pageParam.TextPadding;
        Paint paint = paintContext.textPaint;
        int defaultColor = txtConfig.textColor;
        float x = topL;
        float y = bottom;

        for (ITxtLine line : lines) {
            if (line.HasData()) {
                for (TxtChar txtChar : line.getTxtChars()) {
                    if (txtChar instanceof NumChar || txtChar instanceof EnChar) {
                        paint.setColor(txtChar.getTextColor());
                    } else {
                        paint.setColor(defaultColor);
                    }
                    canvas.drawText(txtChar.getValueStr(), x, y, paint);
                    txtChar.Left = (int) x;
                    txtChar.Right = (int) (x + txtChar.CharWidth);
                    txtChar.Bottom = (int) y + 5;
                    txtChar.Top = txtChar.Bottom - textHeight;
                    x = txtChar.Right + CharPadding;
                }

                x = topL;
                y = y + lineHeight;
            }
        }

        return bitmap;
    }

    public static Bitmap CreateBitmap(int bitmapStyleColor, int bitmapWidth, int bitmapHeight) {
        int[] BitmapColor = getBitmapColor(bitmapStyleColor, bitmapWidth, bitmapHeight);
        return Bitmap.createBitmap(BitmapColor, bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
    }

    public static Bitmap CreateBitmap(Resources res, int backgroundResource, int bitmapWidth, int bitmapHeight) {
        Bitmap bgBitmap = BitmapFactory.decodeResource(res, backgroundResource);
        int width = bgBitmap.getWidth();
        int height = bgBitmap.getHeight();
        int[] color = new int[width * height];
        for (int y = 0; y < height; y++) {// use of x,y is legible then // the
            for (int x = 0; x < width; x++) {
                color[y * width + x] = bgBitmap.getPixel(x, y);// the shift
            }
        }
        int[] colors = new int[bitmapWidth * bitmapHeight];
        for (int y = 0, size = bitmapWidth * bitmapHeight, border = width * height, index = 0; y < size; y++) {
            if (index == border) {
                index = 0;
            }
            colors[y] = color[index];
            index++;
        }
        return Bitmap.createBitmap(colors, bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
    }

    private static int[] getBitmapColor(int color, int with, int height) {
        int[] colors = new int[with * height];
        int STRIDE = height;
        int c = color;
        for (int y = 0; y < with; y++) {// use of x,y is legible then // the //
            for (int x = 0; x < height; x++) {
                colors[y * STRIDE + x] = c;// the shift operation generates
            }
        }
        return colors;
    }


    public int[] getImagePixel(Resources res, int drawable) {
        Bitmap bi = BitmapFactory.decodeResource(res, drawable);
        int with = bi.getWidth();
        int height = bi.getHeight();
        int[] colors = new int[with * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < with; j++) {
                int pixel = bi.getPixel(i, j); // 下面三行代码将一个数字转换为RGB数字
                colors[i * with + j] = pixel;
            }
        }
        return colors;
    }
}

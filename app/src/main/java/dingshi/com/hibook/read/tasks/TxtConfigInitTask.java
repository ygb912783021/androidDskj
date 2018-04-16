package dingshi.com.hibook.read.tasks;

import android.graphics.Paint;

import dingshi.com.hibook.read.interfaces.ILoadListener;
import dingshi.com.hibook.read.interfaces.ITxtTask;
import dingshi.com.hibook.read.main.PageParam;
import dingshi.com.hibook.read.main.PaintContext;
import dingshi.com.hibook.read.main.TxtConfig;
import dingshi.com.hibook.read.main.TxtReaderContext;
import dingshi.com.hibook.read.utils.ELogger;
import dingshi.com.hibook.read.utils.TxtBitmapUtil;


/**
 * Created by HP on 2017/11/26.
 */

public class TxtConfigInitTask implements ITxtTask {
    private String atg = "TxtConfigInitTask";

    @Override
    public void Run(ILoadListener callBack, TxtReaderContext readerContext) {
        ELogger.log(atg, "do TxtConfigInit");
        callBack.onMessage("start init settings in TxtConfigInitTask");

        TxtConfig config = readerContext.getTxtConfig();
        initTxtConfig(readerContext, config);

        //if not null ,do recycle
        if (readerContext.getBitmapData().getBgBitmap() != null) {
            readerContext.getBitmapData().getBgBitmap().recycle();
        }

        int width = readerContext.getPageParam().PageWidth;
        int height = readerContext.getPageParam().PageHeight;

        //init the bg bitmap
        readerContext.getBitmapData().setBgBitmap(TxtBitmapUtil.CreateBitmap(config.backgroundColor, width, height));
        //initPageParam
        initPageParam(readerContext);
        //start draw prepare

        int startParagraphIndex = 0;
        int startCharIndex = 0;
        if (readerContext.getFileMsg() != null) {
            startParagraphIndex = readerContext.getFileMsg().PreParagraphIndex;
            startCharIndex = readerContext.getFileMsg().PreCharIndex;
        }
        //初始化一下之前的设置
        initPainContext(readerContext.getPaintContext(), readerContext.getTxtConfig());

        ITxtTask txtTask = (ITxtTask) new TxtPageLoadTask(startParagraphIndex, startCharIndex);
        txtTask.Run(callBack, readerContext);
    }

    /**
     * 初始化获取保存的或者默认的设置
     *
     * @param readerContext
     * @param config
     */
    private void initTxtConfig(TxtReaderContext readerContext, TxtConfig config) {
        config.showNote = TxtConfig.getIsShowNote(readerContext.context);
        config.canPressSelect = TxtConfig.getCanPressSelect(readerContext.context);
        config.textColor = TxtConfig.getTextColor(readerContext.context);
        config.textSize = TxtConfig.getTextSize(readerContext.context);
        config.backgroundColor = TxtConfig.getBackgroundColor(readerContext.context);
        config.NoteColor = TxtConfig.getNoteTextColor(readerContext.context);
        config.SelectTextColor = TxtConfig.getSelectTextColor(readerContext.context);
        config.SliderColor = TxtConfig.getSliderColor(readerContext.context);
        config.Bold = TxtConfig.isBold(readerContext.context);
    }

    /**
     * @param paintContext
     * @param txtConfig
     */
    public static void initPainContext(PaintContext paintContext, TxtConfig txtConfig) {
        paintContext.textPaint.setTextSize(txtConfig.textSize);
        paintContext.textPaint.setFakeBoldText(txtConfig.Bold);
        paintContext.textPaint.setTextAlign(Paint.Align.LEFT);
        paintContext.textPaint.setColor(txtConfig.textColor);
        paintContext.notePaint.setTextSize(txtConfig.textSize);
        paintContext.notePaint.setColor(txtConfig.NoteColor);
        paintContext.notePaint.setTextAlign(Paint.Align.LEFT);
        paintContext.selectTextPaint.setTextSize(txtConfig.textSize);
        paintContext.selectTextPaint.setColor(txtConfig.SelectTextColor);
        paintContext.selectTextPaint.setTextAlign(Paint.Align.LEFT);
        paintContext.sliderPaint.setColor(txtConfig.SliderColor);
        paintContext.sliderPaint.setAntiAlias(true);
        paintContext.textPaint.setFakeBoldText(txtConfig.Bold);
    }

    private void initPageParam(TxtReaderContext readerContext) {
        PageParam param = readerContext.getPageParam();
        param.LineWidth = param.PageWidth - param.PaddingLeft - param.PaddingRight;
        int lineHeight = readerContext.getTxtConfig().textSize + param.LinePadding;
        param.PageLineNum = (param.PageHeight - param.PaddingTop - param.PaddingBottom + param.LinePadding) / lineHeight;
    }


}

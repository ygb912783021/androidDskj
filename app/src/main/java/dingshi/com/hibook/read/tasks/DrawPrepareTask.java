package dingshi.com.hibook.read.tasks;

import android.graphics.Color;

import dingshi.com.hibook.read.interfaces.ILoadListener;
import dingshi.com.hibook.read.interfaces.ITxtTask;
import dingshi.com.hibook.read.main.PaintContext;
import dingshi.com.hibook.read.main.TxtConfig;
import dingshi.com.hibook.read.main.TxtReaderContext;
import dingshi.com.hibook.read.utils.ELogger;

/**
 * Created by bifan-wei
 * on 2017/11/27.
 */

public class DrawPrepareTask implements ITxtTask {
    private String tag = "DrawPrepareTask";

    @Override
    public void Run(ILoadListener callBack, TxtReaderContext readerContext) {
        callBack.onMessage("start do DrawPrepare");
        ELogger.log(tag, "do DrawPrepare");
        initPainContext(readerContext.getPaintContext(), readerContext.getTxtConfig());
        readerContext.getPaintContext().textPaint.setColor(Color.WHITE);
        ITxtTask txtTask = new BitmapProduceTask();
        txtTask.Run(callBack, readerContext);

    }

    private void initPainContext(PaintContext paintContext, TxtConfig txtConfig) {
        TxtConfigInitTask.initPainContext(paintContext, txtConfig);
    }
}

package dingshi.com.hibook.read.tasks;


import java.util.ArrayList;
import java.util.List;

import dingshi.com.hibook.read.interfaces.IChapter;
import dingshi.com.hibook.read.interfaces.ILoadListener;
import dingshi.com.hibook.read.interfaces.IParagraphData;
import dingshi.com.hibook.read.interfaces.ITxtTask;
import dingshi.com.hibook.read.main.ParagraphData;
import dingshi.com.hibook.read.main.TxtReaderContext;
import dingshi.com.hibook.read.utils.ELogger;

/**
 * Created by bifan-wei
 * on 2018/1/28.
 */

public class TextLoader  {
    private String tag = "FileDataLoadTask";
    public void load(String text, TxtReaderContext readerContext, ILoadListener callBack) {
        IParagraphData paragraphData = new ParagraphData();
        List<IChapter> chapter = new ArrayList<>();
        callBack.onMessage("start read text");
        ELogger.log(tag, "start read text");
        paragraphData.addParagraph(text + "");
        readerContext.setParagraphData(paragraphData);
        readerContext.setChapters(chapter);
        ITxtTask txtTask = new TxtConfigInitTask();
        txtTask.Run(callBack, readerContext);
    }
}

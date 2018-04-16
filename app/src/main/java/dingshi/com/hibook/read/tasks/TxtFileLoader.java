package dingshi.com.hibook.read.tasks;


import java.io.File;

import dingshi.com.hibook.read.bean.TxtFileMsg;
import dingshi.com.hibook.read.bean.TxtMsg;
import dingshi.com.hibook.read.interfaces.ILoadListener;
import dingshi.com.hibook.read.interfaces.ITxtTask;
import dingshi.com.hibook.read.main.TxtReaderContext;
import dingshi.com.hibook.read.utils.ELogger;
import dingshi.com.hibook.read.utils.FileUtil;


/**
 * created by ： bifan-wei
 */

public class TxtFileLoader {
    private String tag = "TxtFileLoader";

    public void load(String filePath, TxtReaderContext readerContext, ILoadListener loadListener) {
        load(filePath, null, readerContext, loadListener);
    }

    public void load(String filePath, String fileName, TxtReaderContext readerContext, ILoadListener loadListener) {
        if (!FileUtil.FileExist(filePath)) {
            loadListener.onFail(TxtMsg.FileNoExist);
            return;
        }
        loadListener.onMessage("initFile start");
        initFile(filePath, fileName, readerContext);
        ELogger.log(tag, "initFile done");
        loadListener.onMessage("initFile done");
        ITxtTask txtTask = new FileDataLoadTask();
        txtTask.Run(loadListener, readerContext);

    }

    private void initFile(String filePath, String fileName, TxtReaderContext readerContext) {
        File file = new File(filePath);
        TxtFileMsg fileMsg = new TxtFileMsg();
        fileMsg.FileSize = file.getTotalSpace();
        fileMsg.FilePath = filePath;
        fileMsg.FileCode = FileUtil.getCharset(filePath);

        fileMsg.CurrentParagraphIndex = 0;
        fileMsg.CurrentParagraphIndex = 0;
        fileMsg.PreParagraphIndex = 0;
        fileMsg.PreCharIndex = 0;
        if (fileName == null||fileName.trim().length()==0) {
            fileName = file.getName();
        }
        fileMsg.FileName = fileName;
        readerContext.setFileMsg(fileMsg);
    }
}

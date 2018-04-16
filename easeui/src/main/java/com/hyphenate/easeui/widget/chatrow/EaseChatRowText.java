package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.utils.EaseSmileUtils;

public class EaseChatRowText extends EaseChatRow {

    private TextView contentView;
    private TextView txAgree;
    private TextView txUnagree;
    private LinearLayout agreeLayout;
    private LinearLayout msgBackGroud;

    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
    }

    @Override
    protected void onFindViewById() {
        contentView = findViewById(R.id.tv_chatcontent);
        txAgree = findViewById(R.id.tv_agree);
        txUnagree = findViewById(R.id.tv_unagree);
        agreeLayout = findViewById(R.id.agree_layout);
        msgBackGroud = findViewById(R.id.bubble);

    }

    /**
     * 订单id
     */
    String order;
    /**
     * 图书
     */
    String book;
    /**
     * 图书isbn
     */
    String isbn;
    /**
     * 确认借书类型
     */
    String borrow;
    /**
     * 是否是接受者
     */
    boolean isReceive;


    @Override
    public void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);
        //如果当前消息是接收消息

        isReceive = message.direct() == EMMessage.Direct.RECEIVE;

        borrow = message.getStringAttribute("borrow", "");

        //订单  1、android   10、ios 发送-->你好，我刚刚借了你的《XXXX》这本书，赶快同意呗！Yes/No
        if (borrow.equals("1") || borrow.equals("10")) {
            book = message.getStringAttribute("book", "");
            isbn = message.getStringAttribute("isbn", "");
            order = message.getStringAttribute("order", "");
            contentView.setText("你好，我刚刚借了你的《" + book + "》这本书，赶快同意呗");

            if (isReceive) {
                agreeLayout.setVisibility(View.VISIBLE);
                msgBackGroud.setBackgroundResource(R.drawable.msg_agree);
            } else {
                agreeLayout.setVisibility(View.GONE);
                msgBackGroud.setBackgroundResource(R.drawable.ease_chatto_bg);
            }
//            小主，您好！《" + book + "》这本书的主人，已经同意了您的借书。赶快跟Ta私聊，取书吧！
        } else if (borrow.equals("1.5") || borrow.equals("15")) {
            book = message.getStringAttribute("book", "");
            agreeLayout.setVisibility(View.GONE);
            if (isReceive) {
                msgBackGroud.setBackgroundResource(R.drawable.msg_agree);
            } else {
                contentView.setText("已同意");
                msgBackGroud.setBackgroundResource(R.drawable.ease_chatto_bg);
            }
        } else if (borrow.equals("2") || borrow.equals("20")) {
            //借书点击不同意时  发送-->小主，您好！《XXXX》这本书的主人，残忍的拒绝了您的借书请求！是否更换藏书人继续借书？
            book = message.getStringAttribute("book", "");
            isbn = message.getStringAttribute("isbn", "");
            order = message.getStringAttribute("order", "");
            if (isReceive) {

                agreeLayout.setVisibility(View.VISIBLE);
                msgBackGroud.setBackgroundResource(R.drawable.msg_agree);
            } else {
                contentView.setText("不同意借书");
                agreeLayout.setVisibility(View.GONE);
                msgBackGroud.setBackgroundResource(R.drawable.ease_chatto_bg);

            }
            //继续借书   点击跳转图书详情
        } else if (borrow.equals("2.5") || borrow.equals("25")) {
            agreeLayout.setVisibility(View.GONE);
            if (isReceive) {
                msgBackGroud.setBackgroundResource(R.drawable.msg_agree);
            } else {
                msgBackGroud.setBackgroundResource(R.drawable.ease_chatto_bg);
            }

        } else if (borrow.equals("4") || borrow.equals("40")) {
//

        } else if (borrow.equals("100") || borrow.equals("1000")) {
            //还书流程
            book = message.getStringAttribute("book", "");
            order = message.getStringAttribute("order", "");

            if (isReceive) {
                agreeLayout.setVisibility(View.VISIBLE);
                txUnagree.setVisibility(View.GONE);
                contentView.setText("这本书我已经看完了，请确认我已将这本《" + book + "》还给您");
                msgBackGroud.setBackgroundResource(R.drawable.msg_agree);
            } else {
                agreeLayout.setVisibility(View.GONE);
                msgBackGroud.setBackgroundResource(R.drawable.ease_chatto_bg);
            }
        } else if (borrow.equals("101") || borrow.equals("1001")) {
            order = message.getStringAttribute("order", "");
            book = message.getStringAttribute("book", "");
            agreeLayout.setVisibility(View.GONE);
            if (isReceive) {
                contentView.setText("小主您好，藏书人" + message.getStringAttribute("nick", "hibook") + "，已经确定《" + book + "》这本书，已经归还！");
                msgBackGroud.setBackgroundResource(R.drawable.msg_agree);
            } else {
                msgBackGroud.setBackgroundResource(R.drawable.ease_chatto_bg);
            }

        } else if (borrow.equals("600") || borrow.equals("6000")) {
            order = message.getStringAttribute("apply_id", "");
            if (isReceive) {
                msgBackGroud.setBackgroundResource(R.drawable.msg_agree);
                agreeLayout.setVisibility(View.VISIBLE);
            } else {
                agreeLayout.setVisibility(View.GONE);
            }
        } else {
            agreeLayout.setVisibility(View.GONE);
            if (isReceive) {
                msgBackGroud.setBackgroundResource(R.drawable.ease_chatfrom_bg);
            } else {
                msgBackGroud.setBackgroundResource(R.drawable.ease_chatto_bg);
            }
        }

        txAgree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                agree();
            }
        });
        txUnagree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unagree();
            }
        });

    }


    public void agree() {
        Log.i("agree", String.format("--%s--%s--%s--%s--%s--%s", borrow, order, isbn, book, isReceive, message.getFrom()));
        itemClickListener.onUserAgree(borrow, order, isbn, book, isReceive, message.getFrom());
    }

    public void unagree() {
        Log.i("unagree", String.format("--%s--%s--%s--%s--%s--%s", borrow, order, isbn, book, isReceive, message.getFrom()));
        itemClickListener.onUSerUnagree(borrow, order, isbn, book, isReceive, message.getFrom());
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        switch (msg.status()) {
            case CREATE:
                onMessageCreate();
                break;
            case SUCCESS:
                onMessageSuccess();
                break;
            case FAIL:
                onMessageError();
                break;
            case INPROGRESS:
                onMessageInProgress();
                break;
            default:
        }
    }

    private void onMessageCreate() {
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }

    private void onMessageSuccess() {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.GONE);
    }

    private void onMessageError() {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.VISIBLE);
    }

    private void onMessageInProgress() {
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }
}

package dingshi.com.hibook.hx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.PathUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.retrofit.exception.ServerException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.ui.BookDetailsActivity;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.SpUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
/**
 * @author lenovo
 */
public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper {

    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_RECALL = 9;


    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        super.setUpView();
        // set click listener
        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (EasyUtils.isSingleActivity(getActivity())) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
                onBackPressed();
            }
        });
//        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
//        //extend menu items
//        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
//        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
//        if(chatType == Constant.CHATTYPE_SINGLE){
//            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
//            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
//        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 设置消息的扩展
     */
    @Override
    public void onSetMessageAttributes(EMMessage message) {
        User user = SpUtils.getUser();
        if (user != null) {
            message.setAttribute("nick", user.getJsonData().getNick_name());
            message.setAttribute("avatar", user.getJsonData().getAvatar());
            message.setAttribute("userid", user.getJsonData().getUser_id());
        }
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }


    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar
//        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
        //用户头像跳转
//        Toast.makeText(getActivity(), "头像" + username, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onUserAgree(String type, String order, String isbn, String book, boolean isReceive, String toUserId) {
        Log.i("onUserAgree", String.format("--%s--%s--%s--%s--%s--%s", type, order, isbn, book, isReceive, toUserId));

        //如果用户同意借书
        if (type.equals("1") || type.equals("10")) {
            //改
            EMMessage message = EMMessage.createTxtSendMessage("小主，您好！《" + book + "》这本书的主人，已经同意了您的借书。赶快去我的借阅里取书吧！", toUserId);
            message.setAttribute("borrow", "1.5");
            message.setAttribute("book", book);
            message.setAttribute("isbn", isbn);
            message.setAttribute("nick", SpUtils.getUser().getJsonData().getNick_name());
            message.setAttribute("avatar", SpUtils.getUser().getJsonData().getAvatar());
            message.setAttribute("userid", SpUtils.getUser().getJsonData().getUser_id());
//            EMClient.getInstance().chatManager().sendMessage(message);

            Log.i("order", "agree---" + order);

            agree(toUserId, order, "1", message);


        } else if (type.equals("2") || type.equals("20")) {

            //如果用户不同意借书，但是却同意再次借阅，我们需要跳转到图书详情页面
            Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
            intent.putExtra("isbn", isbn);
            startActivity(intent);

        } else if (type.equals("100") || type.equals("1000")) {

            //同意还书   请求告诉服务器同意还书
            EMMessage message = EMMessage.createTxtSendMessage("已确认", toUserId);
            message.setAttribute("borrow", "101");
            message.setAttribute("order", order);
            message.setAttribute("book", book);
            message.setAttribute("isbn", isbn);
            message.setAttribute("nick", SpUtils.getUser().getJsonData().getNick_name());
            message.setAttribute("avatar", SpUtils.getUser().getJsonData().getAvatar());
            message.setAttribute("userid", SpUtils.getUser().getJsonData().getUser_id());
            backBook(toUserId, order, message);

        } else if (type.equals("600") || type.equals("6000")) {
            //同意交换名片   order为名片的id
            EMMessage message = EMMessage.createTxtSendMessage("同意交换名片", toUserId);
            message.setAttribute("nick", SpUtils.getUser().getJsonData().getNick_name());
            message.setAttribute("avatar", SpUtils.getUser().getJsonData().getAvatar());
            message.setAttribute("userid", SpUtils.getUser().getJsonData().getUser_id());

            agreeSweep(toUserId, order, "1", message);

        }

    }


    AlertDialog dialog;

    @Override
    public void onUserUnagree(String type, String order, String isbn, String book, boolean isReceive, String toUserId) {
        //如果用户点击的是不同意，我们需要重新发送一条信息过去，告诉用户
        if (type.equals("1") || type.equals("10")) {
            //改
            EMMessage message = EMMessage.createTxtSendMessage("小主，您好！《" + book + "》这本书的主人，残忍的拒绝了您的借书请求！是否更换藏书人继续借书", toUserId);
            message.setAttribute("borrow", "2");
            message.setAttribute("order", order);
            message.setAttribute("book", book);
            message.setAttribute("isbn", isbn);
            message.setAttribute("nick", SpUtils.getUser().getJsonData().getNick_name());
            message.setAttribute("avatar", SpUtils.getUser().getJsonData().getAvatar());
            message.setAttribute("userid", SpUtils.getUser().getJsonData().getUser_id());
//            EMClient.getInstance().chatManager().sendMessage(message);

            agree(toUserId, order, "0", message);
            Toast.makeText(getActivity(), "agree", Toast.LENGTH_SHORT).show();

            //用户拒绝借书，并且拒绝再次借书，应该发送一个信息告诉用户显示退款项
        } else if (type.equals("2") || type.equals("20")) {

//            EMMessage message2 = EMMessage.createTxtSendMessage("暂时不想借书", toUserId);
//            message2.setAttribute("borrow", "3");
//            message2.setAttribute("isbn", isbn);
//            message2.setAttribute("nick", SpUtils.getUser().getJsonData().getNick_name());
//            message2.setAttribute("avatar", SpUtils.getUser().getJsonData().getAvatar());
//            message2.setAttribute("userid", SpUtils.getUser().getJsonData().getUser_id());
//            EMClient.getInstance().chatManager().sendMessage(message2);

            dialog = new AlertDialog.Builder(getActivity()).setMessage("小主，非常抱歉没能满足您的借书愿望！您的借书费用会在0-3个工作日，返还到您的余额中！非常感谢您对图书的喜爱，让我们共同进步！").create();

            dialog.show();
//            EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//            message.setAttribute("borrow", "2.5");
//            message.setFrom(toUserId);
//            EMTextMessageBody messageBody = new EMTextMessageBody("");
//            message.addBody(messageBody);
//            EMClient.getInstance().chatManager().sendMessage(message);

//            messageList.refreshSelectLast();


        } else if (type.equals("3") || type.equals("30")) {
//            EMMessage message = EMMessage.createTxtSendMessage("非常抱歉", toUserId);
//            message.setAttribute("borrow", "4");
//            message.setAttribute("nick", SpUtils.getUser().getJsonData().getNick_name());
//            message.setAttribute("avatar", SpUtils.getUser().getJsonData().getAvatar());
//            message.setAttribute("userid", SpUtils.getUser().getJsonData().getUser_id());
//            EMClient.getInstance().chatManager().sendMessage(message);
        } else if (type.equals("600") || type.equals("6000")) {
            //不同意交换名片  order为名片的id
            EMMessage message = EMMessage.createTxtSendMessage("不同意交换名片", toUserId);
            message.setAttribute("nick", SpUtils.getUser().getJsonData().getNick_name());
            message.setAttribute("avatar", SpUtils.getUser().getJsonData().getAvatar());
            message.setAttribute("userid", SpUtils.getUser().getJsonData().getUser_id());
            agreeSweep(toUserId, order, "2", message);
        }
    }


    /**
     * 同意借书和不同借书
     *
     * @param toUserId
     * @param order
     * @param isGive
     * @param message
     */
    public void agree(String toUserId, String order, String isGive, final EMMessage message) {

        HashMap<String, String> map = new HashMap<>(2);
        map.put("uid", toUserId);
        map.put("out_trade_no", order);
        map.put("is_give", isGive);
        map = AppSign.buildMap(map);

        NetUtils.getGsonRetrofit().agreeBorrow(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(@NonNull Result result) throws Exception {
                        EMClient.getInstance().chatManager().sendMessage(message);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof ServerException) {
                            ServerException exception = (ServerException) throwable;
                            Toast.makeText(getActivity(), exception.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    /**
     * 确认还书
     */
    public void backBook(String uid, String orderId, final EMMessage emMessage) {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("uid", uid);
        map.put("out_trade_no", orderId);
        map = AppSign.buildMap(map);

        NetUtils.getGsonRetrofit().returnPerson(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(@NonNull Result result) throws Exception {
                        EMClient.getInstance().chatManager().sendMessage(emMessage);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof ServerException) {
                            ServerException exception = (ServerException) throwable;
                            Toast.makeText(getActivity(), exception.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * @param toUserId
     * @param applyId
     * @param state    1、不同意   2、同意
     * @param message
     */
    public void agreeSweep(String toUserId, String applyId, String state, final EMMessage message) {

        HashMap<String, String> map = new HashMap<>(2);
        map.put("apply_id", applyId);
        map.put("state", state);
        map = AppSign.buildMap(map);

        NetUtils.getGsonRetrofit().applyCard(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(@NonNull Result result) {
                        EMClient.getInstance().chatManager().sendMessage(message);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof ServerException) {
                            ServerException exception = (ServerException) throwable;
                            Toast.makeText(getActivity(), exception.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }


    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_VIDEO:

                break;
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;

            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {

    }


    /**
     * make a video call
     */
    protected void startVideoCall() {

    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 11;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //messagee recall
                else if (message.getBooleanAttribute(Constant.MESSAGE_TYPE_RECALL, false)) {
                    return MESSAGE_TYPE_RECALL;
                }

            }
            return 0;
        }

        @Override
        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {

                //recall message
//                 if(message.getBooleanAttribute(Constant.MESSAGE_TYPE_RECALL, false)){
//                    EaseChatRowPresenter presenter = new EaseChatRecallPresenter();
//                    return presenter;
//                }

            }
            return null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

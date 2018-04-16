package dingshi.com.hibook.hx;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;

import java.util.List;
import java.util.Map;

import dingshi.com.hibook.MainActivity;
import dingshi.com.hibook.R;
import dingshi.com.hibook.bean.Avatar;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.utils.SpUtils;

/**
 * @author wangqi
 * @since 2018/1/10 11:21
 */

public class DemoHelper {
    protected static final String TAG = "DemoHelper";
    /**
     * 本地的广播
     */
    private LocalBroadcastManager broadcastManager;
    private static DemoHelper instance = null;
    private Context appContext;
    private EaseUI easeUI;

    public synchronized static DemoHelper getInstance() {
        if (instance == null) {
            instance = new DemoHelper();
        }
        return instance;
    }

    public void init(Context context) {
        EMOptions options = initChatOptions();
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;

            EMClient.getInstance().setDebugMode(true);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //设置一些用户的信息
            setEaseUIProviders();
            setGlobalListeners();
            broadcastManager = LocalBroadcastManager.getInstance(appContext);
        }
    }

    private void setEaseUIProviders() {
        /*
         * 设置用户的头像为圆
         */
        EaseAvatarOptions avatarOptions = new EaseAvatarOptions();
        avatarOptions.setAvatarShape(1);
        easeUI.setAvatarOptions(avatarOptions);

        /* 设置全局用户的昵称和头像
         */
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

        /*
            环信语音、震动的一些配置
         */
        easeUI.setSettingsProvider(new EaseUI.EaseSettingsProvider() {

            //语音打开
            @Override
            public boolean isSpeakerOpened() {
                return true;
            }

            //信息震动
            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return true;
            }

            //发送信息的声音
            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return true;
            }

            //允许通知
            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                return true;
            }
        });
        /*
         * 设置表情的处理，就是接收对方发过来的表情要与之对应才能显示
         */
        easeUI.setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {

            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
                for (EaseEmojicon emojicon : data.getEmojiconList()) {
                    if (emojicon.getIdentityCode().equals(emojiconIdentityCode)) {
                        return emojicon;
                    }
                }
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });
        /*
         * 环信消息通知的显示和处理
         */
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return "HiBook";
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return R.mipmap.ic_launcher;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format("%s在群聊中@了你", user.getNick());
                    }
                    return user.getNick() + ": " + ticker;
                } else {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format("%s在群聊中@了你", message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(appContext, ChatActivity.class);
                // open calling activity if there is call
                EMMessage.ChatType chatType = message.getChatType();
                // single chat message
                if (chatType == EMMessage.ChatType.Chat) {
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra("chatType", 1);
                } else { // group chat message
                    // message.getTo() is the group id
                    intent.putExtra("userId", message.getTo());
                    if (chatType == EMMessage.ChatType.GroupChat) {
                        intent.putExtra("chatType", 2);
                    } else {
                        intent.putExtra("chatType", 3);
                    }
                }
                return intent;
            }
        });
    }

    /**
     * @param username 环信的id
     * @return 环信的用户信息
     */
    private EaseUser getUserInfo(String username) {
        User u = SpUtils.getUser();
        //请求后台获取用户的昵称和头像
        EaseUser user = new EaseUser(username);
        //设置默认的头像和昵称
        if (username.equals(EMClient.getInstance().getCurrentUser())) {
            if (u != null) {
                user.setAvatar(u.getJsonData().getAvatar());
                user.setNickname(u.getJsonData().getNick_name());
            }
        } else {
            Avatar avatar = SpUtils.getAvatar(username);
            if (avatar != null) {
                user.setAvatar(avatar.getAvatar());
                user.setNickname(avatar.getNick());
            }
        }
        EaseCommonUtils.setUserInitialLetter(user);
        return user;
    }


    /**
     * 初始化环信的配置
     *
     * @return
     */
    private EMOptions initChatOptions() {
        Log.d(TAG, "init HuanXin Options");

        EMOptions options = new EMOptions();
        //设置加好友验证  false为不验证
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);

        /**
         * NOTE:你需要设置自己申请的Sender ID来使用Google推送功能，详见集成文档
         */
        options.setFCMNumber("921300338324");
        //you need apply & set your own id if you want to use Mi push notification
        options.setMipushConfig("2882303761517426801", "5381742660801");
        return options;
    }


    /**
     * 设置环信的监听，如果用户在另一台登录，应该跳转首页通知用户已在另一台手机登录，然后跳转到登录页面
     */
    EMConnectionListener connectionListener;

    protected void setGlobalListeners() {

        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {
                    onUserException(Constant.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    onUserException(Constant.ACCOUNT_CONFLICT);
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                    onUserException(Constant.ACCOUNT_FORBIDDEN);
                } else if (error == EMError.USER_KICKED_BY_CHANGE_PASSWORD) {
                    onUserException(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD);
                } else if (error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                    onUserException(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE);
                }
            }

            @Override
            public void onConnected() {

            }
        };

        /*
           注册环信的连接监听
         */
        EMClient.getInstance().addConnectionListener(connectionListener);
        //register message event listener
        registerMessageListener();

    }

    /**
     * user met some exception: conflict, removed or forbidden
     */
    protected void onUserException(String exception) {
        EMLog.e(TAG, "onUserException: " + exception);
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.putExtra(exception, true);
        appContext.startActivity(intent);

    }

    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {

            /**
             * //收到消息
             * @param messages
             */
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId() + message.getFrom());
                    if (!easeUI.hasForegroundActivies()) {
                        getNotifier().onNewMsg(message);
                    }
                    /*
                       获取对方过来的昵称和头像
                     */
                    String nick = message.getStringAttribute("nick", "你好读");
                    String avatar = message.getStringAttribute("avatar", "default");

                    /*
                        存储在本地
                     */
                    SpUtils.putAvatar(message.getFrom(), new Avatar(nick, avatar));
                    EMLog.e(TAG, "onMessageReceived  : " + message.toString());
                    EMLog.e(TAG, "onMessageReceived nick : " + nick);
                    EMLog.e(TAG, "onMessageReceived avatar : " + avatar);
                }
            }

            /**
             * 收到透传消息
             * @param messages
             */
            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {

//
//                    Log.e("test", message.getStringAttribute("content", "default"));
//
//                    EMLog.e("test", "receive command message");
//                    //get message body
//                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//                    String action = cmdMsgBody.action();//获取自定义action
//                    EMLog.e("test", String.format("Command：action:%s,message:%s", action, message.toString()));
//                    //这个地方可以判断是评论的消息    然后分发给其他用户
//
//
//                    Intent intent = new Intent(Constant.USER_EVAL);
//
//                    if (action.equals(Constant.USER_BORROW)) {
//                        intent.setAction(Constant.USER_BORROW);
//                        intent.putExtra("user_id", message.getFrom());
//                        intent.putExtra("content", message.getStringAttribute("content", ""));
//                        intent.putExtra("order_id", message.getStringAttribute("orderid", ""));
//                    }
//
//                    broadcastManager.sendBroadcast(intent);
//
//                    EMLog.e("test", action + "----------------------" + message.toString());
//

                }
            }

            /**
             *  //收到已读回执
             * @param messages
             */
            @Override
            public void onMessageRead(List<EMMessage> messages) {

            }

            /**
             *   //收到已送达回执
             * @param message
             */
            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }

            /**
             * //消息被撤回
             * @param messages
             */
            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                for (EMMessage msg : messages) {
                    if (msg.getChatType() == EMMessage.ChatType.GroupChat && EaseAtMessageHelper.get().isAtMeMsg(msg)) {
                        EaseAtMessageHelper.get().removeAtMeGroup(msg.getTo());
                    }
                    EMMessage msgNotification = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                    EMTextMessageBody txtBody = new EMTextMessageBody(String.format("%1$s 撤回了一条消息", msg.getFrom()));
                    msgNotification.addBody(txtBody);
                    msgNotification.setFrom(msg.getFrom());
                    msgNotification.setTo(msg.getTo());
                    msgNotification.setUnread(false);
                    msgNotification.setMsgTime(msg.getMsgTime());
                    msgNotification.setLocalTime(msg.getMsgTime());
                    msgNotification.setChatType(msg.getChatType());
                    msgNotification.setAttribute(Constant.MESSAGE_TYPE_RECALL, true);
                    msgNotification.setStatus(EMMessage.Status.SUCCESS);
                    EMClient.getInstance().chatManager().saveMessage(msgNotification);
                }
            }

            /**
             * //消息状态变动
             * @param message
             * @param change
             */
            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                EMLog.d(TAG, "change:");
                EMLog.d(TAG, "change:" + change);
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }


    /**
     * logout
     *
     * @param unbindDeviceToken whether you need unbind your device token
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        Log.d(TAG, "logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    /**
     * 释放资源
     */
    private synchronized void reset() {
        instance = null;

    }

}

package dingshi.com.hibook;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.igexin.sdk.PushManager;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import dingshi.com.hibook.action.ISettingView;
import dingshi.com.hibook.bean.Avatar;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.hx.Constant;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dingshi.com.hibook.base.BaseActivity;
import dingshi.com.hibook.hx.DemoHelper;
import dingshi.com.hibook.present.SettingPresent;
import dingshi.com.hibook.push.DemoIntentService;
import dingshi.com.hibook.push.DemoPushService;
import dingshi.com.hibook.retrofit.exception.ApiException;
import dingshi.com.hibook.retrofit.net.NetUtils;
import dingshi.com.hibook.retrofit.observer.HttpRxObservable;
import dingshi.com.hibook.retrofit.observer.HttpRxObserver;
import dingshi.com.hibook.ui.LoginActivity;
import dingshi.com.hibook.ui.ZxingBorrowActivity;
import dingshi.com.hibook.ui.fragment.BookMyFragment;
import dingshi.com.hibook.ui.fragment.BookStoreFragment;
import dingshi.com.hibook.ui.fragment.LibraryFragment;
import dingshi.com.hibook.ui.fragment.MsgFragment;
import dingshi.com.hibook.utils.AppSign;
import dingshi.com.hibook.utils.SpUtils;
import dingshi.com.hibook.view.BottomBarItem;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wangqi 2017-10-25 10:11
 */
public class MainActivity extends BaseActivity implements ISettingView {

    @BindView(R.id.main_tab1)
    BottomBarItem mainTab1;
    @BindView(R.id.main_tab2)
    BottomBarItem mainTab2;
    @BindView(R.id.main_tab3)
    BottomBarItem mainTab3;
    @BindView(R.id.main_tab4)
    BottomBarItem mainTab4;
    @BindView(R.id.main_tab5)
    BottomBarItem mainTab5;
    @BindView(R.id.main_unread)
    TextView txUnread;

    /**
     * 书城
     */
    BookStoreFragment storeFragment;

    /**
     * 图书馆
     */
    LibraryFragment libFragment;
    /**
     * 消息
     */
    MsgFragment msgFragment;
    /**
     * 我的
     */
    BookMyFragment myFragment;

    private FragmentManager fragmentManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hideTitleBar();
        fragmentManager = getSupportFragmentManager();
        setTabSelection(1);
        showExceptionDialogFromIntent(getIntent());
        registerBroadcastReceiver();

    }


    /**
     * 处理因异常导致Fragment重叠
     *
     * @param fragment
     */
    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        super.onAttachFragment(fragment);
        if (storeFragment == null && fragment instanceof BookStoreFragment) {
            storeFragment = (BookStoreFragment) fragment;
        } else if (libFragment == null && fragment instanceof LibraryFragment) {
            libFragment = (LibraryFragment) fragment;
        } else if (msgFragment == null && fragment instanceof MsgFragment) {
            msgFragment = (MsgFragment) fragment;
        } else if (myFragment == null && fragment instanceof BookMyFragment) {
            myFragment = (BookMyFragment) fragment;
        }

    }


    @OnClick({R.id.main_tab1, R.id.main_tab2, R.id.main_tab4, R.id.main_tab5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tab1:
                setTabSelection(1);
                break;
            case R.id.main_tab2:
                setTabSelection(2);
                break;
            case R.id.main_tab4:
                setTabSelection(4);
                break;
            case R.id.main_tab5:
                setTabSelection(5);
                break;
            default:
        }
    }

    /**
     * 当前位置
     */
    int currentPosition;

    public void setTabSelection(int index) {
        currentPosition = index;
        resetBottomStatus();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 1:
                mainTab1.setStatus(true);
                if (storeFragment == null) {
                    storeFragment = new BookStoreFragment();
                    transaction.add(R.id.main_controller, storeFragment);
                } else {
                    transaction.show(storeFragment);
                }
                break;
            case 2:
                mainTab2.setStatus(true);
                if (libFragment == null) {
                    libFragment = new LibraryFragment();
                    transaction.add(R.id.main_controller, libFragment);
                } else {
                    transaction.show(libFragment);
                }
                break;
            case 4:
                mainTab4.setStatus(true);
                if (msgFragment == null) {
                    msgFragment = new MsgFragment();
                    transaction.add(R.id.main_controller, msgFragment);
                } else {
                    transaction.show(msgFragment);
                }
                break;
            case 5:
                mainTab5.setStatus(true);
                if (myFragment == null) {
                    myFragment = new BookMyFragment();
                    transaction.add(R.id.main_controller, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                break;
            default:
        }
        transaction.commitAllowingStateLoss();
    }


    /**
     * 将所有的Fragment设置隐藏状态,用于对Fragment执行操作的事务
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (storeFragment != null) {
            transaction.hide(storeFragment);
        }
        if (libFragment != null) {
            transaction.hide(libFragment);
        }
        if (msgFragment != null) {
            transaction.hide(msgFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    @OnClick({R.id.main_tab3})
    public void clickActivity(View view) {
        switch (view.getId()) {
            case R.id.main_tab3:
                AndPermission.with(this)
                        .requestCode(200)
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(new PermissionListener() {
                            @Override
                            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                                if (requestCode == 200) {
                                    startActivity(new Intent(MainActivity.this, ZxingBorrowActivity.class));
                                }
                            }

                            @Override
                            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                                showToast("请从应用信息里面打开权限");
                            }
                        }).start();

                break;
            default:
        }
    }

    public void resetBottomStatus() {
        mainTab1.setStatus(false);
        mainTab2.setStatus(false);
        mainTab3.setStatus(false);
        mainTab4.setStatus(false);
        mainTab5.setStatus(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        if (!isConflict) {
            updateUnreadLabel();
        }
    }

    private boolean isExceptionDialogShow = false;

    private void showExceptionDialogFromIntent(Intent intent) {
        EMLog.e("MainActivity", "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


    public boolean isConflict = false;
    private AlertDialog.Builder exceptionBuilder;
    SettingPresent settingPresent = new SettingPresent(this, this);

    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        if (!MainActivity.this.isFinishing()) {
            try {
                if (exceptionBuilder == null) {
                    exceptionBuilder = new AlertDialog.Builder(MainActivity.this);
                }
                exceptionBuilder.setTitle("离线通知");
                exceptionBuilder.setMessage("该账号已在其他设备登录，请重新登录");
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        settingPresent.signOut();
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
    }

    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        //透传消息过来的评价，查看DemoHelper的接收cmd消息
        intentFilter.addAction(Constant.USER_EVAL);
        intentFilter.addAction(Constant.USER_BORROW);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction();
//
//                Log.i("test", "MainActivity come " + action);
//                /**
//                 * 获取用户接受的透传消息
//                 */
//                if (action.equals(Constant.USER_BORROW)) {
//                    Toast.makeText(context, intent.getStringExtra("content"), Toast.LENGTH_SHORT).show();
//                    String userId = intent.getStringExtra("user_id");
//
//                    SpUtils.putInvite(userId, true);
//
//
//                    Log.i("test", "SpUtils putInvite " + SpUtils.getInvite(userId));
//                }

            }
        };

        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }


    /**
     * 消息监听
     */
    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    /**
     * 刷新消息
     */
    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (currentPosition == 4) {
                    // refresh conversation list
                    if (msgFragment != null) {
                        msgFragment.refresh();
                    }
                }
            }
        });
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        if (txUnread == null) {
            return;
        }
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            txUnread.setText(String.valueOf(count));
            txUnread.setVisibility(View.VISIBLE);
        } else {
            txUnread.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取未读消息
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMessageCount();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastManager != null) {
            broadcastManager.unregisterReceiver(broadcastReceiver);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public void signOut() {
        finish();
        DemoHelper.getInstance().logout(false, null);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

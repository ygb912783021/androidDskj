package dingshi.com.hibook.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseFragment;
import dingshi.com.hibook.bean.card.CardDetails;

/**
 * @author wangqi
 * @since 2017/12/20 上午10:18
 */

public class CardInfoFragment extends BaseFragment {

    @BindView(R.id.card_info_sex)
    TextView txGender;
    @BindView(R.id.card_info_birth)
    TextView txBirth;
    @BindView(R.id.card_info_address)
    TextView txAddress;
    @BindView(R.id.card_info_college)
    TextView txCollege;
    @BindView(R.id.card_info_email)
    TextView txEmail;
    @BindView(R.id.card_info_position)
    TextView txPosition;
    @BindView(R.id.card_info_work)
    TextView txWork;
    @BindView(R.id.card_info_edu)
    TextView txEdu;
    @BindView(R.id.card_info_intro)
    TextView txIntro;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_card_info;
    }

    @Override
    public void initView() {

    }


    public void updateData(CardDetails.JsonDataBean cardDetails) {
        if (cardDetails.getSex() == 1) {
            txGender.setText("男");
        } else if (cardDetails.getSex() == 2) {
            txGender.setText("女");
        }
        txBirth.setText(cardDetails.getBirthday());
        txAddress.setText(cardDetails.getAddress());
        txCollege.setText(cardDetails.getEducation().get(0).getCollege());
        txEmail.setText(cardDetails.getEmail());
        txPosition.setText(cardDetails.getPosition());
        txWork.setText(cardDetails.getWork().get(0).getDescribe());
        txEdu.setText(cardDetails.getEducation().get(0).getDescribe());
        txIntro.setText(cardDetails.getIntroduce());
    }
}

package com.iplay.feastbooking.ui.self;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.LoginUserHolder;
import com.iplay.feastbooking.assistance.WindowAttr;
import com.iplay.feastbooking.basic.BasicFragment;
import com.iplay.feastbooking.component.view.bar.FunctionBar;
import com.iplay.feastbooking.dto.UserDto;
import com.iplay.feastbooking.gson.cashBack.CashBackMessageEvent;
import com.iplay.feastbooking.gson.selfInfo.SelfInfo;
import com.iplay.feastbooking.messageEvent.selfInfo.SelfInfoMessageEvent;
import com.iplay.feastbooking.net.utilImpl.cashBack.CashBackUtility;
import com.iplay.feastbooking.net.utilImpl.selfDetail.ChangeSelfInfoUtility;
import com.iplay.feastbooking.ui.order.OrderListActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 2017/7/17.
 */

public class SelfFragment extends BasicFragment implements View.OnClickListener{

    public static final String TAG = "SelfFragment";

    private static final int ORDER_UNDER_INDEX = 0;

    private static final int ORDER_HISTORY_INDEX = 1;

    private static final int MY_COLLECTION_INDEX = 2;

    private static final int PROBLEM_INDEX = 3;

    private static final int SETTING_INDEX = 4;

    private View view;

    private Context mContext;

    private View statusView;

    private CircleImageView avatar;

    private FunctionBar functionBars[] = new FunctionBar[5];

    private TextView user_name_tv;

    private TextView user_email_tv;

    private TextView total_cash_back_tv, pending_cash_back_tv, completed_cash_back_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.self_main_page,container,false);
        mContext = getActivity();

        total_cash_back_tv = (TextView) view.findViewById(R.id.total_cash_back_tv);
        pending_cash_back_tv = (TextView) view.findViewById(R.id.pending_cash_back_tv);
        completed_cash_back_tv = (TextView) view.findViewById(R.id.completed_cash_back_tv);

        statusView = view.findViewById(R.id.status_bar_fix);
        statusView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(getActivity())));

        user_name_tv = (TextView) view.findViewById(R.id.user_name);
        user_email_tv = (TextView) view.findViewById(R.id.user_email);
        avatar = (CircleImageView) view.findViewById(R.id.user_portrait);

        functionBars[ORDER_UNDER_INDEX] = (FunctionBar) view.findViewById(R.id.order_under_fb);
        functionBars[ORDER_UNDER_INDEX].setOnClickListener(this);

        functionBars[ORDER_HISTORY_INDEX] = (FunctionBar) view.findViewById(R.id.order_history_fb);
        functionBars[ORDER_HISTORY_INDEX].function_name_tv.setText("歷史訂單");

        functionBars[MY_COLLECTION_INDEX] = (FunctionBar) view.findViewById(R.id.my_collection_fb);
        functionBars[MY_COLLECTION_INDEX].function_name_tv.setText("我的收藏");

        functionBars[PROBLEM_INDEX] = (FunctionBar) view.findViewById(R.id.problem_fb);
        functionBars[PROBLEM_INDEX].function_name_tv.setText("常見為題");

        functionBars[SETTING_INDEX] = (FunctionBar) view.findViewById(R.id.setting_fb);
        functionBars[SETTING_INDEX].function_name_tv.setText("設置");

        UserDto currentUser;
        if((currentUser = LoginUserHolder.getInstance().getCurrentUser())!=null){
            user_name_tv.setText(currentUser.getUsername());
            user_email_tv.setText("郵箱: " + currentUser.getEmail());
            String avatarUrl = currentUser.getAvatarUrl();
            if(avatarUrl != null){
                Glide.with(mContext).load(avatarUrl).into(avatar);
            }
        }

        view.findViewById(R.id.self_detail_rl).setOnClickListener(this);
        refreshSelfInfo();

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCashBackMessageEvent(CashBackMessageEvent event){
        int userId = event.getUserId();
        UserDto currentUser = LoginUserHolder.getInstance().getCurrentUser();
        if(currentUser != null){
            if(userId == currentUser.getUserId()){
                completed_cash_back_tv.setText("$" + event.getCompletedCashback());
                total_cash_back_tv.setText("$" + event.getTotalCashback());
                pending_cash_back_tv.setText("$" + event.getPendingCashback());
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        isRegisteredNeed = true;
        super.onCreate(savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelfInfoMessageEvent(SelfInfoMessageEvent event){
        SelfInfo selfInfo = event.getSelfInfo();
        if(selfInfo != null){
            String avatarUrl = selfInfo.avatar;
            if(avatarUrl != null){
                Glide.with(mContext).load(selfInfo.avatar).placeholder(R.drawable.loading).into(avatar);
            }
            if(selfInfo.username != null){
                user_name_tv.setText(selfInfo.username);
            }
            if(selfInfo.email != null){
                user_email_tv.setText("郵箱: " + selfInfo.email);
            }
        }
    }

    private void refreshSelfInfo(){
        ChangeSelfInfoUtility.getInstance(mContext).updateSelfInfo(mContext);
        CashBackUtility.getInstance(mContext).getCashBack(mContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.self_detail_rl:
                SelfInfoActivity.start(mContext);
                break;
            case R.id.order_under_fb:
                functionBars[ORDER_UNDER_INDEX].disable();
                OrderListActivity.start(mContext);
                functionBars[ORDER_UNDER_INDEX].enable();
                break;
            default:
                break;
        }
    }
}

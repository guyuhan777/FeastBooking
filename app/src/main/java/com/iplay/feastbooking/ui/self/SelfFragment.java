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
import com.iplay.feastbooking.dao.UserDao;
import com.iplay.feastbooking.ui.order.OrderListActivity;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.self_main_page,container,false);
        mContext = getActivity();

        statusView = (View) view.findViewById(R.id.status_bar_fix);
        statusView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(getActivity())));

        avatar = (CircleImageView) view.findViewById(R.id.user_portrait);
        Glide.with(mContext).load(R.drawable.ks).into(avatar);

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

        UserDao currentUser;
        if((currentUser = LoginUserHolder.getInstance().getCurrentUser())!=null){
            ((TextView) view.findViewById(R.id.user_name)).setText(currentUser.getUsername());
            ((TextView) view.findViewById(R.id.user_id)).setText("ID: " + currentUser.getUserId());
        }

        view.findViewById(R.id.self_detail_rl).setOnClickListener(this);

        return view;
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

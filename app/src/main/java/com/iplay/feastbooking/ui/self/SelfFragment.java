package com.iplay.feastbooking.ui.self;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.iplay.feastbooking.R;
import com.iplay.feastbooking.assistance.WindowAttr;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 2017/7/17.
 */

public class SelfFragment extends Fragment {

    public static final String TAG = "SelfFragment";

    private View view;

    private Context mContext;

    private CircleImageView user_avatar_iv;

    private View statusView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.self_main_page,container,false);
        mContext = getActivity();

        statusView = (View) view.findViewById(R.id.status_bar_fix);
        statusView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowAttr.getStatusBarHeight(getActivity())));

        user_avatar_iv = (CircleImageView) view.findViewById(R.id.user_avatar_civ);
        Glide.with(mContext).load(R.drawable.shenzi).into(user_avatar_iv);
        return view;
    }
}

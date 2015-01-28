package com.bainiaohe.dodo.main.fragments.personal_center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.setting.SettingActivity;

/**
 * Created by Lewis on 2015/1/20.
 */
public class PersonalCenterFragment extends Fragment implements View.OnClickListener {

    protected View view;
    private Button set_but;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_personal, container, false);
        initView();
        initListen();

        return view;
    }

    public void initView() {
        set_but = (Button) view.findViewById(R.id.settingitems);
    }

    public void initListen() {
        set_but.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.settingitems) {
            startActivity(new Intent(PersonalCenterFragment.this.getActivity(), SettingActivity.class));
        }
    }
}

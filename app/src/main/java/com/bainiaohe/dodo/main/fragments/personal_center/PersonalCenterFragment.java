package com.bainiaohe.dodo.main.fragments.personal_center;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bainiaohe.dodo.R;

/**
 * Created by Lewis on 2015/1/20.
 */
public class PersonalCenterFragment extends Fragment {

    protected View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_personal, container, false);

        return view;
    }
}

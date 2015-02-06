package com.bainiaohe.dodo.main.fragments.internship;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bainiaohe.dodo.R;
import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;

public class InternshipFragment extends Fragment {

    private static final String TAG = "InternshipFragment";

    private MaterialEditText internship_location = null;
    private MaterialEditText internship_mentor = null;
    private MaterialEditText school_teacher = null;
    private MaterialEditText internship_report = null;
    private CircularProgressButton submitButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_internship, container, false);
        initViews(view);

        return view;
    }

    /**
     * 初始化界面
     */
    private void initViews(View view) {
        //todo set title bar 设置签到

        //TODO 自动获取位置  &&  自动完成
        internship_location = (MaterialEditText) view.findViewById(R.id.intern_location);
        internship_mentor = (MaterialEditText) view.findViewById(R.id.intern_mentor);
        school_teacher = (MaterialEditText) view.findViewById(R.id.school_teacher);
        internship_report = (MaterialEditText) view.findViewById(R.id.intern_report);

        submitButton = (CircularProgressButton) view.findViewById(R.id.submit);
        submitButton.setIndeterminateProgressMode(true);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (submitButton.getProgress() == -1) {
                    submitButton.setProgress(0);
                } else if (submitButton.getProgress() == 0) {
                    //TODO 发送数据(点击事件的处理逻辑可能需要调整)
                    submitButton.setProgress(50);
                } else if (submitButton.getProgress() == 100) {
                    submitButton.setProgress(0);
                } else
                    submitButton.setProgress(100);
            }
        });
    }


}

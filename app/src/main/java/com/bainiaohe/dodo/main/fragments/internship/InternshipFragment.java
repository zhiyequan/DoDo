package com.bainiaohe.dodo.main.fragments.internship;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.bainiaohe.dodo.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class InternshipFragment extends Fragment {

    private MaterialEditText internship_location = null;
    private MaterialEditText internship_mentor = null;
    private MaterialEditText shcool_teacher = null;
    private MaterialEditText internship_report = null;
    private Button submitButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_internship, container, false);
    }


    /**
     * 初始化界面
     */
    private void initViews(View view) {
        //todo set title bar

        internship_report = (MaterialEditText) view.findViewById(R.id.intern_report);

        view.findViewById(R.id.intern_location);

    }

}

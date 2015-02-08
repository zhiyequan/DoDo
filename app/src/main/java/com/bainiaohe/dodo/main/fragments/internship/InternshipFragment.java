package com.bainiaohe.dodo.main.fragments.internship;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bainiaohe.dodo.R;
import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InternshipFragment extends Fragment {

    private static final String TAG = "InternshipFragment";

    private MaterialEditText internship_location = null;
    private MaterialEditText internship_mentor = null;
    private MaterialEditText school_teacher = null;
    private MaterialEditText internship_report = null;
    private CircularProgressButton submitButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);//否则不执行onCreateOptionsMenu
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();//清除activity中其他的menu item
        inflater.inflate(R.menu.menu_internship, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.punch_card) {

            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.view_calendar, null);

            initCalendar((CalendarPickerView) contentView.findViewById(R.id.calendar_view));

            new MaterialDialog.Builder(getActivity())
                    .title("您已连续签到10天")//todo 引用res，并从服务器加载数据
                    .customView(contentView, true)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 从服务器获取数据，初始化签到日历
     *
     * @param calendarPickerView
     */
    private void initCalendar(CalendarPickerView calendarPickerView) {
        //TODO dummy implementation

        Date today = new Date();
        Log.e(TAG, "today:" + today);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendarPickerView.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);


        ArrayList<Date> selectedDates = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Calendar temp = Calendar.getInstance();
            temp.add(Calendar.DATE, i);
            selectedDates.add(temp.getTime());
        }

        calendarPickerView.highlightDates(selectedDates);

        //截获点击事件，不再继续响应
        calendarPickerView.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                return true;
            }
        });
    }
}

package com.bainiaohe.dodo.main.fragments.menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.main.MainActivity;
import com.bainiaohe.dodo.main.fragments.friends.FriendsFragment;
import com.bainiaohe.dodo.main.fragments.info.InfoFragment;
import com.bainiaohe.dodo.main.fragments.internship.InternshipFragment;
import com.bainiaohe.dodo.main.fragments.messages.MessageFragment;
import com.bainiaohe.dodo.main.sections.MaterialAccountSection;
import com.bainiaohe.dodo.main.sections.MaterialBodySection;
import com.bainiaohe.dodo.main.sections.MaterialBottomSection;
import com.bainiaohe.dodo.main.sections.listeners.MaterialAccountSectionOnClickListener;
import com.bainiaohe.dodo.main.sections.listeners.MaterialBodySectionOnClickListener;
import com.bainiaohe.dodo.main.sections.listeners.MaterialBottomSectionOnClickListener;
import com.bainiaohe.dodo.main.util.Utils;

import java.util.ArrayList;

/**
 * 注:生成section时，需要从上到下按顺序生成，且生成后必须添加到sections中
 * Created by zhugongpu on 15/2/13.
 */
public class MenuFragment extends Fragment implements MaterialBodySectionOnClickListener, MaterialAccountSectionOnClickListener, MaterialBottomSectionOnClickListener {

    private static final String TAG = "Menu Fragment";

    /**
     * Views
     */
    private RelativeLayout account_section_container = null;
    private LinearLayout body_sections_container = null;
    private LinearLayout bottom_sections_container = null;
    /**
     * 用于存放menu中body中的所有section（account和bottom不需要存储）
     */
    private ArrayList<MaterialBodySection>
            bodySections = new ArrayList<>();
    //当前正在显示的fragment
    private Fragment currentFragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, null);
        init(view);

        return view;
    }

    private void init(View view) {


        this.account_section_container = (RelativeLayout) view.findViewById(R.id.account_section_container);
        this.body_sections_container = (LinearLayout) view.findViewById(R.id.body_sections_container);
        this.bottom_sections_container = (LinearLayout) view.findViewById(R.id.bottom_sections_container);

        // add sections

        //set up account section
        setAccountSection("name", "email", R.drawable.ic_launcher, null);

        //TODO add body sections
        addBodySection(getText(R.string.section_info).toString(), "", new InfoFragment());
        addBodySection(getText(R.string.section_internship).toString(), "", new InternshipFragment());

        addSubHeader(getText(R.string.sub_header_friends).toString());
        addBodySection(getText(R.string.section_friends).toString(), "", R.drawable.tab_friends, new FriendsFragment());
        addBodySection(getText(R.string.section_messages).toString(), "", R.drawable.tab_message, new MessageFragment());
        addBodySection(getText(R.string.section_discovery).toString(), "", R.drawable.ic_launcher, null);

        addDivider();//结束body section

        //set up bottom section
        setBottomSection(getText(R.string.section_settings).toString(), "", R.drawable.settings, null);

        //设置第一个被选中
        bodySections.get(0).setSelected(true);
    }

    //add bodySections
    public void setAccountSection(String name, String email, String avatarUrl, Intent targetIntent) {
        setAccountSection(new MaterialAccountSection(getActivity(), name, email, avatarUrl, targetIntent, this));
    }

    public void setAccountSection(String name, String email, int avatarId, Intent targetIntent) {
        setAccountSection(new MaterialAccountSection(getActivity(), name, email, avatarId, targetIntent, this));
    }

    /**
     * 设置account section，不需要指定position
     */
    public void setAccountSection(MaterialAccountSection account) {
        account_section_container.removeAllViews();
        account_section_container.addView(account.getContentView());
    }

    /**
     * add body section
     *
     * @param title
     * @param notification
     * @param targetFragment
     */
    public void addBodySection(String title, String notification, Fragment targetFragment) {
        addBodySection(new MaterialBodySection(getActivity(), title, notification, bodySections.size(), targetFragment, this));
    }

    public void addBodySection(String title, String notification, int iconId, Fragment targetFragment) {
        addBodySection(new MaterialBodySection(getActivity(), iconId, title, notification, bodySections.size(), targetFragment, this));
    }

    public void addSubHeader(String title) {

        float density = getActivity().getResources().getDisplayMetrics().density;
        // create layout
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        // inflate the line
        View divider = new View(getActivity());
        divider.setBackgroundColor(Color.parseColor("#e0e0e0"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(0, (int) (8 * density), 0, (int) (8 * density));
        layout.addView(divider, params);

        // inflate the textView
        TextView textView = new TextView(getActivity());
        Utils.setAlpha(textView, 0.54f);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setGravity(Gravity.START);
        textView.setText(title);
        textView.setTextColor(Color.BLACK);

        LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsText.setMargins((int) (16 * density), 0, (int) (16 * density), (int) (4 * density));
        layout.addView(textView, paramsText);

        body_sections_container.addView(layout);
    }

    /**
     * 向body section 中添加分割线
     */
    public void addDivider() {
        View view = new View(getActivity());
        view.setBackgroundColor(Color.parseColor("#e0e0e0"));
        // height 1 px
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        body_sections_container.addView(view, params);
    }

    /**
     * 向body添加一个section(Icon Section或No Icon Section或Sub Header)，并指定position
     */
    public void addBodySection(MaterialBodySection section) {
        bodySections.add(section);
        body_sections_container.addView(section.getContentView());
    }

    public void setBottomSection(String title, String notification, int iconId, Intent targetIntent) {
        setBottomSection(new MaterialBottomSection(getActivity(), iconId, title, notification, targetIntent, this));
    }

    /**
     * 设置bottom section(Icon Section或No Icon Section)，不需要指定position
     */
    public void setBottomSection(MaterialBottomSection section) {
        bottom_sections_container.removeAllViews();
        bottom_sections_container.addView(section.getContentView());
    }


    @Override
    public void onClick(MaterialBodySection selectedSection) {

        Log.e(TAG, "onClick : " + selectedSection.getPosition() + " " + selectedSection.isSelected());

        if (!selectedSection.isSelected() && selectedSection.getTargetFragment() != null)//已处于选中状态时，不再响应点击事件
        {
            ((MainActivity) getActivity()).switchContent(selectedSection.getTargetFragment());

//            Log.e(TAG, "show content : " + selectedSection.getTargetFragment().getClass().getSimpleName());
        }

        //取消其他section的选中状态
        for (MaterialBodySection section : bodySections) {
            if (section.isSelected() && section.getPosition() != selectedSection.getPosition())
                section.setSelected(false);
        }

        // 设置当前section为选中状态
        selectedSection.setSelected(true);

    }

    @Override
    public void onClick(MaterialAccountSection selectedSection) {
        if (selectedSection.getTargetIntent() != null) {
            startActivity(selectedSection.getTargetIntent());
        }
    }

    @Override
    public void onClick(MaterialBottomSection selectedSection) {
        if (selectedSection.getTargetIntent() != null) {
            startActivity(selectedSection.getTargetIntent());
        }
    }
}


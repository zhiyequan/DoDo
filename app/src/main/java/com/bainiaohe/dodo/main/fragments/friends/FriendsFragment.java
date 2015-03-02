//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.bainiaohe.dodo.main.fragments.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.bainiaohe.dodo.main.fragments.friends.adapter.FriendListAdapter;
import com.bainiaohe.dodo.main.fragments.friends.adapter.FriendListAdapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.rong.imkit.RCloudContext;
import io.rong.imkit.Res;
import io.rong.imkit.adapter.FriendMultiChoiceAdapter;
import io.rong.imkit.fragment.ActionBaseFragment;
import io.rong.imkit.model.Friend;
import io.rong.imkit.view.PinnedHeaderListView;
import io.rong.imkit.view.SwitchGroup;
import io.rong.imkit.view.SwitchGroup.ItemHander;
import io.rong.imkit.view.SwitchItemView;
import io.rong.imlib.RongIMClient.UserInfo;

public class FriendsFragment extends ActionBaseFragment implements ItemHander, OnClickListener, OnItemClickListener {
    protected FriendListAdapter mAdapter;
    protected List<Friend> mFriendsList;
    //联系人列表
    private PinnedHeaderListView mListView;
    //侧滑栏
    private SwitchGroup mSwitchGroup;
    private boolean isMultiChoice = false;
    private ArrayList<String> mSelectedItemIds;

    public FriendsFragment() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(Res.getInstance(this.getActivity()).layout("fragment_friends"), null);
        this.mListView = this.getViewById(view, android.R.id.list);
        this.mSwitchGroup = this.getViewById(view, android.R.id.message);

        this.mListView.setPinnedHeaderView(null);
        this.mListView.setFastScrollEnabled(false);
        this.mListView.setOnItemClickListener(this);
        this.mSwitchGroup.setItemHander(this);
        this.mListView.setHeaderDividersEnabled(true);
        this.mListView.setFooterDividersEnabled(true);
        return view;
    }

    public void onResume() {
        super.onResume();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (this.getActionBar() != null) {
            this.getActionBar().setOnBackClick(new OnClickListener() {
                public void onClick(View v) {
                    FriendsFragment.this.getActivity().finish();
                }
            });
        }

        ArrayList userInfos = null;
        if (RCloudContext.getInstance() != null && RCloudContext.getInstance().getGetFriendsProvider() != null) {
            userInfos = (ArrayList) RCloudContext.getInstance().getGetFriendsProvider().getFriends();
        }

        this.mFriendsList = new ArrayList();
        Iterator var4;
        if (userInfos != null) {
            var4 = userInfos.iterator();

            while (var4.hasNext()) {
                UserInfo id = (UserInfo) var4.next();
                Friend friend = new Friend();
                friend.setNickname(id.getName());
                friend.setPortrait(id.getPortraitUri());
                friend.setUserId(id.getUserId());
                this.mFriendsList.add(friend);
            }
        }

        this.mFriendsList = this.sortFriends(this.mFriendsList);
//        if (this.mSelectedItemIds != null && this.isMultiChoice) {
//            var4 = this.mSelectedItemIds.iterator();
//
//            while (var4.hasNext()) {
//                String id1 = (String) var4.next();
//                Iterator friend2 = this.mFriendsList.iterator();
//
//                while (friend2.hasNext()) {
//                    Friend friend1 = (Friend) friend2.next();
//                    if (id1.equals(friend1.getUserId())) {
//                        friend1.setSelected(true);
//                        break;
//                    }
//                }
//            }
//        }
        //如果没有多选框的话则创建联系人列表
        this.mAdapter = (FriendListAdapter) (this.isMultiChoice ? new FriendMultiChoiceAdapter(this.getActivity(), this.mFriendsList, this.mSelectedItemIds) : new FriendListAdapter(this.getActivity(), this.mFriendsList));
        this.mListView.setAdapter(this.mAdapter);

        //填充数据
        this.fillData();
        super.onViewCreated(view, savedInstanceState);
    }

    public boolean onBackPressed() {
        return false;
    }

    private final void fillData() {
        this.mAdapter.removeAll();
        this.mAdapter.setAdapterData(this.mFriendsList);
        this.mAdapter.notifyDataSetChanged();
    }


    public void onClick(View v) {
        if (v instanceof SwitchItemView) {
            CharSequence tag = ((SwitchItemView) v).getText();
            if (this.mAdapter != null && this.mAdapter.getSectionIndexer() != null) {
                Object[] sections = this.mAdapter.getSectionIndexer().getSections();
                int size = sections.length;

                for (int i = 0; i < size; ++i) {
                    if (tag.equals(sections[i])) {
                        int index = this.mAdapter.getPositionForSection(i);
                        this.mListView.setSelection(index + this.mListView.getHeaderViewsCount());
                        break;
                    }
                }
            }
        }

    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object tagObj = view.getTag();
        if (tagObj != null && tagObj instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) tagObj;
            switch (position) {
                case 1:
                    Toast.makeText(getActivity(), "推荐的好友/群", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getActivity(), "进入我的群组", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    startActivity(new Intent(getActivity(), PhoneContactActivity.class));
                    break;
                default:
                    this.mAdapter.onItemClick(viewHolder.friend.getUserId(), viewHolder.friend.getNickname());
            }

        }
    }

    public void onDestroyView() {
        if (this.mAdapter != null) {
            this.mAdapter.destroy();
            this.mAdapter = null;
        }

        super.onDestroyView();
    }


    private ArrayList<Friend> sortFriends(List<Friend> friends) {
        String[] searchLetters = this.getResources().getStringArray(Res.getInstance(this.getActivity()).array("rc_search_letters"));
        HashMap userMap = new HashMap();
        ArrayList friendsArrayList = new ArrayList();
        Iterator i = friends.iterator();

        //设置联系人列表中最开头的功能item
        setHeadgroups(userMap);

        while (i.hasNext()) {
            Friend letter = (Friend) i.next();
            String fArrayList = new String(new char[]{letter.getSearchKey()});
            ArrayList friendList;
            if (userMap.containsKey(fArrayList)) {
                friendList = (ArrayList) userMap.get(fArrayList);
                friendList.add(letter);
            } else {
                friendList = new ArrayList();
                friendList.add(letter);
                userMap.put(fArrayList, friendList);
            }
        }

        for (int var9 = 0; var9 < searchLetters.length; ++var9) {
            String var10 = searchLetters[var9];
            ArrayList var11 = (ArrayList) userMap.get(var10);
            if (var11 != null) {
                friendsArrayList.addAll(var11);
            }
        }

        return friendsArrayList;
    }

    /**
     * 设置联系人关系列表前面几个item——推荐的人/群 ，我的群组、
     *
     * @param userMap
     */
    private void setHeadgroups(HashMap userMap) {

        String ffArrayList = new String("↑");
        ArrayList fFriendList = new ArrayList();

        //推荐的人/群 作为最开始的item
        Friend itemFriend1 = new Friend();
        itemFriend1.setNickname("推荐的人/群");
        itemFriend1.setUserId("↑");
        itemFriend1.setSearchKey('↑');
        fFriendList.add(itemFriend1);

        //我的群组 作为第二个item
        Friend itemFriend2 = new Friend();
        itemFriend2.setNickname("我的群组");
        itemFriend2.setUserId("↑");
        itemFriend2.setSearchKey('↑');
        fFriendList.add(itemFriend2);

        //手机好友 作为第三个item
        Friend itemFriend3 = new Friend();
        itemFriend3.setNickname("通讯录好友");
        itemFriend3.setUserId("↑");
        itemFriend3.setSearchKey('↑');
        fFriendList.add(itemFriend3);

        userMap.put(ffArrayList, fFriendList);

    }

}

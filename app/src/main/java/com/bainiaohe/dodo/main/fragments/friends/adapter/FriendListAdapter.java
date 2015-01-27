package com.bainiaohe.dodo.main.fragments.friends.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.sea_monster.core.resource.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.rong.imkit.Res;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.Friend;
import io.rong.imkit.model.FriendSectionIndexer;
import io.rong.imkit.view.AsyncImageView;
import io.rong.imkit.view.PinnedHeaderAdapter;

@SuppressLint({"UseSparseArrays"})
public class FriendListAdapter extends PinnedHeaderAdapter<Friend> {
    private LayoutInflater mInflater;

    private ArrayList<View> mViewList;

    public FriendListAdapter(Context context, List<Friend> friends) {
        super(context);
        this.setAdapterData(friends);
        this.mViewList = new ArrayList();
        if (context != null) {
            this.mInflater = LayoutInflater.from(context);
        }

    }

    public void setAdapterData(List<Friend> friends) {
        if (this.dataSet != null) {
            this.dataSet.clear();
        }

        HashMap hashMap = new HashMap();
        ArrayList result = new ArrayList();
        boolean key = false;
        Iterator var5 = friends.iterator();

        while (var5.hasNext()) {
            Friend friend = (Friend) var5.next();
            char key1 = friend.getSearchKey();
            int length;
            if (hashMap.containsKey(Integer.valueOf(key1))) {
                length = ((Integer) hashMap.get(Integer.valueOf(key1))).intValue();
                if (length <= result.size() - 1) {
                    ((List) result.get(length)).add(friend);
                }
            } else {
                result.add(new ArrayList());
                length = result.size() - 1;
                ((List) result.get(length)).add(friend);
                hashMap.put(Integer.valueOf(key1), Integer.valueOf(length));
            }
        }

        this.updateCollection(result);

    }

    protected View newView(Context context, int partition, List<Friend> data, int position, ViewGroup parent) {
        View view = this.mInflater.inflate(Res.getInstance(context).layout("rc_item_friend"), (ViewGroup) null);
        FriendListAdapter.ViewHolder holder = new FriendListAdapter.ViewHolder();
        this.newSetTag(view, holder, position, data);
        view.setTag(holder);
        return view;
    }

    protected void bindView(View v, int partition, List<Friend> data, int position) {
        FriendListAdapter.ViewHolder holder = (FriendListAdapter.ViewHolder) v.getTag();
        TextView name = holder.name;
        AsyncImageView photo = holder.photo;
        Friend friend = (Friend) data.get(position);
        name.setText(friend.getNickname());
        Resource res = friend.getPortraitResource();
        photo.setResource(res);
        photo.setTag(Integer.valueOf(position));
        holder.friend = friend;
    }

    protected View newHeaderView(Context context, int partition, List<Friend> data, ViewGroup parent) {
        View view = null;
        if (partition == 0) {
            view = this.mInflater.inflate(Res.getInstance(context).layout("rc_item_friend_index"), (ViewGroup) null);
            view.setTag(view.findViewById(android.R.id.text1));
            Log.d("FriendAdapter", "这是0+1");
        } else {
            view = this.mInflater.inflate(Res.getInstance(context).layout("rc_item_friend_index"), (ViewGroup) null);
            view.setTag(view.findViewById(android.R.id.text1));
        }

        return view;



    }

    protected void bindHeaderView(View view, int partition, List<Friend> data) {

        Object objTag = view.getTag();

        if (partition == 0) {
            ((TextView) objTag).setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            Log.d("FriendAdapter", "这是0");
        }

        if (objTag != null && partition != 0) {
            ((TextView) objTag).setText(String.valueOf(((Friend) data.get(0)).getSearchKey()));
            Log.d("FriendAdapter", String.valueOf(((Friend) data.get(0)).getSearchKey()));
        }


//        if(String.valueOf(((Friend)data.get(0)).getSearchKey()).equals("*"))
//        {
//            view.setVisibility(View.GONE);
//            Log.d("FriendListAdapter", String.valueOf(((Friend) data.get(0)).getSearchKey())+"test "+partition);
//        }


    }

    protected SectionIndexer updateIndexer(Partition<Friend>[] data) {
        return new FriendSectionIndexer(data);
    }

    public void configurePinnedHeader(View header, int position, int alpha) {
        FriendListAdapter.PinnedHeaderCache cache = (FriendListAdapter.PinnedHeaderCache) header.getTag();
        if (cache == null) {
            cache = new FriendListAdapter.PinnedHeaderCache();
            cache.titleView = (TextView) header.findViewById(android.R.id.text1);
            cache.textColor = cache.titleView.getTextColors();
            cache.background = header.getBackground();
            header.setTag(cache);
        }

        int section = this.getSectionForPosition(position);
        if (section != -1) {
            String title = (String) this.getSectionIndexer().getSections()[section];
            cache.titleView.setText(title);
            if (alpha == 255) {
                cache.titleView.setTextColor(cache.textColor);
            } else {
                int textColor = cache.textColor.getDefaultColor();
                cache.titleView.setTextColor(Color.argb(alpha, Color.red(textColor), Color.green(textColor), Color.blue(textColor)));
            }
        }

    }

    protected void newSetTag(View view, FriendListAdapter.ViewHolder holder, int position, List<Friend> data) {
        AsyncImageView photo = (AsyncImageView) view.findViewById(android.R.id.icon);
        if (this.mViewList != null && !this.mViewList.contains(view)) {
            this.mViewList.add(view);
        }

        holder.name = (TextView) view.findViewById(android.R.id.text1);
        holder.photo = photo;
    }

    public void destroy() {
        if (this.mViewList != null) {
            this.mViewList.clear();
            this.mViewList = null;
        }

    }


    public void onItemClick(String friendId, CheckBox checkBox) {
        RongIM.getInstance().startPrivateChat(mContext, friendId, "聊天界面");
    }


    public static class ViewHolder {
        public TextView name;
        public AsyncImageView photo;
        public String userId;
        public Friend friend;
        public CheckBox choice;

        public ViewHolder() {
        }
    }

    class PinnedHeaderCache {
        TextView titleView;
        ColorStateList textColor;
        Drawable background;

        PinnedHeaderCache() {
        }
    }
}

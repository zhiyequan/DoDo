//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bainiaohe.dodo.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sea_monster.core.resource.model.Resource;
import io.rong.imkit.Res;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.Friend;
import io.rong.imkit.model.FriendSectionIndexer;
import io.rong.imkit.utils.PinyinFilterList;
import io.rong.imkit.view.AsyncImageView;
import io.rong.imkit.view.PinnedHeaderAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@SuppressLint({"UseSparseArrays"})
public class FriendListAdapter extends PinnedHeaderAdapter<Friend> implements Filterable {
    private LayoutInflater mInflater;
    private FriendFilter mFilter;
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
        this.mFilter = new FriendFilter(friends);
    }

    protected View newView(Context context, int partition, List<Friend> data, int position, ViewGroup parent) {
        View view = this.mInflater.inflate(Res.getInstance(context).layout("rc_item_friend"), null);
        ViewHolder holder = new ViewHolder();
        this.newSetTag(view, holder, position, data);
        view.setTag(holder);
        return view;
    }

    protected void bindView(View v, int partition, List<Friend> data, int position) {
        ViewHolder holder = (ViewHolder) v.getTag();
        TextView name = holder.name;
        AsyncImageView photo = holder.photo;
        Friend friend = data.get(position);
        name.setText(friend.getNickname());
        Resource res = friend.getPortraitResource();
        photo.setResource(res);
        photo.setTag(Integer.valueOf(position));
        holder.friend = friend;
    }

    protected View newHeaderView(Context context, int partition, List<Friend> data, ViewGroup parent) {
        View view = this.mInflater.inflate(Res.getInstance(context).layout("rc_item_friend_index"), null);
        view.setTag(view.findViewById(android.R.id.text1));
        return view;
    }

    protected void bindHeaderView(View view, int partition, List<Friend> data) {
        Object objTag = view.getTag();
        if (objTag != null) {
            ((TextView) objTag).setText(String.valueOf(data.get(0).getSearchKey()));
        }

    }

    protected SectionIndexer updateIndexer(Partition<Friend>[] data) {
        return new FriendSectionIndexer(data);
    }

    public void configurePinnedHeader(View header, int position, int alpha) {
        PinnedHeaderCache cache = (PinnedHeaderCache) header.getTag();
        if (cache == null) {
            cache = new PinnedHeaderCache();
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

    protected void newSetTag(View view, ViewHolder holder, int position, List<Friend> data) {
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

    public Filter getFilter() {
        return this.mFilter;
    }

    public void onItemClick(String friendId, CheckBox checkBox) {
        RongIM.getInstance().startPrivateChat(getContext(), friendId, "聊天界面");
        Log.d("FriendListAdapter", "the Click is " + friendId);
    }

    public interface OnFilterFinished {
        void onFilterFinished();
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

    class FriendFilter extends PinyinFilterList<Friend> {
        public FriendFilter(List dataList) {
            super(dataList);
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            Object friends = results.values;
            if (friends == null) {
                friends = new ArrayList();
            }

            HashMap hashMap = new HashMap();
            ArrayList result = new ArrayList();
            boolean key = false;
            Iterator var7 = ((List) friends).iterator();

            while (var7.hasNext()) {
                Friend friend = (Friend) var7.next();
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

            FriendListAdapter.this.updateCollection(result);
            if (result.size() > 0) {
                FriendListAdapter.this.notifyDataSetChanged();
            } else {
                FriendListAdapter.this.notifyDataSetInvalidated();
            }

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

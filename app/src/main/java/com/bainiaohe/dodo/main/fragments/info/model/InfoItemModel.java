package com.bainiaohe.dodo.main.fragments.info.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * InfoFragment中的每个条目
 * 实现Parcelable，可以用intent传递
 * Created by zhugongpu on 15/1/22.
 */
public class InfoItemModel implements Parcelable {
    public static final Creator<InfoItemModel> CREATOR = new Creator<InfoItemModel>() {
        @Override
        public InfoItemModel createFromParcel(Parcel parcel) {
            return new InfoItemModel(parcel);
        }

        @Override
        public InfoItemModel[] newArray(int size) {
            return new InfoItemModel[size];
        }
    };
    public String id = "";
    public String name = "";//用户名
    public boolean isMarked;//是否已经赞过
    public String avatarImage = "";//头像
    public String text_content = "";//文本内容
    public ArrayList<String> imageUrls;
    public String time = "";
    public ArrayList<CommentModel> comments;//评论列表


    public InfoItemModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        boolean[] boolArray = new boolean[1];
        in.readBooleanArray(boolArray);
        this.isMarked = boolArray[0];
        this.avatarImage = in.readString();
        this.text_content = in.readString();
        this.imageUrls = new ArrayList<>();
        in.readList(this.imageUrls, ClassLoader.getSystemClassLoader());
        this.time = in.readString();
        this.comments = new ArrayList<>();
        in.readList(this.comments, ClassLoader.getSystemClassLoader());
    }

    public InfoItemModel() {

    }

    /**
     * 内容描述
     * 不用管
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 打包
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeBooleanArray(new boolean[]{this.isMarked});
        dest.writeString(this.avatarImage);
        dest.writeString(this.text_content);
        dest.writeList(this.imageUrls);
        dest.writeString(this.time);
        dest.writeList(this.comments);
    }
}

package com.bainiaohe.dodo.main.fragments.info.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhugongpu on 15/2/2.
 */
public class CommentModel implements Parcelable {
    public static final Creator<CommentModel> CREATOR = new Creator<CommentModel>() {

        @Override
        public CommentModel createFromParcel(Parcel parcel) {
            return new CommentModel(parcel);
        }

        @Override
        public CommentModel[] newArray(int size) {
            return new CommentModel[size];
        }
    };
    public String id = "";
    public String avatar = "";
    public String name = "";
    public String time = "";
    public String content = "";


    public CommentModel() {

    }

    public CommentModel(Parcel in) {

        this.id = in.readString();
        this.avatar = in.readString();
        this.name = in.readString();
        this.time = in.readString();
        this.content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.id);
        parcel.writeString(this.avatar);
        parcel.writeString(this.name);
        parcel.writeString(this.time);
        parcel.writeString(this.content);
    }
}

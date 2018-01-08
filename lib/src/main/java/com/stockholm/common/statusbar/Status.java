package com.stockholm.common.statusbar;

import android.os.Parcel;
import android.os.Parcelable;

public class Status implements Parcelable {

    private String packageName;
    private int iconRes;
    private int order;
    private int group;

    public Status(String packageName, int iconRes, int order, int group) {
        this.packageName = packageName;
        this.iconRes = iconRes;
        this.order = order;
        this.group = group;
    }

    public Status(StatusManager.Builder builder) {
        this.iconRes = builder.iconRes;
        this.order = builder.order;
        this.group = builder.group;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getIconRes() {
        return iconRes;
    }

    public int getOrder() {
        return order;
    }

    public int getGroup() {
        return group;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeInt(this.iconRes);
        dest.writeInt(this.order);
        dest.writeInt(this.group);
    }

    protected Status(Parcel in) {
        this.packageName = in.readString();
        this.iconRes = in.readInt();
        this.order = in.readInt();
        this.group = in.readInt();
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel source) {
            return new Status(source);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
}

package com.stockholm.common.task;


import android.os.Parcel;
import android.os.Parcelable;

public class MeowTaskModel implements Parcelable {

    public static final Creator<MeowTaskModel> CREATOR = new Creator<MeowTaskModel>() {
        @Override
        public MeowTaskModel createFromParcel(Parcel source) {
            return new MeowTaskModel(source);
        }

        @Override
        public MeowTaskModel[] newArray(int size) {
            return new MeowTaskModel[size];
        }
    };

    private String packageName;
    private String taskId;
    private int priority;

    protected MeowTaskModel(Parcel in) {
        this.packageName = in.readString();
        this.taskId = in.readString();
        this.priority = in.readInt();
    }

    public MeowTaskModel(String packageName, String taskId, int priority) {
        this.packageName = packageName;
        this.taskId = taskId;
        this.priority = priority;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getTaskId() {
        return taskId;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.taskId);
        dest.writeInt(this.priority);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MeowTaskModel) {
            MeowTaskModel model = (MeowTaskModel) obj;
            return model.getTaskId().equals(taskId) && model.getPackageName().equals(packageName);
        }
        return false;
    }

    @Override
    public String toString() {
        return "MeowTaskModel{"
                + "packageName='" + packageName + '\''
                + ", taskId='" + taskId + '\''
                + ", priority=" + priority + '}';
    }

}

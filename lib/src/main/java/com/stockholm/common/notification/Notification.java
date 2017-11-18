package com.stockholm.common.notification;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spanned;
import android.text.TextUtils;

import org.joda.time.LocalDateTime;

public class Notification implements Parcelable {
    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    private static final int PRIORITY_MAX = 2;
    private static final int PRIORITY_HIGH = 1;
    private static final int PRIORITY_DEFAULT = 0;

    private String title;
    private String content;
    private String description;
    private String leftMsg;
    private String rightMsg;
    private NotificationType type = NotificationType.GLOBAL_NORMAL;
    private boolean showAppearTime = true;
    private LocalDateTime eventTime = LocalDateTime.now();
    private boolean autoDismiss = false;
    private boolean openRing = false;
    private boolean breakOtherSound = true;
    private boolean canPause = true;
    private long duration = 5000;
    private int priority;
    private FontSpanStyle fontSpanStyle;

    public Notification() {
    }

    protected Notification(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.description = in.readString();
        this.leftMsg = in.readString();
        this.rightMsg = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : NotificationType.values()[tmpType];
        this.showAppearTime = in.readByte() != 0;
        this.eventTime = (LocalDateTime) in.readSerializable();
        this.autoDismiss = in.readByte() != 0;
        this.openRing = in.readByte() != 0;
        this.breakOtherSound = in.readByte() != 0;
        this.canPause = in.readByte() != 0;
        this.duration = in.readLong();
        this.priority = in.readInt();
        this.fontSpanStyle = in.readParcelable(FontSpanStyle.class.getClassLoader());
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public String getLeftMsg() {
        return leftMsg;
    }

    public String getRightMsg() {
        return rightMsg;
    }

    public NotificationType getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isShowAppearTime() {
        return showAppearTime;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public boolean isAutoDismiss() {
        return autoDismiss;
    }

    public boolean isOpenRing() {
        return openRing;
    }

    public boolean isBreakOtherSound() {
        return breakOtherSound;
    }

    public boolean isCanPause() {
        return canPause;
    }

    public long getDuration() {
        return duration;
    }

    public FontSpanStyle getFontSpanStyle() {
        return fontSpanStyle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.description);
        dest.writeString(this.leftMsg);
        dest.writeString(this.rightMsg);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeByte(this.showAppearTime ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.eventTime);
        dest.writeByte(this.autoDismiss ? (byte) 1 : (byte) 0);
        dest.writeByte(this.openRing ? (byte) 1 : (byte) 0);
        dest.writeByte(this.breakOtherSound ? (byte) 1 : (byte) 0);
        dest.writeByte(this.canPause ? (byte) 1 : (byte) 0);
        dest.writeLong(this.duration);
        dest.writeInt(this.priority);
        dest.writeParcelable(this.fontSpanStyle, flags);
    }

    public static class Builder {
        private String title;
        private String content;
        private String description;
        private String leftMsg;
        private String rightMsg;
        private boolean showAppearTime = true;
        private LocalDateTime eventTime = LocalDateTime.now();
        private boolean autoDismiss = false;
        private boolean openRing = false;
        private boolean breakOtherSound = true;
        private boolean canPause = true;
        private long duration = 5000;
        private FontSpanStyle fontSpanStyle;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            if (!TextUtils.isEmpty(this.leftMsg) || !TextUtils.isEmpty(this.rightMsg))
                throw new IllegalArgumentException("Notification Error: content can't exist with leftMsg either rightMsg");
            this.content = content;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setLeftMsg(String leftMsg) {
            if (!TextUtils.isEmpty(this.content))
                throw new IllegalArgumentException("Notification Error: content can't exist with leftMsg either rightMsg");
            this.leftMsg = leftMsg;
            return this;
        }

        public Builder setRightMsg(String rightMsg) {
            if (!TextUtils.isEmpty(this.content))
                throw new IllegalArgumentException("Notification Error: content can't exist with leftMsg either rightMsg");
            this.rightMsg = rightMsg;
            return this;
        }

        public Builder setShowAppearTime(boolean showAppearTime) {
            this.showAppearTime = showAppearTime;
            return this;
        }

        public Builder setEventTime(LocalDateTime eventTime) {
            this.eventTime = eventTime;
            return this;
        }

        public Builder setAutoDismiss(boolean autoDismiss) {
            this.autoDismiss = autoDismiss;
            return this;
        }

        public Builder setOpenRing(boolean openRing) {
            this.openRing = openRing;
            return this;
        }

        public Builder setBreakOtherSound(boolean breakOtherSound) {
            this.breakOtherSound = breakOtherSound;
            return this;
        }

        public Builder setCanPause(boolean canPause) {
            this.canPause = canPause;
            return this;
        }

        public Builder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder setFontSpanStyle(FontSpanStyle fontSpanStyle) {
            this.fontSpanStyle = fontSpanStyle;
            return this;
        }

        private void apply(Notification notification) {
            notification.title = this.title;
            notification.content = this.content;
            notification.description = this.description;
            notification.leftMsg = this.leftMsg;
            notification.rightMsg = this.rightMsg;
            notification.priority = parsePriority(notification.getType());
            notification.showAppearTime = this.showAppearTime;
            notification.eventTime = this.eventTime;
            notification.autoDismiss = this.autoDismiss;
            notification.openRing = this.openRing;
            notification.breakOtherSound = this.breakOtherSound;
            notification.canPause = this.canPause;
            notification.duration = this.duration;
            notification.fontSpanStyle = this.fontSpanStyle;
        }

        public Notification builder(NotificationType notificationType) {
            Notification notification = new Notification();
            notification.type = notificationType;
            apply(notification);
            return notification;
        }

        private int parsePriority(NotificationType type) {
            switch (type) {
                case GLOBAL_FORCE:
                    return PRIORITY_MAX;
                case GLOBAL_NORMAL:
                    return PRIORITY_HIGH;
                case NORMAL_TASK:
                    return PRIORITY_DEFAULT;
                default:
                    return PRIORITY_DEFAULT;
            }
        }
    }

    // read more http://www.jianshu.com/p/84067ad289d2
    public static class FontSpanStyle implements Parcelable {
        public static final Creator<FontSpanStyle> CREATOR = new Creator<FontSpanStyle>() {
            @Override
            public FontSpanStyle createFromParcel(Parcel source) {
                return new FontSpanStyle(source);
            }

            @Override
            public FontSpanStyle[] newArray(int size) {
                return new FontSpanStyle[size];
            }
        };

        public int startIndex;
        public int endIndex;
        public int style; //Typeface: NORMAL BOLD, ITALIC, BOLD_ITALIC
        public int spanFlag = Spanned.SPAN_INCLUSIVE_INCLUSIVE;

        public FontSpanStyle() {
        }

        protected FontSpanStyle(Parcel in) {
            this.startIndex = in.readInt();
            this.endIndex = in.readInt();
            this.style = in.readInt();
            this.spanFlag = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.startIndex);
            dest.writeInt(this.endIndex);
            dest.writeInt(this.style);
            dest.writeInt(this.spanFlag);
        }

    }

    public enum NotificationType {
        //全局通知: 闹钟
        GLOBAL_FORCE,
        //普通全局: 日程提醒, 天气提醒
        GLOBAL_NORMAL,
        //一般通知
        NORMAL_TASK
    }

}
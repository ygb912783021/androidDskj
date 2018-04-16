package dingshi.com.hibook.share;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wangqi
 * @since 2017/11/15 11:14
 */

public class ShareParams implements Parcelable {
    public String title;
    public String describe;
    public String webUrl;
    /**
     * 分享的类型，有朋友圈、微信好友和喜欢的圈子
     * 默认是朋友圈
     */
    public int shareType;


    private ShareParams(Build build) {
        this.title = build.title;
        this.describe = build.describe;
        this.webUrl = build.webUrl;
        this.shareType = build.shareType;
    }

    private ShareParams(Parcel in) {
        title = in.readString();
        describe = in.readString();
        webUrl = in.readString();
        shareType = in.readInt();
    }

    public static final Creator<ShareParams> CREATOR = new Creator<ShareParams>() {
        @Override
        public ShareParams createFromParcel(Parcel in) {
            return new ShareParams(in);
        }

        @Override
        public ShareParams[] newArray(int size) {
            return new ShareParams[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(describe);
        dest.writeString(webUrl);
        dest.writeInt(shareType);
    }


    public static class Build {
        private String title;
        private String describe;
        private String webUrl;
        private int shareType = IWeixin.WXSceneTimeline;

        public Build withTitle(String title) {
            this.title = title;
            return this;
        }

        public Build withDescribe(String describe) {
            this.describe = describe;
            return this;
        }

        public Build withUrl(String webUrl) {
            this.webUrl = webUrl;
            return this;
        }

        public Build withShareType(int shareType) {
            this.shareType = shareType;
            return this;
        }

        public ShareParams build() {
            return new ShareParams(this);
        }
    }
}

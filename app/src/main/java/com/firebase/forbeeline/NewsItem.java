package com.firebase.forbeeline;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsItem implements Parcelable {
    private String NewsImageResource;
    private String title, description;

    public NewsItem(String ImageResource, String text1, String text2) {
        NewsImageResource = ImageResource;
        title = text1;
        description = text2;
    }

    protected NewsItem(Parcel in) {
        NewsImageResource = in.readString();
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    public String getNewsImageResource() {
        return NewsImageResource;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(NewsImageResource);
        parcel.writeString(title);
        parcel.writeString(description);
    }
}

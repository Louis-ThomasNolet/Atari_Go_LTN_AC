package ca.csfoy.tp2_ac_ltn.parcelable;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageButton;

public class ParcelableImageButtonArray implements Parcelable {
    private ImageButton[][] imageButtonArray;

    public ParcelableImageButtonArray(ImageButton[][] array) {
        this.imageButtonArray = array;
    }

    private ParcelableImageButtonArray(Parcel in) {
        // Read from parcel in the same order you wrote to it
        // Note: This assumes that ImageButton implements Parcelable or can be serialized in another way
        // You might need to implement Parcelable for ImageButton or use another serialization approach
        // Alternatively, you can store the resource IDs or other information necessary to recreate the ImageButton in the Parcelable
    }

    public ImageButton[][] getImageButtonArray() {
        return imageButtonArray;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write to parcel in the order you want to read from it
        // Note: This assumes that ImageButton implements Parcelable or can be serialized in another way
        // You might need to implement Parcelable for ImageButton or use another serialization approach
        // Alternatively, you can store the resource IDs or other information necessary to recreate the ImageButton in the Parcelable
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableImageButtonArray> CREATOR = new Creator<ParcelableImageButtonArray>() {
        @Override
        public ParcelableImageButtonArray createFromParcel(Parcel in) {
            return new ParcelableImageButtonArray(in);
        }

        @Override
        public ParcelableImageButtonArray[] newArray(int size) {
            return new ParcelableImageButtonArray[size];
        }
    };
}


package ca.csfoy.tp2_ac_ltn.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class MoveData implements Parcelable {
    private int no_play;
    private String color;
    private String stone;

    public MoveData(){
        this(0,"","");
    }

    public MoveData(int no_play, String color, String stone){
        this.setNo_play(no_play);
        this.setColor(color);
        this.setStone(stone);
    }
    public MoveData(Parcel in){
        this(in.readInt(), in.readString(),in.readString());
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
    public static final Parcelable.Creator<MoveData> CREATOR = new Parcelable.Creator<MoveData>(){
        @Override
        public MoveData createFromParcel(Parcel in) {
            return new MoveData(in);
        }

        @Override
        public MoveData[] newArray(int size) {
            return new MoveData[size];
        }
    };

    public int getNo_play() {
        return no_play;
    }

    public void setNo_play(int no_play) {
        this.no_play = no_play;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStone() {
        return stone;
    }

    public void setStone(String stone) {
        this.stone = stone;
    }
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof MoveData))return false;
        MoveData moveData = (MoveData) o;
        return no_play == moveData.no_play &&
                color.equals(moveData.color) &&
                stone.equals(moveData.stone);
    }
    @Override
    public int hashCode(){
        return Objects.hash(no_play,color,stone);
    }


}

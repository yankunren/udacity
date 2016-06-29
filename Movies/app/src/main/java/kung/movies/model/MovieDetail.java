package kung.movies.model;

import android.os.Parcel;

/**
 * Created by Administrator on 2016/6/29.
 */
public class MovieDetail extends Movie {

    private int runtime;
    private int vote_average;
    private String homepage;

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getVote_average() {
        return vote_average;
    }

    public void setVote_average(int vote_average) {
        this.vote_average = vote_average;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.runtime);
        dest.writeInt(this.vote_average);
        dest.writeString(this.homepage);
    }

    public MovieDetail() {
    }

    protected MovieDetail(Parcel in) {
        super(in);
        this.runtime = in.readInt();
        this.vote_average = in.readInt();
        this.homepage = in.readString();
    }

    public static final Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel source) {
            return new MovieDetail(source);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };
}

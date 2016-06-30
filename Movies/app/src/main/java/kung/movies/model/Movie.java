package kung.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/6/27.
 */
public class Movie implements Parcelable {

    private long id;
    private String title;
    private String original_title;
    private String poster_path;
    private String backdrop_path;
    private String original_language;
    private String release_date;
    private int[] genre_ids;
    private String overview;
    private boolean adult;
    private boolean video;
    private int vote_count;
    private double popularity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.original_title);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.original_language);
        dest.writeString(this.release_date);
        dest.writeIntArray(this.genre_ids);
        dest.writeString(this.overview);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeInt(this.vote_count);
        dest.writeDouble(this.popularity);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.original_title = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.original_language = in.readString();
        this.release_date = in.readString();
        this.genre_ids = in.createIntArray();
        this.overview = in.readString();
        this.adult = in.readByte() != 0;
        this.video = in.readByte() != 0;
        this.vote_count = in.readInt();
        this.popularity = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private byte[] data;
    private List<String> comments;
    private List<Integer> ratings;
    private int viewCount;

    public Photo(String name, byte[] data) {
        this.name = name;
        this.data = data;
        this.comments = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.viewCount = 0;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public List<String> getComments() {
        return comments;
    }

    public void addRating(int rating) {
        ratings.add(rating);
    }

    public double getAverageRating() {
        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public int getViewCount() {
        return viewCount;
    }

    public void incrementViewCount() {
        viewCount++;
    }
}

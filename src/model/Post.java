package model;

public class Post {
	private String id;
    private User author;
    private String content;
    private int likes;
    private int shares;
    private String dateTime;

    public Post(String id, User author, String content, int likes, int shares, String dateTime) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.likes = likes;
        this.shares = shares;
        this.dateTime = dateTime;
    }

    // Getters and setters for each field
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

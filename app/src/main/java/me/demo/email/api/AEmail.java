package me.demo.email.api;

public class AEmail {
    private String id;
    private String title;
    private String content;

    public AEmail(String id, String title, String content) {
        this.id = id;
        setTitle(title);
        setContent(content);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

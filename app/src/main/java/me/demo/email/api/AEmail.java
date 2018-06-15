package me.demo.email.api;

public class AEmail {
    private int id;
    private String title;
    private String content;
    private String date;
    private int sendId;
    private int receiverId;
    private int sendStatus;
    private int readStatus;
    private int deleteStatus;

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public String getDate() {
        return date;
    }

    public static class SendStatus {
        public static final int NOT_SEND = 1;
        public static final int HAS_SENT = 2;
    }

    public static class ReadStatus {
        public static final int NOT_READ = 1;
        public static final int HAS_READ = 2;
    }

    public static class DeleteStatus {
        public static final int NOT_DELETE = 0;
        public static final int SOFT_DELETE = 1;
        public static final int HARD_DELETE = 2;
    }


    AEmail(int id, String title, String content, String date, int sendId, int receiverId, int sendStatus, int readStatus, int deleteStatus) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.sendId = sendId;
        this.receiverId = receiverId;
        this.sendStatus = sendStatus;
        this.readStatus = readStatus;
        this.deleteStatus = deleteStatus;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }

    public int getSendId() {
        return sendId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public int getReadStatus() {
        return readStatus;
    }

}

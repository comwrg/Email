package me.demo.email.api;

import java.util.List;

public interface EmailApi {
    boolean login(String usr, String pwd);

    boolean sendEmail(String receiver, String title, String content);

    enum RegisterCode {
        SUCC,
        USR_HAS_REGISTERED,
        UNKNOWN_ERROR,
    }
    RegisterCode register(String usr, String pwd);

    enum Box {
        IN,
        OUT,
        DRAFT,
        DELETE,
    }

    List<AEmail> read(Box box);
    boolean addDrafts(String title, String content, String receiver);
    void markRead(int id);
}

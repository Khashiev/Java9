package edu.school21.sockets.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Message {
    private Long id;
    private String sender;
    private String text;
    private LocalDateTime time;

    public Message() {
    }

    public Message(Long id, String sender, String text, LocalDateTime time) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Message message = (Message) object;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Message.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("sender='" + sender + "'")
                .add("text='" + text + "'")
                .add("time=" + time)
                .toString();
    }
}

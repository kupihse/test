package com.example.models;

public class LastChatMessage {
    public ChatMessage message;
    public String otherEmail;
    public String chatId;

    public LastChatMessage(){}

    public LastChatMessage(String mail, ChatMessage msg, String chat) {
        otherEmail = mail;
        message = msg;
        chatId = chat;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public String getChatId() {
        return chatId;
    }

    public String getOtherEmail() {
        return otherEmail;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public void setOtherEmail(String otherEmail) {
        this.otherEmail = otherEmail;
    }
}

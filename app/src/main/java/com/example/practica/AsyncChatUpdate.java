package com.example.practica;

public class AsyncChatUpdate implements Runnable {
    private ChatActivity chat;

    public AsyncChatUpdate(ChatActivity chat) {
        this.chat = chat;
    }

    @Override
    public void run() {
        while (chat.isInChat()) {
            try {
                chat.getMessagesFromAPI();
                Thread.sleep(500);
            } catch (Exception e) {}
        }

        System.out.println("stop thread");
    }
}

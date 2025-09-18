package com.example.pollypress;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.Response;
import android.util.Log;

public class PollWebSocketManager {
    private static PollWebSocketManager instance;
    private WebSocket webSocket;
    private static final String SOCKET_URL = "ws://coms-3090-003.class.las.iastate.edu:8080/poll/";
    private String username;
    private OkHttpClient client;

    private PollWebSocketManager(String username) {
        this.username = username;
        client = new OkHttpClient();
        connect();
    }

    public static PollWebSocketManager getInstance() {
        if (instance == null) {
            instance = new PollWebSocketManager("defaultUser");
        }
        return instance;
    }

    public void connect() {
        Request request = new Request.Builder().url(SOCKET_URL + username).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d("PollWebSocket", "Connected to poll WebSocket.");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d("PollWebSocket", "Received: " + text);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
                Log.d("PollWebSocket", "Closing: " + code + " / " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e("PollWebSocket", "Error: " + t.getMessage());
            }
        });
    }

    public void sendVote(String selectedOption) {
        if (webSocket != null) {
            webSocket.send("OPTION:" + selectedOption);
        }
    }
}

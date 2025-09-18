package com.example.pollypress;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.Response;
import android.util.Log;
import org.json.JSONObject;

public class PollPercentageWebSocketManager {
    private static PollPercentageWebSocketManager instance;
    private WebSocket webSocket;
    private static final String SOCKET_URL = VolleySingleton.BASE_WS_URL + "pollPercentage/";
    private PercentageListener listener;

    public interface PercentageListener {
        void onUpdate(String selectedOption, double yourShare, int totalVotes, int optionVotes);
        void onError(String message);
    }

    private PollPercentageWebSocketManager() { }

    public static synchronized PollPercentageWebSocketManager getInstance() {
        if (instance == null) {
            instance = new PollPercentageWebSocketManager();
        }
        return instance;
    }

    public void connect(String username, long pollId, PercentageListener l) {
        this.listener = l;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SOCKET_URL + username + "/" + pollId)
                .build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override public void onOpen(WebSocket ws, Response response) {
                Log.d("PctSocket", "Connected");
            }
            @Override public void onMessage(WebSocket ws, String text) {
                try {
                    JSONObject o = new JSONObject(text);
                    if (o.has("ERROR")) {
                        listener.onError(o.getString("ERROR"));
                    } else {
                        listener.onUpdate(
                                o.getString("selectedOption"),
                                o.getDouble("yourShare"),
                                o.getInt("totalVotes"),
                                o.getInt("optionVotes")
                        );
                    }
                } catch (Exception e) {
                    listener.onError(e.getMessage());
                }
            }
            @Override public void onFailure(WebSocket ws, Throwable t, Response response) {
                listener.onError(t.getMessage());
            }
        });
    }

    public void sendVote(String option) {
        if (webSocket != null) {
            webSocket.send(option);
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, null);
        }
    }
}

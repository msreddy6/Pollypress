package com.example.pollypress;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PollWebSocketManagerTest {

    @Test
    public void singletonInstance_notNull() {
        PollWebSocketManager mgr = PollWebSocketManager.getInstance();
        assertNotNull("getInstance() should never return null", mgr);
    }

    @Test
    public void sendVote_withoutExplicitConnect_doesNotThrow() {
        PollWebSocketManager mgr = PollWebSocketManager.getInstance();
        // The constructor already calls connect(), so webSocket may be non-null,
        // but in any case sendVote should not throw even if webSocket is null.
        mgr.sendVote("TestOption");
    }

    @Test
    public void connect_andSendVote_doesNotThrow() {
        PollWebSocketManager mgr = PollWebSocketManager.getInstance();
        // Explicitly reconnect
        mgr.connect();
        // Send a vote
        mgr.sendVote("AnotherOption");
    }
}

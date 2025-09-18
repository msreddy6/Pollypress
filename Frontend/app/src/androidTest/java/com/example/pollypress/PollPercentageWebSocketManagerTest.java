package com.example.pollypress;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PollPercentageWebSocketManagerTest {

    @Test
    public void singletonReturnsSameInstance() {
        PollPercentageWebSocketManager a = PollPercentageWebSocketManager.getInstance();
        PollPercentageWebSocketManager b = PollPercentageWebSocketManager.getInstance();
        assertSame("getInstance() should return the same instance", a, b);
    }

    @Test
    public void sendVote_noConnection_doesNotThrow() {
        PollPercentageWebSocketManager mgr = PollPercentageWebSocketManager.getInstance();
        // should be a no-op when no socket is open
        mgr.sendVote("someOption");
    }

    @Test
    public void close_noConnection_doesNotThrow() {
        PollPercentageWebSocketManager mgr = PollPercentageWebSocketManager.getInstance();
        // should safely handle closing when no socket exists
        mgr.close();
    }
}

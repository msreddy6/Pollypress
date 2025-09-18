package com.example.pollypress;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NewsUserSessionTest {

    private NewsUserSession session;
    private SharedPreferences prefs;

    @Before
    public void setUp() {
        Context ctx = ApplicationProvider.getApplicationContext();
        prefs = ctx.getSharedPreferences("NewsUserSession", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
        session = new NewsUserSession(ctx);
    }

    @Test
    public void saveAndRetrieveUserDetails() {
        session.saveUserDetails(10, "testUser", "test@example.com", "pass123");
        assertEquals(10, session.getUserId());
        assertEquals("testUser", session.getUsername());
        assertEquals("test@example.com", session.getEmail());
        assertEquals("pass123", session.getPassword());
    }

    @Test
    public void clearSession_resetsValues() {
        session.saveUserDetails(5, "userX", "x@x.com", "pwd");
        session.clearSession();
        assertEquals(-1, session.getUserId());
        assertEquals("NewsUser", session.getUsername()); // default if not set
        assertNull(session.getEmail());
        assertNull(session.getPassword());
    }
}

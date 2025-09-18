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
public class ReporterSessionTest {

    private ReporterSession session;
    private SharedPreferences prefs;

    @Before
    public void setUp() {
        Context ctx = ApplicationProvider.getApplicationContext();
        prefs = ctx.getSharedPreferences("ReporterSession", Context.MODE_PRIVATE);
        // Clear any existing data
        prefs.edit().clear().apply();
        session = new ReporterSession(ctx);
    }

    @Test
    public void saveAndRetrieveReporterDetails_fourArgs() {
        // Save with all fields
        session.saveReporterDetails(42, "UserX", "user@example.com", "secretPwd");

        // Verify getters
        assertEquals(42, session.getReporterId());
        // username is stored lowercase
        assertEquals("userx", session.getReporterUsername());
        assertEquals("user@example.com", session.getReporterEmail());
        assertEquals("secretPwd", session.getReporterPassword());

        // isLoggedIn should now be true
        assertTrue(session.isLoggedIn());
    }

    @Test
    public void clearSession_resetsAllData() {
        // Pre-populate
        session.saveReporterDetails(7, "NameY", "y@example.com", "pwdY");
        assertTrue(session.isLoggedIn());

        // Clear
        session.clearSession();

        // All getters should return defaults
        assertEquals(-1, session.getReporterId());
        assertNull(session.getReporterUsername());
        assertNull(session.getReporterEmail());
        assertNull(session.getReporterPassword());

        // isLoggedIn should now be false
        assertFalse(session.isLoggedIn());
    }
}

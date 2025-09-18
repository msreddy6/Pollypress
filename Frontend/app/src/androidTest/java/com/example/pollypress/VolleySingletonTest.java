package com.example.pollypress;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.android.volley.RequestQueue;

import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class VolleySingletonTest {

    @Test
    public void getInstance_returnsSameInstance() {
        Context ctx = ApplicationProvider.getApplicationContext();
        VolleySingleton s1 = VolleySingleton.getInstance(ctx);
        VolleySingleton s2 = VolleySingleton.getInstance(ctx);
        assertSame(
                "getInstance() should always return the same object",
                s1,
                s2
        );
    }

    @Test
    public void getRequestQueue_notNull() {
        Context ctx = ApplicationProvider.getApplicationContext();
        VolleySingleton singleton = VolleySingleton.getInstance(ctx);
        RequestQueue queue = singleton.getRequestQueue();
        assertNotNull("RequestQueue must not be null", queue);
    }
}

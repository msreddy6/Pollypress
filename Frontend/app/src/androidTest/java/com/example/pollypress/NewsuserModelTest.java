package com.example.pollypress;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NewsuserModelTest {

    @Test
    public void gettersAndSetters_workAsExpected() {
        Newsuser u = new Newsuser();

        // Initially null
        assertNull(u.getId());
        assertNull(u.getUsername());
        assertNull(u.getEmail());

        // Set values
        u.setId(55L);
        u.setUsername("newsUser");
        u.setEmail("news@example.com");

        // Verify via getters
        assertEquals(Long.valueOf(55L), u.getId());
        assertEquals("newsUser", u.getUsername());
        assertEquals("news@example.com", u.getEmail());
    }
}

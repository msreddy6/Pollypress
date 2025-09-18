package com.example.pollypress;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ReporterModelTest {

    @Test
    public void gettersAndSetters_workAsExpected() {
        Reporter r = new Reporter();

        // Initially null
        assertNull(r.getId());
        assertNull(r.getUsername());
        assertNull(r.getEmail());

        // Set values
        r.setId(10L);
        r.setUsername("reporterUser");
        r.setEmail("rep@example.com");

        // Verify via getters
        assertEquals(Long.valueOf(10L), r.getId());
        assertEquals("reporterUser", r.getUsername());
        assertEquals("rep@example.com", r.getEmail());
    }
}

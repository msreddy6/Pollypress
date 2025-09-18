package com.example.pollypress.utils;

import android.os.IBinder;
import android.view.WindowManager;
import androidx.test.espresso.Root;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Matches a Toast message popup.
 */
public class ToastMatcher extends BaseMatcher<Root> {

    @Override
    public boolean matches(Object item) {
        if (!(item instanceof Root)) {
            return false;
        }
        Root root = (Root) item;
        // Look for a window of type TOAST
        int type = root.getWindowLayoutParams().get().type;
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            IBinder windowToken = root.getDecorView().getWindowToken();
            IBinder appToken    = root.getDecorView().getApplicationWindowToken();
            // Window that is not contained by any other windows (appToken == windowToken)
            return windowToken == appToken;
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is a toast");
    }
}

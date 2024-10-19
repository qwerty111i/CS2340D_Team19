package com.example.sprint1;
import com.example.sprint1.view.DestinationsActivity;

import android.content.Intent;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogisticsActivityTest {
    public static class LogisticsActivity {
        public Intent getNavigationIntent(Class<?> targetActivity) {
            return new Intent(null, targetActivity);
        }
    }

    @Test
    public void navigate() {
        LogisticsActivity logisticsActivity = new LogisticsActivity();
        Intent intent = logisticsActivity.getNavigationIntent(DestinationsActivity.class);
        assertEquals(DestinationsActivity.class.getName(), intent.getComponent().getClassName());
    }
}

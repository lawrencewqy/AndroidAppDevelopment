package com.example.robolectric.support;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowApplication;

import java.io.File;

/**
 * Just relocate the database file to a hard defined position.
 */
@Implements(className = "android.app.Application")
public class CustomShadowApplication extends ShadowApplication {

    private static final String alternativeDatabasePath = "build/test-resources/unit-test.db";
    private File database = new File(alternativeDatabasePath);

    @Override
    @Implementation
    public File getDatabasePath(String name) {
        return database;
    }
}

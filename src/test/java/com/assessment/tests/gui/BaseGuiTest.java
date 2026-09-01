package com.assessment.tests.gui;

import com.assessment.core.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseGuiTest {
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.createDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}

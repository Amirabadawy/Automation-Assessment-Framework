package com.assessment.tests.gui;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class GuiRetryAnalyzer implements IRetryAnalyzer {
    private static final int MAX_RETRIES = 1;
    private int retryCount;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRIES) {
            retryCount++;
            return true;
        }
        return false;
    }
}

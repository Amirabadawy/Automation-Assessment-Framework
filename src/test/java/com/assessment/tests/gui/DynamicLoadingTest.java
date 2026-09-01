package com.assessment.tests.gui;

import static org.testng.Assert.assertEquals;

import com.assessment.core.DriverManager;
import com.assessment.pages.HomePage;
import com.assessment.utils.TestDataReader;
import org.testng.annotations.Test;

public class DynamicLoadingTest extends BaseGuiTest {
    @Test(groups = {"gui", "regression"})
    public void shouldDisplayHelloWorldAfterLoadingCompletes() {
        String actualText = new HomePage(DriverManager.getDriver())
                .open()
                .openDynamicLoading()
                .openExampleTwo()
                .startLoading()
                .waitUntilFinished()
                .loadedText();

        assertEquals(actualText, TestDataReader.get("dynamic.loading.expected.text"));
    }
}

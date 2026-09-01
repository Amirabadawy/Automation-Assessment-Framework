package com.assessment.pages;

import com.assessment.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DynamicLoadingPage extends BasePage<DynamicLoadingPage> {
    private final By exampleTwoLink = By.linkText("Example 2: Element rendered after the fact");

    public DynamicLoadingPage(WebDriver driver) {
        super(driver);
    }

    public DynamicLoadingExampleTwoPage openExampleTwo() {
        click(exampleTwoLink);
        return new DynamicLoadingExampleTwoPage(driver);
    }
}

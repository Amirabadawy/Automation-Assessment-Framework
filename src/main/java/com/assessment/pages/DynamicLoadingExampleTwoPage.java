package com.assessment.pages;

import com.assessment.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DynamicLoadingExampleTwoPage extends BasePage<DynamicLoadingExampleTwoPage> {
    private final By startButton = By.cssSelector("#start button");
    private final By loadingIndicator = By.id("loading");
    private final By finishText = By.cssSelector("#finish h4");

    public DynamicLoadingExampleTwoPage(WebDriver driver) {
        super(driver);
    }

    public DynamicLoadingExampleTwoPage startLoading() {
        click(startButton);
        return self();
    }

    public DynamicLoadingExampleTwoPage waitUntilFinished() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingIndicator));
        visible(finishText);
        return self();
    }

    public String loadedText() {
        return textOf(finishText);
    }
}

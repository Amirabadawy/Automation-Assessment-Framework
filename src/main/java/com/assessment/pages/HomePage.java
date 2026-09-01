package com.assessment.pages;

import com.assessment.core.BasePage;
import com.assessment.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage<HomePage> {
    private final By fileUploadLink = By.linkText("File Upload");
    private final By dynamicLoadingLink = By.linkText("Dynamic Loading");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        driver.get(ConfigReader.get("baseUrl"));
        return self();
    }

    public FileUploadPage openFileUpload() {
        click(fileUploadLink);
        return new FileUploadPage(driver);
    }

    public DynamicLoadingPage openDynamicLoading() {
        click(dynamicLoadingLink);
        return new DynamicLoadingPage(driver);
    }
}

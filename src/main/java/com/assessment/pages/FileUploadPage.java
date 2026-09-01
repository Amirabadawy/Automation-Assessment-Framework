package com.assessment.pages;

import com.assessment.core.BasePage;
import java.nio.file.Path;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FileUploadPage extends BasePage<FileUploadPage> {
    private final By fileInput = By.id("file-upload");
    private final By submitButton = By.id("file-submit");
    private final By uploadedFiles = By.id("uploaded-files");
    private final By successHeader = By.cssSelector("h3");

    public FileUploadPage(WebDriver driver) {
        super(driver);
    }

    public FileUploadPage uploadFile(Path filePath) {
        type(fileInput, filePath.toAbsolutePath().toString());
        return self();
    }

    public FileUploadPage submit() {
        click(submitButton);
        visible(uploadedFiles);
        return self();
    }

    public String uploadedFileName() {
        return textOf(uploadedFiles);
    }

    public String successMessage() {
        return textOf(successHeader);
    }
}

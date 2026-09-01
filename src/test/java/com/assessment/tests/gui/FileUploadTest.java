package com.assessment.tests.gui;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.assessment.core.DriverManager;
import com.assessment.pages.FileUploadPage;
import com.assessment.pages.HomePage;
import com.assessment.utils.TestDataReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FileUploadTest extends BaseGuiTest {
    @DataProvider(name = "uploadFiles", parallel = true)
    public Object[][] uploadFiles() {
        String fileName = TestDataReader.get("upload.file");
        return new Object[][]{{Paths.get(TestDataReader.get("upload.file.path"), fileName)}};
    }

    @Test(groups = {"gui", "smoke"}, dataProvider = "uploadFiles", retryAnalyzer = GuiRetryAnalyzer.class)
    public void shouldUploadImageSuccessfully(Path filePath) {
        assertTrue(Files.exists(filePath), "Upload test file should exist before test execution.");

        FileUploadPage fileUploadPage = new HomePage(DriverManager.getDriver())
                .open()
                .openFileUpload()
                .uploadFile(filePath)
                .submit();

        assertEquals(fileUploadPage.successMessage(), TestDataReader.get("upload.success.message"));
        assertEquals(fileUploadPage.uploadedFileName(), filePath.getFileName().toString());
    }
}

package com.assessment.reporting;

import com.assessment.core.DriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {
    private static final Path REPORT_PATH = Path.of("target", "extent-reports", "extent-report.html");
    private static final Path SCREENSHOT_DIR = Path.of("target", "extent-reports", "screenshots");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    private static ExtentReports extentReports;

    @Override
    public void onStart(ITestContext context) {
        synchronized (ExtentReportListener.class) {
            if (extentReports == null) {
                createReportDirectories();
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH.toString());
                sparkReporter.config().setDocumentTitle("Automation Assessment Results");
                sparkReporter.config().setReportName(context.getSuite().getName());

                extentReports = new ExtentReports();
                extentReports.attachReporter(sparkReporter);
                extentReports.setSystemInfo("Suite", context.getSuite().getName());
                extentReports.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
                extentReports.setSystemInfo("Headless", System.getProperty("headless", "false"));
            }
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        CURRENT_TEST.set(createTest(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        currentTest(result).pass("Test passed");
        CURRENT_TEST.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = currentTest(result);
        test.fail(result.getThrowable());

        Path screenshot = captureScreenshot(result);
        if (screenshot != null) {
            test.fail(
                    "Failure screenshot",
                    MediaEntityBuilder.createScreenCaptureFromPath(
                            REPORT_PATH.getParent().relativize(screenshot).toString())
                            .build());
        }

        CURRENT_TEST.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        currentTest(result).log(Status.SKIP, result.getThrowable());
        CURRENT_TEST.remove();
    }

    @Override
    public void onFinish(ITestContext context) {
        synchronized (ExtentReportListener.class) {
            if (extentReports != null) {
                extentReports.flush();
            }
        }
    }

    private static void createReportDirectories() {
        try {
            Files.createDirectories(REPORT_PATH.getParent());
            Files.createDirectories(SCREENSHOT_DIR);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to create report directories.", exception);
        }
    }

    private static ExtentTest currentTest(ITestResult result) {
        ExtentTest test = CURRENT_TEST.get();
        if (test == null) {
            test = createTest(result);
            CURRENT_TEST.set(test);
        }
        return test;
    }

    private static ExtentTest createTest(ITestResult result) {
        return extentReports.createTest(result.getMethod().getQualifiedName());
    }

    private static Path captureScreenshot(ITestResult result) {
        try {
            if (!DriverManager.hasDriver()) {
                return null;
            }

            WebDriver driver = DriverManager.getDriver();
            if (!(driver instanceof TakesScreenshot screenshotDriver)) {
                return null;
            }

            byte[] screenshotBytes = screenshotDriver.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Failure screenshot", new ByteArrayInputStream(screenshotBytes));

            String screenshotName = result.getMethod().getMethodName()
                    + "-"
                    + LocalDateTime.now().format(TIMESTAMP_FORMAT)
                    + ".png";
            Path screenshotPath = SCREENSHOT_DIR.resolve(screenshotName);
            Files.write(screenshotPath, screenshotBytes);
            return screenshotPath;
        } catch (RuntimeException | IOException exception) {
            currentTest(result).warning("Unable to capture failure screenshot: " + exception.getMessage());
            return null;
        }
    }
}

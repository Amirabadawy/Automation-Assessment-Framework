package com.assessment.core;

import com.assessment.utils.ConfigReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.Locale;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class BrowserFactory {
    private BrowserFactory() {
    }

    public static WebDriver createDriver() {
        String browser = ConfigReader.get("browser").toLowerCase(Locale.ROOT);
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));
        String remoteUrl = ConfigReader.getOptional("remoteUrl");

        WebDriver driver = remoteUrl.isBlank()
                ? createLocalDriver(browser, headless)
                : createRemoteDriver(remoteUrl, browser, headless);

        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().window().maximize();
        return driver;
    }

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        return switch (browser) {
            case "firefox" -> new FirefoxDriver(firefoxOptions(headless));
            case "edge" -> new EdgeDriver(edgeOptions(headless));
            case "chrome" -> new ChromeDriver(chromeOptions(headless));
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private static WebDriver createRemoteDriver(String remoteUrl, String browser, boolean headless) {
        try {
            return switch (browser) {
                case "firefox" -> new RemoteWebDriver(URI.create(remoteUrl).toURL(), firefoxOptions(headless));
                case "edge" -> new RemoteWebDriver(URI.create(remoteUrl).toURL(), edgeOptions(headless));
                case "chrome" -> new RemoteWebDriver(URI.create(remoteUrl).toURL(), chromeOptions(headless));
                default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
            };
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException("Invalid remoteUrl: " + remoteUrl, exception);
        }
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--disable-gpu", "--window-size=1440,900");
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--disable-gpu", "--window-size=1440,900");
        return options;
    }
}

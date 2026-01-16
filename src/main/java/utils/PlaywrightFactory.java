package utils;

import com.microsoft.playwright.*;

public class PlaywrightFactory {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public Page initBrowser() {
        playwright = Playwright.create();
        browser = playwright.firefox().launch(
                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000)
        );
        context = browser.newContext();
        page = context.newPage();
        return page;
    }
}

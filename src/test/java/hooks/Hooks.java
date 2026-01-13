package hooks;

import com.microsoft.playwright.Page;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import utils.PlaywrightFactory;

public class Hooks {

    public static Page page;
    private PlaywrightFactory factory;

    @Before
    public void setup() {
        factory = new PlaywrightFactory();
        page = factory.initBrowser();
    }

    @After
    public void tearDown() {
        page.context().browser().close();
    }
}
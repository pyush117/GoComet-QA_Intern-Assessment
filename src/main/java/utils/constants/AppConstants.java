package utils.constants;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class AppConstants {
    public static  final  String BASE_URL="https://www.royalbrothers.com/";
    public static final String SEARCH_BAR="//input[@id='autocomplete-input']";
    public static final  String PICKUP_DATE_INPUT_FIELD="//input[@id='pickup-date-other']";
    public static final String SEARCH_BUTTON= "//div[@id='booking-pc']//button[@type='submit'][normalize-space()='Search']";
    public static final String CITY_CARD ="//div[@id='modal-content-scroll']//a[.//p[normalize-space()='%s'] and not(contains(@style,'display: none'))]";
    public static final String CITY_SEARCH_RESULTS="//a[contains(@class,'city-box-true') and not(contains(@style,'display: none'))]";
    public static final String CALENDER_HOLDER_VISIBLE = ".picker__holder:visible";
    public static final String DATE_OPTION = "div[aria-label='%s']";
    public static final String TIME_OPTION = "li[aria-label='%s']";
    public static final String PICKUP_DATE_DESK="//input[@id='pickup-date-desk']";
    public static final String DATE_FORMATTER="yyyy-MM-dd HH:mm:ss Z";
    public static final String DATE_PATTERN="dd MMM, yyyy";
    public static final String PICKUP_TIME_DESK="//input[@id='pickup-time-desk']";
    public static final String DROP_DATE_DESK="//input[@id='dropoff-date-desk']";
    public static final String DROP_TIME_DESK="//input[@id='dropoff-time-desk']";
    public static final String BIKE_CARDS="xpath=//div[contains(@class,'tarif-desc-body')]";
    public static final String BIKE_NAME="xpath=.//h6[contains(@class,'bike_name')]";
    public static final String BIKE_LOCATION_TITLE = "xpath=.//span[contains(@class,'dropdown-selected')]";
    public static final String BIKE_LOCATION_PLACEHOLDER = "xpath=.//span[contains(@class,'dropdown-placeholder') and normalize-space()='Select pickup location']";
    public static final String BIKE_SOLD_OUT_TEXT = "xpath=.//span[normalize-space()='Sold Out!']";
    public static final String BIKE_PRICE = "xpath=.//span[@id='rental_amount']";
    public static final String APPLY_FILTER_BUTTON="//button[normalize-space()='Apply filter']";
    public static final String FILTER_CONTAINER = "#filter";
    public static final String LOCATION_SEARCH_INPUT="//input[@placeholder='Search Location']";
    public static final String LOCATION_SEARCH_RESULT="(//ul//li[not(contains(@style,'display: none'))]//label[.//input[@name='location[]']])[1]";
    public static final String UNAVILABLE_MESSAGE_LOCATION="(//span[@class='font16'])[1]";
    public static final String MONTH_MOVER   = "xpath=.//div[@role='button' and contains(@class,'picker__nav--next')]";
    public static final String CALENDAR_MONTH_TEXT = "xpath=.//div[contains(@class,'picker__month')]";
    public static final String CALENDAR_YEAR_TEXT  = "xpath=.//div[contains(@class,'picker__year')]";
    public static final String SEARCH_ERROR_MESSAGE="xpath=//*p[normalize-sapce()='please select date!']";
  

    public static String formatSelectedDate(String rawDate) {
        if (rawDate == null || rawDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Raw date is null/empty");
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return ZonedDateTime.parse(rawDate, inputFormatter).format(outputFormatter);
    }

    public static void selectDateWithMonthMove(Page page,Locator calendar, LocalDate expectedDate, String rawDate) {

        String expectedMonth = expectedDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        String expectedYear = String.valueOf(expectedDate.getYear());
        Locator monthText = calendar.locator(AppConstants.CALENDAR_MONTH_TEXT);
        Locator yearText = calendar.locator(AppConstants.CALENDAR_YEAR_TEXT);
        Locator nextBtn = calendar.locator(AppConstants.MONTH_MOVER);

        int maxMoves = 15;
        for (int i = 0; i < maxMoves; i++) {
            String currentMonth = monthText.innerText().trim();
            String currentYear = yearText.innerText().trim();

            if (currentMonth.equalsIgnoreCase(expectedMonth) && currentYear.equals(expectedYear)) {
                break;
            }
            nextBtn.click();
            page.waitForTimeout(200);
        }

        Assert.assertTrue(
                monthText.innerText().trim().equalsIgnoreCase(expectedMonth)
                        && yearText.innerText().trim().equals(expectedYear),
                "Calendar did not reach expected month/year: " + expectedMonth + " " + expectedYear
        );

        Locator dateOption = calendar.locator(String.format(AppConstants.DATE_OPTION, rawDate));
        Assert.assertNotEquals(dateOption.count(), 0, "Date not found: " + rawDate);

        String disabled = dateOption.first().getAttribute("aria-disabled");
        Assert.assertFalse("true".equalsIgnoreCase(disabled), "Date is disabled: " + rawDate);

        dateOption.first().click();
    }

    public static void selectTimeWithScroll(Page page, String time) {

        Locator timeOption = page.locator(AppConstants.CALENDER_HOLDER_VISIBLE)
                .locator(String.format(AppConstants.TIME_OPTION, time));

        if (timeOption.count() == 0 || !timeOption.first().isVisible()) {
            page.locator(AppConstants.CALENDER_HOLDER_VISIBLE)
                    .evaluate("el => el.scrollTop = el.scrollTop + 400");
            page.waitForTimeout(300);
        }

        Locator timeOptionAgain = page.locator(AppConstants.CALENDER_HOLDER_VISIBLE)
                .locator(String.format(AppConstants.TIME_OPTION, time));

        Assert.assertNotEquals(timeOptionAgain.count(), 0, "Time not found: " + time);
        timeOptionAgain.first().click();
    }


}



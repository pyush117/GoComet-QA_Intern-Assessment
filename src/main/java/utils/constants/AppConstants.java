package utils.constants;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AppConstants {
    public static  final  String BASE_URL="https://www.royalbrothers.com/";
    public static final String SEARCH_BAR="//input[@id='autocomplete-input']";
    public static final  String PICKUP_DATE_INPUT_FIELD="//input[@id='pickup-date-other']";
    public static final String SEARCH_BUTTON= "//div[@id='booking-pc']//button[@type='submit'][normalize-space()='Search']";
    public static final String CITY_CARD="//a[contains(@class,'city-box-true')]" + "[not(contains(@style,'display: none'))]";
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
    public static final String BIKE_LOCATION_ADDRESS = "xpath=.//div[contains(@class,'option-address')]";
    public static final String BIKE_LOCATION_PLACEHOLDER = "xpath=.//span[contains(@class,'dropdown-placeholder') and normalize-space()='Select pickup location']";
    public static final String BIKE_SOLD_OUT_TEXT = "xpath=.//span[normalize-space()='Sold Out!']";
    public static final String BIKE_PRICE = "xpath=.//span[@id='rental_amount']";
    public static final String APPLY_FILTER_BUTTON="//button[normalize-space()='Apply filter']";
    public static final String FILTER_CONTAINER = "#filter";
    public static final String LOCATION_LABEL = "xpath=//label[starts-with(@for,'loc_') and normalize-space()='%s']";
    public static final String UNAVILABLE_MESSAGE= "xpath=//*[contains(normalize-space(),\"%s\")]";
    public static final String CHECKBOX="input[type='checkbox']";
    public static final String MONTH_MOVER="//div[contains(@class,'picker--opened')]//div[@class='picker__nav--next' and @data-nav='1']";

    public static String formatSelectedDate(String rawDate) {
        if (rawDate == null || rawDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Raw date is null/empty");
        }
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return ZonedDateTime.parse(rawDate, inputFormatter).format(outputFormatter);
    }
}



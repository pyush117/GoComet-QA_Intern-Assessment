package pages;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.cucumber.java.it.Ma;
import org.testng.Assert;
import utils.constants.AppConstants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class RoyalBrothers {
    Page page;
    private boolean noBikesFound=false;
    private final List<String> availableBikes = new ArrayList<>();
    public RoyalBrothers(Page page) {
        this.page = page;
    }
    public void landing() {
        page.navigate(AppConstants.BASE_URL);
        Assert.assertEquals(page.url(), AppConstants.BASE_URL, "User did not land on Royal Brothers home page");
    }
    public void searchAndSelectCity(String city) {
        Locator searchBarLocator = page.locator(AppConstants.SEARCH_BAR);
        searchBarLocator.fill(city);
        Assert.assertTrue(validateSearch(city), "City search validation failed for: " + city);
        page.locator(String.format(AppConstants.CITY_CARD,city)).first().click();
        page.waitForTimeout(2000);
        String currentUrl = page.url();
        Assert.assertTrue(currentUrl.toLowerCase().contains(city.toLowerCase()),
                "City not applied in URL. Expected city in URL: " + city + " | Found URL: " + currentUrl);
    }
    public boolean validateSearch(String city) {
        Locator visibleCities = page.locator(AppConstants.CITY_SEARCH_RESULTS);
        int count = visibleCities.count();
        if (count == 0) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            String displayedCity = visibleCities.nth(i).locator("p").innerText().trim();
            if (displayedCity.equalsIgnoreCase(city)) {
                return true;
            }
        }
        return false;
    }
    public void setPickAndDropInfo(String pickupDate, String pickupTime, String dropDate, String dropTime) {

    Assert.assertFalse(pickupDate == null || pickupDate.trim().isEmpty(), "Pickup date is null/empty");
    Assert.assertFalse(pickupTime == null || pickupTime.trim().isEmpty(), "Pickup time is null/empty");
    Assert.assertFalse(dropDate == null || dropDate.trim().isEmpty(), "Drop date is null/empty");
    Assert.assertFalse(dropTime == null || dropTime.trim().isEmpty(), "Drop time is null/empty");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.ENGLISH);
    LocalDate pickDate = LocalDate.parse(pickupDate.trim(), formatter);
    LocalDate dropD = LocalDate.parse(dropDate.trim(), formatter);
    LocalDate today = LocalDate.now();
    Assert.assertFalse(pickDate.isBefore(today), "Pickup date is past: " + pickupDate);
    Assert.assertFalse(dropD.isBefore(pickDate), "Drop date cannot be before pickup date");
    page.locator(AppConstants.PICKUP_DATE_INPUT_FIELD).click();
    Locator calendar = page.locator(AppConstants.CALENDER_HOLDER_VISIBLE);
    AppConstants.selectDateWithMonthMove(page,calendar, pickDate, pickupDate.trim());
    AppConstants.selectTimeWithScroll(page,pickupTime.trim());
    AppConstants.selectDateWithMonthMove(page,calendar, dropD, dropDate.trim());
    AppConstants.selectTimeWithScroll(page,dropTime.trim());
}
    public boolean shouldContinue() {
        Locator noBikesMsg = page.locator(AppConstants.UNAVILABLE_MESSAGE_LOCATION);
        if (noBikesMsg.count() > 0) {
            System.out.println(noBikesMsg.innerText());
            return false;
        }
        return true;
    }
    public void clickSearch() {
        page.locator(AppConstants.SEARCH_BUTTON).click();
        if(!shouldContinue()) noBikesFound=true;
        Locator locateError=page.locator(AppConstants.SEARCH_ERROR_MESSAGE);
        String message=locateError.innerText().trim();
        System.out.println(message);
        Assert.assertNotEquals(locateError.innerText(), "please select date!");


    }
    public void validatePickupAndDropOff(String pickupDate, String pickupTime, String dropDate, String dropTime) {

        if (noBikesFound) return;

        String pickupRawDate = page.locator(AppConstants.PICKUP_DATE_DESK).getAttribute("data-selected");
        Assert.assertNotNull(pickupRawDate, "Pickup date not found");
        String actualPickupDate = AppConstants.formatSelectedDate(pickupRawDate);
        Assert.assertEquals(actualPickupDate, pickupDate, "Pickup date mismatch. Expected: " + pickupDate + ", Found: " + actualPickupDate);
        String actualPickupTime = page.locator(AppConstants.PICKUP_TIME_DESK).inputValue().trim();
        Assert.assertEquals(actualPickupTime, pickupTime, "Pickup time mismatch. Expected: " + pickupTime + ", Found: " + actualPickupTime);
        String dropRawDate = page.locator(AppConstants.DROP_DATE_DESK).getAttribute("data-selected");
        Assert.assertNotNull(dropRawDate, "DropOff date not found");
        String actualDropDate = AppConstants.formatSelectedDate(dropRawDate);
        Assert.assertEquals(actualDropDate, dropDate, "DropOff date mismatch. Expected: " + dropDate + ", Found: " + actualDropDate);
        String actualDropTime = page.locator(AppConstants.DROP_TIME_DESK).inputValue().trim();
        Assert.assertEquals(actualDropTime, dropTime, "DropOff time mismatch. Expected: " + dropTime + ", Found: " + actualDropTime);
    }
    public void applyFilter(String locationName) {

    if (noBikesFound) return;


    String[] locations = locationName.split(",");

    Locator filterContainer = page.locator(AppConstants.FILTER_CONTAINER);

    for (String loc : locations) {
        String trimmedLoc = loc.trim();
        if (trimmedLoc.isEmpty()) continue;
        Locator searchInput = page.locator(AppConstants.LOCATION_SEARCH_INPUT);
        searchInput.click();
        searchInput.press("Control+A");
        searchInput.press("Backspace");
        searchInput.pressSequentially(trimmedLoc);
        Locator firstResult = page.locator(AppConstants.LOCATION_SEARCH_RESULT);
        firstResult.waitFor();
        firstResult.click();
        Locator checkbox = firstResult.locator(AppConstants.LOCATION_SEARCH_RESULT);
        if (checkbox.count() > 0) {
            checkbox.check();Assert.assertTrue(checkbox.isChecked(), "Checkbox not checked for location: " + trimmedLoc);
        }
    }
    Locator clickFilter = page.locator(AppConstants.APPLY_FILTER_BUTTON);
    clickFilter.click();
}
    public void collectBikesData(String expectedLocations) {

        if (noBikesFound) return;
        String[] selectedLocations = expectedLocations.split(",");
        Locator cards = page.locator(AppConstants.BIKE_CARDS);
        int count = cards.count();
        if (count == 0) {
            System.out.println("No available Bikes");
            return;
        }
        System.out.println("Total cards found: " + count);
        availableBikes.clear();
        for (int i = 0; i < count; i++) {
            Locator card = cards.nth(i);
            card.scrollIntoViewIfNeeded();
            if (card.locator(AppConstants.BIKE_SOLD_OUT_TEXT).count() > 0 || card.locator(AppConstants.BIKE_LOCATION_PLACEHOLDER).count() > 0) {
                continue;
            }
            Locator titleLoc = card.locator(AppConstants.BIKE_LOCATION_TITLE);

            if (titleLoc.count() == 0) {
                continue;
            }
            String bikeName = card.locator(AppConstants.BIKE_NAME).innerText().trim();
            String price = card.locator(AppConstants.BIKE_PRICE).innerText().trim();
            String Title = titleLoc.innerText().trim();
            boolean matched = false;
            for (String loc : selectedLocations) {
                String expected = loc.trim();
                if (expected.contains(Title)) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                continue;
            }
            availableBikes.add("Bike: " + bikeName + " | Available at: " + Title + " | Price: " + price);

        }
        System.out.println("\n========== BIKES AVAILABLE AT SELECTED LOCATIONS ==========");
        if (availableBikes.isEmpty()) {
            System.out.println("No bikes available at selected locations.");
        } else {
            for (String element : availableBikes) {
                System.out.println(element);
            }

        }
    }







}

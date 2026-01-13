package pages;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import utils.constants.AppConstants;
import java.util.ArrayList;
import java.util.List;

public class RoyalBrothers {
    Page page;
    private final List<String> availableBikes = new ArrayList<>();
    private final List<String> notAvailableBikes = new ArrayList<>();
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
        page.locator(AppConstants.CITY_CARD).first().click();
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
    public void setPickUpInfo(String date, String time) {
        page.locator(AppConstants.PICKUP_DATE_INPUT_FIELD).click();
        page.locator(AppConstants.CALENDER_HOLDER_VISIBLE).locator(String.format(AppConstants.DATE_OPTION, date)).click();
        page.locator(AppConstants.CALENDER_HOLDER_VISIBLE).locator(String.format(AppConstants.TIME_OPTION, time)).click();
    }
    public void setDropInfo(String date, String time) {
        page.locator(AppConstants.CALENDER_HOLDER_VISIBLE).locator(String.format(AppConstants.DATE_OPTION, date)).click();
        page.locator(AppConstants.CALENDER_HOLDER_VISIBLE).locator(String.format(AppConstants.TIME_OPTION, time)).click();
    }
    public void clickSearch() {
        page.locator(AppConstants.SEARCH_BUTTON).click();
        page.waitForTimeout(4000);
    }
    public void validatePickup(String date, String time) {
        String pickupDateSelected = page.locator(AppConstants.PICKUP_DATE_DESK)
                .getAttribute("data-selected");
        if (pickupDateSelected == null) {
            throw new AssertionError("Pickup date not found");
        }
        String formattedPickupDate = AppConstants.formatSelectedDate(pickupDateSelected);
        if (!formattedPickupDate.equals(date)) {
            throw new AssertionError(
                    "Pickup date mismatch. Expected: " + date +
                            ", Found: " + formattedPickupDate
            );
        }
        String pickupTimeSelected =
                page.locator(AppConstants.PICKUP_TIME_DESK)
                        .inputValue()
                        .trim();
        if (!pickupTimeSelected.equals(time)) {
            throw new AssertionError(
                    "Pickup time mismatch. Expected: " + time +
                            ", Found: " + pickupTimeSelected
            );
        }
    }
    public void validateDropOff(String date, String time) {
        String dropOffDateSelected =
                page.locator(AppConstants.DROP_DATE_DESK)
                        .getAttribute("data-selected");
        if (dropOffDateSelected == null) {
            throw new AssertionError("DropOff date not found");
        }
        String formattedDropOffDate = AppConstants.formatSelectedDate(dropOffDateSelected);
        if (!formattedDropOffDate.equals(date)) {
            throw new AssertionError(
                    "DropOff date mismatch. Expected: " + date +
                            ", Found: " + formattedDropOffDate
            );
        }
        String dropOffTimeSelected =
                page.locator(AppConstants.DROP_TIME_DESK)
                        .inputValue()
                        .trim();
        if (!dropOffTimeSelected.equals(time)) {
            throw new AssertionError(
                    "DropOff time mismatch. Expected: " + time +
                            ", Found: " + dropOffTimeSelected
            );
        }
    }
    public void applyFilter(String locationName) {
        Locator filterContainer = page.locator(AppConstants.FILTER_CONTAINER);
        Locator location = page.locator(String.format(AppConstants.LOCATION_LABEL, locationName));
        int maxScrolls = 15;
        int step = 300;
        boolean found = false;
        for (int i = 0; i < maxScrolls; i++) {
            if (location.count() > 0) {
                found = true;
                break;
            }
            filterContainer.evaluate("el => el.scrollTop = el.scrollTop + " + step);
        }
        Assert.assertTrue(found, "Location not found in filter list: " + locationName);
        location.scrollIntoViewIfNeeded();
        location.click();
        Locator checkbox = location.locator(AppConstants.CHECKBOX);
        checkbox.check();
        Assert.assertTrue(checkbox.isChecked(),
                "Checkbox not checked for location: " + locationName);
        Locator clickFilter = page.locator(AppConstants.APPLY_FILTER_BUTTON);
        clickFilter.click();
    }
    public void collectBikesData(String expectedLocation) {
        Locator cards = page.locator(AppConstants.BIKE_CARDS);
        int count = cards.count();
        System.out.println("Total cards found: " + count);
        for (int i = 0; i < count; i++) {
            Locator card = cards.nth(i);
            card.scrollIntoViewIfNeeded();
            String bikeName = card.locator(AppConstants.BIKE_NAME).innerText();
            String price = card.locator(AppConstants.BIKE_PRICE).innerText();
            if (card.locator(AppConstants.BIKE_SOLD_OUT_TEXT).count() > 0) {
                notAvailableBikes.add("Bike: " + bikeName + " | Price: " + price + " | Reason: Sold Out!");
                continue;
            }
            if (card.locator(AppConstants.BIKE_LOCATION_PLACEHOLDER).count() > 0) {
                notAvailableBikes.add("Bike: " + bikeName + " | Price: " + price + " | Reason: Select pickup location (placeholder)");
                continue;
            }
            Locator titleLoc = card.locator(AppConstants.BIKE_LOCATION_TITLE);
            Locator addressLoc = card.locator(AppConstants.BIKE_LOCATION_ADDRESS);
            if (titleLoc.count() == 0 || addressLoc.count() == 0) {
                notAvailableBikes.add("Bike: " + bikeName + " | Price: " + price + " | Reason: Location details missing");
                continue;
            }
            String actualTitle = titleLoc.innerText();
            String actualAddress = addressLoc.innerText();
            String actualFullLocation = actualTitle + " (" + actualAddress + ")";
            if (!expectedLocation.contains(actualTitle) || !expectedLocation.contains(actualAddress)) {
                notAvailableBikes.add("Bike: " + bikeName + " | Price: " + price + " | Found Location: " + actualFullLocation + " | Expected Location: " + expectedLocation);
                continue;
            }
            availableBikes.add("Bike: " + bikeName + " | Available at: " + actualFullLocation + " | Price: " + price);
        }
    }
    public void printBikes() {
        System.out.println("\n========== BIKES AVAILABLE AT SELECTED LOCATION ==========");
        if (availableBikes.isEmpty()) {
            System.out.println("No bikes available at selected location.");
        } else {
            for (String bike : availableBikes) {
                System.out.println(bike);
            }
        }
        System.out.println("\n========== BIKES NOT AVAILABLE AT SELECTED LOCATION ==========");
        if (notAvailableBikes.isEmpty()) {
            System.out.println("All bikes are available at selected location.");
        } else {
            for (String bike : notAvailableBikes) {
                System.out.println(bike);
            }
        }
    }
    public void validateCollectedData(String locationName){
        int countUnAvilableBikes=notAvailableBikes.size();
        Assert.assertEquals(countUnAvilableBikes, 0, "Some bikes are NOT available at " + locationName + " or Sold Out!:\n");
    }
    public void unAvilableBikes(String expectedMessage) {
        Locator noBikesMsg = page.locator(String.format(AppConstants.UNAVILABLE_MESSAGE, expectedMessage)
        );
        Assert.assertTrue(noBikesMsg.count() > 0, "Expected message not found: " + expectedMessage);
    }


}

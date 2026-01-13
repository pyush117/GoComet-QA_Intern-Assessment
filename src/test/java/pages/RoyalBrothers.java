package pages;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import utils.constants.AppConstants;

public class RoyalBrothers {
    Page page;
    public RoyalBrothers(Page page) {
        this.page = page;
    }
    public void landing() {
        page.navigate(AppConstants.BASE_URL);
        String title = page.title();
        Assert.assertEquals(
                page.url(),
                AppConstants.BASE_URL,
                "User did not land on Royal Brothers home page"
        );
    }
    public void searchAndSelectCity(String city) {
        Locator searchBarLocator = page.locator(AppConstants.SEARCH_BAR);
        searchBarLocator.fill(city);
        Assert.assertTrue(
                validateSearch(city),
                "City search validation failed for: " + city
        );
        if (validateSearch(city)) {
            page.locator(
                    AppConstants.CITY_CARD
            ).first().click();
        } else {
            throw new AssertionError("City search validation failed for: " + city);
        }
    }
    public boolean validateSearch(String city) {
        Locator visibleCities = page.locator(
                AppConstants.CITY_SEARCH_RESULTS
        );
        int count = visibleCities.count();
        if (count == 0) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            String displayedCity = visibleCities
                    .nth(i)
                    .locator("p")
                    .innerText()
                    .trim();
            if (displayedCity.equalsIgnoreCase(city)) {
                return true;
            }
        }
        return false;
    }
    public void setPickUpInfo(String date, String time) {
        page.locator(AppConstants.PICKUP_DATE_INPUT_FIELD).click();
        page.locator(AppConstants.PICKER_HOLDER_VISIBLE)
                .locator(String.format(AppConstants.PICKUP_DATE_OPTION, date)).click();
        page.locator(AppConstants.PICKER_HOLDER_VISIBLE)
                .locator(String.format(AppConstants.PICKUP_TIME_OPTION, time)).click();
    }
    public void setDropInfo(String date, String time) {
        page.locator(AppConstants.PICKER_HOLDER_VISIBLE)
                .locator(String.format(AppConstants.PICKUP_DATE_OPTION, date))
                .click();
        page.locator(AppConstants.PICKER_HOLDER_VISIBLE)
                .locator(String.format(AppConstants.PICKUP_TIME_OPTION, time))
                .click();
    }
    public void clickSearch() {
        page.locator(AppConstants.SEARCH_BUTTON).click();
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
        filterContainer.evaluate("el => el.scrollTop = el.scrollHeight");
        Locator location = page.locator(String.format(AppConstants.LOCATION_LABEL, locationName));
        Assert.assertTrue(location.count() > 0,
                "Location not found in filter list: " + locationName);
        location.scrollIntoViewIfNeeded();
        location.click();
        Locator checkbox = location.locator(AppConstants.CHECKBOX);
        checkbox.check();
        Assert.assertTrue(checkbox.isChecked(),
                "Checkbox not checked for location: " + locationName);
        Locator clickFilter = page.locator(AppConstants.APPLY_FILTER_BUTTON);
        clickFilter.click();
    }
    public void validateAndPrintBikes(String expectedLocation) {
        String shortLocation = expectedLocation.split("\\(")[0].trim();
        Locator cards = page.locator(AppConstants.BIKE_CARDS);
        int count = cards.count();
        System.out.println("Total cards found: " + count);
        for (int i = 0; i < count; i++) {
            Locator card = cards.nth(i);
            card.scrollIntoViewIfNeeded();
            Locator locationLoc = card.locator(
                    AppConstants.BIKE_LOCATION);
            if (locationLoc.count() == 0) continue;
            String location = locationLoc.innerText().trim();
            if (!location.contains(shortLocation)) continue;
            String bikeName = card
                    .locator(AppConstants.BIKE_NAME)
                    .innerText()
                    .trim();
            String pickupTime = card
                    .locator(AppConstants.BIKE_PICKUP_TIME)
                    .innerText()
                    .trim();
            String dropTime = card.locator(AppConstants.BIKE_DROP_TIME)
                    .innerText().trim();
            System.out.println("Bike: " + bikeName + " | Pickup time: " + pickupTime + "| Drop time: " + dropTime);
        }
    }
    public void unAvilableBikes(String expectedMessage) {
        Locator noBikesMsg = page.locator(String.format(AppConstants.UNAVILABLE_MESSAGE, expectedMessage)
        );
        Assert.assertTrue(noBikesMsg.count() > 0, "Expected message not found: " + expectedMessage);
    }
}

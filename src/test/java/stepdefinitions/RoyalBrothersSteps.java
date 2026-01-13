package stepdefinitions;
import hooks.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.RoyalBrothers;

public class RoyalBrothersSteps {
    RoyalBrothers royalBrothers;

    @Given("user navigates to Royal Brothers website")
    public void user_navigates_to_Royal_Brothers_website() {
        royalBrothers = new RoyalBrothers(Hooks.page);
        royalBrothers.landing();
    }

    @When("user searches and selects the city {string}")
    public void user_searches_and_selects_the_city(String city) {
        royalBrothers.searchAndSelectCity(city);
    }

    @And("user selects pickup {string} and {string}")
    public void user_selects_pickup_date_and_time(String date, String time) {
        royalBrothers.setPickUpInfo(date, time);
    }

    @And("user selects drop {string} and {string}")
    public void user_selects_drop_date_and_time(String date, String time) {
        royalBrothers.setDropInfo(date, time);
    }

    @And("user clicks on search")
    public void user_clicks_on_search() {
        royalBrothers.clickSearch();
    }

    @Then("user validate {string} date {string} and time {string} should be visible")
    public void selected_pickUp_date_and_time_should_be_visible(String type, String date, String time) {
        if (type.equalsIgnoreCase("pickUp")) {
            royalBrothers.validatePickup(date, time);
        } else {
            royalBrothers.validateDropOff(date, time);
        }
    }

    @When("user applies location filter {string}")
    public void user_applies_location_filter(String location) {
        royalBrothers.applyFilter(location);
    }

    @Then("all listed bikes should belong to {string}")
    public void all_listed_bikes_should_belong_to_location(String location) {
        royalBrothers.validateAndPrintBikes(location);
    }

    @Then("user should see no bikes message {string}")
    public void user_should_see_no_bikes_message(String expectedMessage) {
        royalBrothers.unAvilableBikes(expectedMessage);
    }



}

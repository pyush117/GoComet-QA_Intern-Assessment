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

    @Then("user applies location filter {string}")
    public void user_applies_location_filter(String location) {
        royalBrothers.applyFilter(location);
    }

    @And( "user should collect all the data of bikes with {string}")
    public void user_should_collect_all_the_data_of_bikes_with_location(String location) {
        royalBrothers.collectBikesData(location);
    }

    @Then("user should print bikes data")
    public  void user_should_print_bikes_data(){
        royalBrothers.printBikes();
    }

    @Then("user should validate the data at {string}")
    public void user_should_validate_the_data(String locationName){
        royalBrothers.validateCollectedData(locationName);
    }
     // ============ Negative case ==========
    @Then("user should see no bikes message {string}")
    public void user_should_see_no_bikes_message(String expectedMessage) {
        royalBrothers.unAvilableBikes(expectedMessage);
    }
}

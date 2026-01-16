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

    @And("user selects pickup date {string} time {string} and dropOff date {string} time {string}")
    public void user_selects_pickup_date_time_and_dropOff_date_time(String pickUpDate, String pickUpTime, String dropDate, String dropTime) {
        royalBrothers.setPickAndDropInfo(pickUpDate,pickUpTime,dropDate,dropTime);
    }
    @And("user clicks on search")
    public void user_clicks_on_search() {
        royalBrothers.clickSearch();
    }

    @Then("user validate pickup date {string} time {string} and dropOff date {string} time {string}")
    public void user_validate_pickup_date_time_and_dropOff_date_time(String pickupDate, String pickupTime, String dropDate, String dropTime) {
        royalBrothers.validatePickupAndDropOff(pickupDate,pickupTime,dropDate,dropTime);
    }

    @Then("user applies location filter {string}")
    public void user_applies_location_filter(String location) {
        royalBrothers.applyFilter(location);
    }

    @And( "user should collect and print all the data of bikes with {string}")
    public void user_should_collect_all_the_data_of_bikes_with_location(String location) {
        royalBrothers.collectBikesData(location);
    }


}

Feature: Search and validate bikes based on city and location
  Scenario Outline: User searches bikes for a city and validates results
    Given user navigates to Royal Brothers website
    When user searches and selects the city "<city>"
    And user clicks on search


    Examples:
      | city      | location                          | pickupDate   | pickupTime | dropDate    | dropTime |
      | Bangalore | Koramangala (Beside Sony Signal)  | 18 Jan, 2026  | 9:00 AM   | 19 Jan, 2026 | 10:00 AM |
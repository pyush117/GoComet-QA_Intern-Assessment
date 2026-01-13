Feature: Search and validate bikes based on city and location
  Scenario Outline: User searches bikes for a city with no availability
    Given user navigates to Royal Brothers website
    When user searches and selects the city "<city>"
    And user selects pickup "<pickupDate>" and "<pickupTime>"
    And user selects drop "<dropDate>" and "<dropTime>"
    And user clicks on search
    Then user should see no bikes message "We do not have any bikes in <city> currently."

    Examples:
      | city      |pickupDate   | pickupTime | dropDate    | dropTime  |
      | Manali | 26 Jan, 2026  | 9:00 AM   | 27 Jan, 2026 | 10:00 AM |


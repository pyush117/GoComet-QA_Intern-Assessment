Feature: Search and validate bikes based on city and location
  Scenario Outline: User searches bikes for a city and validates results
    Given user navigates to Royal Brothers website
    When user searches and selects the city "<city>"
    And user selects pickup "<pickupDate>" and "<pickupTime>"
    And user selects drop "<dropDate>" and "<dropTime>"
    And user clicks on search
    Then user validate "pickUp" date "<pickupDate>" and time "<pickupTime>" should be visible
    Then user validate "dropOff" date "<dropDate>" and time "<dropTime>" should be visible
    When user applies location filter "<location>"
    Then all listed bikes should belong to "<location>"

    Examples:
      | city      | location                         | pickupDate   | pickupTime | dropDate    | dropTime |
      | Bangalore | Koramangala (Beside Sony Signal) | 26 Jan, 2026  | 9:00 AM   | 27 Jan, 2026 | 10:00 AM |







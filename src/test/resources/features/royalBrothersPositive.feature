Feature: Search and validate bikes based on city and location
  Scenario Outline: User searches bikes for a city and validates results
    Given user navigates to Royal Brothers website
    When user searches and selects the city "<city>"
    And user selects pickup date "<pickupDate>" time "<pickupTime>" and dropOff date "<dropDate>" time "<dropTime>"
    And user clicks on search
    Then user validate pickup date "<pickupDate>" time "<pickupTime>" and dropOff date "<dropDate>" time "<dropTime>"
    Then user applies location filter "<location>"
    And  user should collect and print all the data of bikes with "<location>"



    Examples:
      | city      | location                          | pickupDate   | pickupTime | dropDate    | dropTime |
      | Bangalore | Koramangala (Beside Sony Signal)  | 18 Jan, 2026  | 9:00 AM   | 19 Jan, 2026 | 10:00 AM |
      | Manali    |  -                                | 26 Jan, 2026  | 9:00 AM   | 27 Jan, 2026 | 10:00 AM |








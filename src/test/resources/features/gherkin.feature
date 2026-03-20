Feature: In the web application, key tasks like adding competitors,
  entering results, viewing standings, saving and exporting results
  can be achieved with minimal steps.

  # TINA - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  Scenario: Adding a competitor
    Given the user is on webpage "localhost"
    And the name field in Add competitor is selected
    When a "name" has been entered into the field
    And user clicks the "add competitor" button
    Then the name should be visible in the Standings section

  Scenario: Entering results in 100m sprint for a competitor
    Given the user is on webpage "localhost:8080"
    And a "name" has been entered into the Name field
    And a "result" has been entered into the Result field
    When user clicks the "Save score" button
    Then the name and score is visible in Standings

  Scenario: Entering and exporting results
    Given the user is on webpage "localhost:8080"
    When user has entered a "name" and "result"
    And user clicks the "Export CSV" button
    Then the result file is exported
    And available to download result file

  # ANTON - - - - - - - - - - - - - - - - - - - - - - - - - - -


  # KIM - - - - - - - - - - - - - - - - - - - - - - - - - - - -


  # OSKAR - - - - - - - - - - - - - - - - - - - - - - - - - - -


  # PHYLLIS - - - - - - - - - - - - - - - - - - - - - - - - - -


  # SAM - - - - - - - - - - - - - - - - - - - - - - - - - - - -
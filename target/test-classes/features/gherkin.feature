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

  Scenario Outline: Invalid first name inputs
    Given the user is on webpage "localhost:8080"
    And a "name" has been entered into the Name field
    And a "<result>" has been entered into the Result field
    When user clicks the "Save score" button
    Then the name and score is visible in Standings

    Examples:
      | result     |
      | Anton123   |
      |            |
      | Anton!     |
      | Anton€     |
      | 123        |
      | %41        |


  # KIM - - - - - - - - - - - - - - - - - - - - - - - - - - - -


  # OSKAR - - - - - - - - - - - - - - - - - - - - - - - - - - -


  # PHYLLIS - - - - - - - - - - - - - - - - - - - - - - - - - -
Scenario: Calculating points for a specific event
  Given the user is on the calculator page
  And "100m (s)" is selected from the event dropdown
  And the result "10.55" is entered in the result field
  When user clicks the "Save score" button
  Then the message area should show "Saved"

  Scenario: Handling failed score entry
  Given the user is on the calculator page
  And "Anna" is entered in the result name field
  And a non-numeric value "abc" is entered in the resul field
  When user clicks the "Save score" button
  Then an error message "Score failed" should be displayed

  # SAM - - - - - - - - - - - - - - - - - - - - - - - - - - - -
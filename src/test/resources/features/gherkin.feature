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

  Scenario Outline: Add Decathlon scores for all events
    Given I have entered competitor name "Test Test"
    When I select "<event>" from the event dropdown
    And I enter "<result>" as result
    And I submit the score
    Then the points should be "<points>"

    Examples:
      | event            | result | points |
      | 100m             | 10.00  | 1096   |
      | Long Jump        | 800    | 1061   |
      | Shot Put         | 20.00  | 1100   |
      | High Jump        | 200    | 803    |
      | 400m             | 40.00  | 1333   |
      | 110m Hurdles     | 14.00  | 975    |
      | Discus Throw     | 70.00  | 1295   |
      | Pole Vault       | 500    | 910    |
      | Javelin Throw    | 90.00  | 1198   |
      | 1500m            | 200    | 1268   |

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
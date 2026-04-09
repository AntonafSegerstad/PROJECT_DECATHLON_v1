Feature: In the web application, key tasks like adding competitors,
  entering results, viewing standings, saving and exporting results
  can be achieved with minimal steps.

  # TINA - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  Scenario: Adding a competitor
    Given the user is on webpage "localhost"
    When the valid name "Test" has been entered into the name field
    And user clicks the add competitor button
    Then the name "Test" should be visible in the Standings section

  Scenario: Entering results in 100m sprint for a competitor
    Given the user is on webpage "localhost:8080"
    When the valid name "Test" has been entered into the name field
    And user clicks the add competitor button
    And the event "100m" has been selected in the event field
    And the result "10" has been entered into the Result field
    When user clicks the Save score button
    Then the name "Test" should be visible in the Standings section
    Then score "1096" is visible in Standings

  Scenario: Entering and exporting results
    Given the user is on webpage "localhost:8080"
    When the valid name "Test" has been entered into the name field
    And user clicks the add competitor button
    And the event "100m" has been selected in the event field
    And the result "10" has been entered into the Result field
    And user clicks the Save score button
    And user clicks the "Export CSV" button
    Then the CSV file is exported and downloaded

   # ANTON - - - - - - - - - - - - - - - - - - - - - - - - - - -

  Scenario Outline: Scoring Validation
    Given the user is on webpage "localhost:8080"
    And the event "<event>" has been selected in the event field
    And the result "<result>" has been entered into the Result field
    When user clicks the Save score button
    Then the correct "<points>" are presented for each event

    Examples:
      | event            | result | points |
      | 100m             | 10.00  | 1096   |
      | longJump         | 800    | 1061   |
      | shotPut          | 20.00  | 1100   |
      | highJump         | 200    | 803    |
      | 400m             | 40.00  | 1333   |
      | 110mHurdles      | 14.00  | 975    |
      | discusThrow      | 70.00  | 1295   |
      | poleVault        | 500    | 910    |
      | javelinThrow     | 90.00  | 1198   |
      | 1500m            | 200    | 1268   |


      # KIM - - - - - - - - - - - - - - - - - - - - - - - - - -


  # OSKAR - - - - - - - - - - - - - - - - - - - - - - - - - - -


  # PHYLLIS - - - - - - - - - - - - - - - - - - - - - - - - - -
  #Scenario: Calculating points for a specific event
   # Given the user is on the calculator page
   # And "100m (s)" is selected from the event dropdown
   # And the result "10.55" is entered in the result field
   # When user clicks the "Save score" button
   # Then the message area should show "Saved"

  #Scenario: Handling failed score entry
   # Given the user is on the calculator page
    #And "Anna" is entered in the result name field
    #And a non-numeric value "abc" is entered in the resul field
    #When user clicks the "Save score" button
    #Then an error message "Score failed" should be displayed

  # SAM - - - - - - - - - - - - - - - - - - - - - - - - - - - -

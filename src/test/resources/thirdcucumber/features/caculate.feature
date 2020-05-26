#Bai tap buoi 5: Viet automation test script (su dung Cucumber data driven) kiem thu 4 phep toan +, -, *, /
# cho ung dung https://www.desmos.com/scientific
Feature: Check 4 operators in Scientific
  Scenario Outline: The functionality of 4 operators in Scientific operates correctly
    Given The Scientific page is displayed on the browser
    When User get the "<sample>" data by reading the json data file
    And User attempt to perform the operator functionality
    Then The result of the operator matches with the "<sample>" result in the json data file
    Examples:
      |sample|
      |1     |
      |2     |
      |3     |
      |4     |
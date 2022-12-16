Feature: Parse a record and store the intake air temperature

  Scenario: Parse a single record with valid intake air temperature data
    Given a file with the following rows:
      | Device Time              | Intake Air Temperature(°F) |
      | 18-Sep-2022 14:15:47.968 | 123.8                      |
    When the file is parsed
    Then the following data log records will be created:
      | sessionId | timestamp | intakeAirTemperature |
      |           |           | 123.8                |
    And the application will log the following messages:
      | level | message      |
      | INFO  | Parsing file |

#  Scenario: Parse multiple records with valid intake air temperature data
#    Given a file with the following rows:
#      | Device Time              | Intake Air Temperature(°F) |
#      | 18-Sep-2022 14:15:47.968 | 123.8                      |
#      | 18-Sep-2022 14:15:48.962 | 130                        |
#    When the file is parsed
#    Then the following data log records will be created:
#      | sessionId | timestamp | intakeAirTemperature |
#      |           |           | 123.8                |
#    And the application will log the following messages:
#      | level | message      |
#      | INFO  | Parsing file |
#
#  Scenario: Skip records with with invalid intake air temperature data
#    Given a file with the following rows:
#      | Device Time              | Intake Air Temperature(°F) |
#      | 18-Sep-2022 14:15:47.968 | 123.8                      |
#      | 18-Sep-2022 14:15:48.962 | -                          |
#      | 18-Sep-2022 14:15:49.965 | 130                        |
#    When the file is parsed
#    Then the following data log records will be created:
#      | sessionId | timestamp | intakeAirTemperature |
#      |           |           | 123.8                |
#    And the application will log the following messages:
#      | level | message      |
#      | INFO  | Parsing file |
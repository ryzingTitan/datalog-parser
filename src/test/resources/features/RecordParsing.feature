Feature: Parse a record and store the session data

  Scenario: Parse a single record with valid session data
    Given a file with the following rows:
      | Device Time              | Intake Air Temperature(°F) | Turbo Boost & Vacuum Gauge(psi) |
      | 18-Sep-2022 14:15:47.968 | 123.8                      | 16.5                            |
    When the file is parsed
    Then the following data log records will be created:
      | sessionId                            | timestamp                | intakeAirTemperature | boostPressure |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | 123.8                | 16.5          |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |

  Scenario: Parse multiple records with valid session data
    Given a file with the following rows:
      | Device Time              | Intake Air Temperature(°F) | Turbo Boost & Vacuum Gauge(psi) |
      | 18-Sep-2022 14:15:47.968 | 123.8                      | 16.5                            |
      | 18-Sep-2022 14:15:48.962 | 130                        | 15                              |
    When the file is parsed
    Then the following data log records will be created:
      | sessionId                            | timestamp                | intakeAirTemperature | boostPressure |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | 123.8                | 16.5          |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:48.962Z | 130.0                | 15.0          |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |

  Scenario: Parse records with invalid session data
    Given a file with the following rows:
      | Device Time              | Intake Air Temperature(°F) | Turbo Boost & Vacuum Gauge(psi) |
      | 18-Sep-2022 14:15:47.968 | 123.8                      | -                               |
      | 18-Sep-2022 14:15:48.962 | -                          | 16.5                            |
      | 18-Sep-2022 14:15:49.965 | 130                        | 15.0                            |
    When the file is parsed
    Then the following data log records will be created:
      | sessionId                            | timestamp                | intakeAirTemperature | boostPressure |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | 123.8                |               |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:48.962Z |                      | 16.5          |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:49.965Z | 130.0                | 15.0          |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |
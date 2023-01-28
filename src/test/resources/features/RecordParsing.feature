Feature: Parse a record and store the session data

  Scenario: Parse a single record with valid session data
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude           | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004 | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            |
    When the file is parsed
    Then the following data log records will be created:
      | sessionId                            | timestamp                | longitude          | latitude           | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | -86.14170333333335 | 42.406800000000004 | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |

  Scenario: Parse multiple records with valid session data
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              |
    When the file is parsed
    Then the following data log records will be created:
      | sessionId                            | timestamp                | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:48.962Z | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 98                 | 2500      | 79    | 7.0              |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |

  Scenario: Parse records with invalid session data
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 166.2                          | -               | 123.8                      | 74.56            | 5.6                            | -                               |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 95.9                           | 3500.35         | -                          | -                | 7                              | 16.5                            |
      | 18-Sep-2022 14:15:49.965 | -86.14162          | 42.406800000000004  | 186.8    | -                              | 2500            | 130                        | 79               | -                              | 15.0                            |
    When the file is parsed
    Then the following data log records will be created:
      | sessionId                            | timestamp                | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:47.968Z | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  |               | 166                |           | 74    | 5.6              |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:48.962Z | 86.14162999999999  | -42.406816666666664 | 188.0    |                      | 16.5          | 95                 | 3500      |       | 7.0              |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 2022-09-18T18:15:49.965Z | -86.14162          | 42.406800000000004  | 186.8    | 130                  | 15.0          |                    | 2500      | 79    |                  |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |

Feature: Parse each record in a file and store the session data

  Scenario: Parse a single record with valid session data
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude           | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004 | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
    When the file is parsed
    Then the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude           | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004 | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |

  Scenario: Parse multiple records with valid session data
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.7                         |
    When the file is parsed
    Then the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 98                 | 2500      | 79    | 7.0              | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |

  Scenario: Parse records with invalid session data
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 166.2                          | -               | 123.8                      | 74.56            | 5.6                            | -                               | -                            |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 95.9                           | 3500.35         | -                          | -                | 7                              | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:49.965 | -86.14162          | 42.406800000000004  | 186.8    | -                              | 2500            | 130                        | 79               | -                              | 15.0                            | 14.8                         |
    When the file is parsed
    Then the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  |               | 166                |           | 74    | 5.6              |              | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    |                      | 16.5          | 95                 | 3500      |       | 7.0              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524949965     | -86.14162          | 42.406800000000004  | 186.8    | 130                  | 15.0          |                    | 2500      | 79    |                  | 14.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |

  Scenario: Update existing records
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.9                         |
    And the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 100                  | 16.5          | 95                 | 4500      | 74    | 5.6              | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    When the file is parsed
    Then the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email         |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 98                 | 2500      | 79    | 7.0              | 14.9         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |

  Scenario: Do not overwrite data for other users when parsing files
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.9                         |
    And the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email          |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 100                  | 16.5          | 95                 | 4500      | 74    | 5.6              | 14.7         | Test Track | 42.4086       | -86.1374       | test2     | tester   | test2@test.com |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test2     | tester   | test2@test.com |
    When the file is parsed
    Then the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email          |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 100                  | 16.5          | 95                 | 4500      | 74    | 5.6              | 14.7         | Test Track | 42.4086       | -86.1374       | test2     | tester   | test2@test.com |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test2     | tester   | test2@test.com |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | c61cc339-f93d-45a4-aa2b-923f0482b97f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 98                 | 2500      | 79    | 7.0              | 14.9         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |

  Scenario: Only update records that belong to the current user when parsing files
    Given a file with the following rows:
      | Device Time              | Longitude          | Latitude            | Altitude | Engine Coolant Temperature(°F) | Engine RPM(rpm) | Intake Air Temperature(°F) | Speed (OBD)(mph) | Throttle Position(Manifold)(%) | Turbo Boost & Vacuum Gauge(psi) | Air Fuel Ratio(Measured)(:1) |
      | 18-Sep-2022 14:15:47.968 | -86.14170333333335 | 42.406800000000004  | 188.4    | 95.9                           | 3500.35         | 123.8                      | 74.56            | 5.6                            | 16.5                            | 17.5                         |
      | 18-Sep-2022 14:15:48.962 | 86.14162999999999  | -42.406816666666664 | 188.0    | 98                             | 2500            | 130                        | 79               | 7                              | 15                              | 14.9                         |
    And the following datalogs exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email          |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 100                  | 16.5          | 95                 | 4500      | 74    | 5.6              | 14.7         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test2     | tester   | test2@test.com |
    When the file is parsed
    Then the following datalogs will exist:
      | sessionId                            | epochMilliseconds | longitude          | latitude            | altitude | intakeAirTemperature | boostPressure | coolantTemperature | engineRpm | speed | throttlePosition | airFuelRatio | trackName  | trackLatitude | trackLongitude | firstName | lastName | email          |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524947968     | -86.14170333333335 | 42.406800000000004  | 188.4    | 123                  | 16.5          | 95                 | 3500      | 74    | 5.6              | 17.5         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | d864a09b-a349-4ca7-bd69-f71ae1ba2371 | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 15.0          | 98                 | 2500      | 79    | 7.0              | 14.9         | Test Track | 42.4086       | -86.1374       | test      | tester   | test@test.com  |
      | 9628a8bb-0a44-4c31-af7d-a54ff16f080f | 1663524948962     | 86.14162999999999  | -42.406816666666664 | 188.0    | 130                  | 9.0           | 98                 | 1500      | 79    | 7.0              | 15.8         | Test Track | 42.4086       | -86.1374       | test2     | tester   | test2@test.com |
    And the application will log the following messages:
      | level | message                                       |
      | INFO  | Beginning to parse file: testFile.txt         |
      | INFO  | File parsing completed for file: testFile.txt |
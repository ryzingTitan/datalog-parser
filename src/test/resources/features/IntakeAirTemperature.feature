Feature: Parse a record and store the intake air temparature

  Scenario: With a Postgres database
    When a user runs the console application
    Then the application will log the following messages:
      | level | message      |
      | INFO  | Parsing file |

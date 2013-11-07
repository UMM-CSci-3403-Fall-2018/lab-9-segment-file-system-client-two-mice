@announce
Feature: Getting files from a server and putting them together
  In order to demonstrate my understanding of UDP networks
  As a student
  I want to be able to get three files from a remote server and put them together.

  Scenario: Sending text: premade client, your server
    When I run the file server client
    Then the file "small.txt" exists
    And the file "small.txt" should differ from "etc/small.txt" only by whitespace
    And the file "AsYouLikeIt.txt" exists
    And the file "AsYouLikeIt.txt" should differ from "etc/AsYouLikeIt.txt" only by whitespace
    And the file "binary.jpg" exists
    And the file "binary.jpg" should differ from "etc/binary.jpg" only by whitespace

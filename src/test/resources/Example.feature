Feature: Creating company tree

  I want to manage my company

  So I have to create a hierarchy tree

  Scenario: Send JSON request and create company tree

    Given I prepare JSON request with company tree

    When Sending request on company/create

    Then There should be returned status 201



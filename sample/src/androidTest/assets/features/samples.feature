Feature: Samples app

  Background:
    Given I see samples list

  @sample-scenario-01 @sharing-apps-installed
  Scenario: Showing a System activity chooser
    When I tap on the 'Share text with the stock Activity Chooser' button
    Then I should see a system activity chooser

  @sample-scenario-02 @sharing-apps-installed
  Scenario: Showing a default activity chooser
    When I tap on the 'Share text' button
    Then I should see an activity chooser with text 'Share via'
    And I should see at least one activity item
    And I should not see an empty view

  @sample-scenario-03
  Scenario: Dismissing activity chooser with BACK button
    Given I see an activity chooser with items
    When I tap the Back button
    Then activity chooser should be dismissed

  @sample-scenario-04
  Scenario: Dismissing activity chooser with a tap on the outside
    Given I see an activity chooser with items
    When I tap the content view
    Then activity chooser should be dismissed

  @sample-scenario-05
  Scenario: Dismissing activity chooser with swipe down
    Given I see an activity chooser with items
    When I swipe the bottom sheet down
    Then activity chooser should be dismissed

  @sample-scenario-06 @sharing-apps-installed
  Scenario: Showing an activity chooser with custom text
    When I tap on the 'Share text with title' button
    Then I should see an activity chooser with text 'Some custom title'
    And I should see at least one activity item
    And I should not see an empty view

  @sample-scenario-07 @sharing-apps-installed
  Scenario: Showing an activity chooser with custom text from resource
    When I tap on the 'Share text with title from resource' button
    Then I should see an activity chooser with text 'Custom title'
    And I should see at least one activity item
    And I should not see an empty view

  @sample-scenario-08 @sharing-apps-installed
  Scenario: Showing an activity chooser with custom style
    When I tap on the 'Share text with custom style without item animation' button
    Then I should see an activity chooser with text 'Share via'
    And I should see at least one activity item
    And I should not see an empty view
    And I should see a list with 2 columns

  @sample-scenario-09 @sharing-apps-installed
  Scenario: Tapping on an activity item
    Given I see an activity chooser with items
    When I tap on an activity item
    Then I should see the clicked activity

  @sample-scenario-10 @sharing-apps-installed
  Scenario: Long-tapping on an activity item
    Given I see an activity chooser with items
    When I long-tap on an activity item
    Then I should see the clicked activity's settings

  @sample-scenario-11 @facebook-app-installed @gmail-app-installed
  Scenario Template: Using alternative intents for selected apps
    Given I see an activity chooser with 'Share text with secondary intents' items
    When I tap on '<appName>' activity item
    Then I should see the clicked activity with package name: '<packageName>', extra title: '<extraTitle>' and extra text '<extraText>'

    Examples:
      | appName | packageName           | extraTitle              | extraText                                     |
      | Gmail   | com.google.android.gm | Secondary text to share | Secondary shared link: http://www.google.com  |
      | Facebook| com.facebook.katana   | Tertiary text to share  | Tertiary shared link: http://www.facebook.com |

  @sample-scenario-12 @pdf-reader-apps-installed
  Scenario: Showing a 'PDF reader' activity chooser
    When I tap on the 'Preview PDF' button
    Then I should see an activity chooser with text 'Preview PDF'
    And I should see at least one activity item
    And I should not see an empty view

  @sample-scenario-13 @pdf-reader-apps-installed
  Scenario: Tapping on a 'PDF reader' activity item
    Given I see an activity chooser with 'Preview PDF' items
    When I tap on an activity item
    Then I should see the clicked 'PDF reader' activity

  @sample-scenario-14 @sharing-apps-installed
  Scenario: Showing a 'tracking' activity chooser
    When I tap on the 'Track clicked items' button
    Then I should see an activity chooser with text 'Share via'
    And I should see at least one activity item
    And I should not see an empty view

  @sample-scenario-15 @sharing-apps-installed
  Scenario: Tapping on a 'tracking' activity item
    Given I see an activity chooser with 'Track clicked items' items
    When I tap on an activity item
    Then I should see a tracking message

  @sample-scenario-16
  Scenario: Showing an activity chooser with default empty view
    When I tap on the 'Show default empty view' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'Action couldn't be completed.'
    And I should not see an action button in empty view

  @sample-scenario-17
  Scenario: Showing an activity chooser with custom empty view message
    When I tap on the 'Show empty view with custom text' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'No activities found!'
    And I should not see an action button in empty view

  @sample-scenario-18
  Scenario: Showing an activity chooser with custom empty view message from resource
    When I tap on the 'Show empty view with custom text from resource' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'Custom empty title'
    And I should not see an action button in empty view

  @sample-scenario-19
  Scenario: Showing an activity chooser with custom empty view action
    When I tap on the 'Show empty view with custom action' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'Action couldn't be completed.'
    And I should see an action button in empty view with text 'Resolve'

  @sample-scenario-20
  Scenario: Tapping on empty view action
    Given I see an activity chooser with 'Show empty view with custom action' items
    When I tap the empty view action button
    And I should see the action result

  @sample-scenario-21
  Scenario: Showing an activity chooser with custom empty view action text
    When I tap on the 'Show empty view with custom action text' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'Action couldn't be completed.'
    And I should see an action button in empty view with text 'Click me!'

  @sample-scenario-22
  Scenario: Showing an activity chooser with custom empty view action text from resource
    When I tap on the 'Show empty view with custom action text from resource' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'Action couldn't be completed.'
    And I should see an action button in empty view with text 'Click me, please'

  @sample-scenario-23
  Scenario: Showing an activity chooser with custom empty view layout
    When I tap on the 'Show empty view with custom view' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should not see an empty view message
    And I should not see an action button in empty view
    And I should see a custom empty view layout
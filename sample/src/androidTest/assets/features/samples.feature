Feature: Samples app

  Background:
    Given I see samples list

  @sharing-apps-installed
  Scenario: Showing a System activity chooser
    When I tap on the 'Share text with the stock Activity Chooser' button
    Then I should see a system activity chooser

  @sharing-apps-installed
  Scenario: Showing a default activity chooser
    When I tap on the 'Share text' button
    Then I should see an activity chooser with text 'Share via'
    And I should see at least one activity item
    And I should not see an empty view

  Scenario: Dismissing activity chooser with BACK button
    Given I see an activity chooser with items
    When I tap the Back button
    Then activity chooser should be dismissed

  Scenario: Dismissing activity chooser with a tap on the outside
    Given I see an activity chooser with items
    When I tap the content view
    Then activity chooser should be dismissed

  Scenario: Dismissing activity chooser with swipe down
    Given I see an activity chooser with items
    When I swipe the bottom sheet down
    Then activity chooser should be dismissed

  @sharing-apps-installed
  Scenario: Showing an activity chooser with custom text
    When I tap on the 'Share text with title' button
    Then I should see an activity chooser with text 'Some custom title'
    And I should see at least one activity item
    And I should not see an empty view

  @sharing-apps-installed
  Scenario: Showing an activity chooser with custom text from resource
    When I tap on the 'Share text with title from resource' button
    Then I should see an activity chooser with text 'Custom title'
    And I should see at least one activity item
    And I should not see an empty view

  @sharing-apps-installed
  Scenario: Showing an activity chooser with custom style
    When I tap on the 'Share text with custom style without item animation' button
    Then I should see an activity chooser with text 'Share via'
    And I should see at least one activity item
    And I should not see an empty view
    And I should see a list with 2 columns

  @sharing-apps-installed
  Scenario: Tapping on an activity item
    Given I see an activity chooser with items
    When I tap on an activity item
    Then I should see the clicked activity

  @sharing-apps-installed
  Scenario: Long-tapping on an activity item
    Given I see an activity chooser with items
    When I long-tap on an activity item
    Then I should see the clicked activity's settings

  @facebook-app-installed @gmail-app-installed
  Scenario Template: Using alternative intents for selected apps
    Given I see an activity chooser with 'Share text with secondary intents' items
    When I tap on '<appName>' activity item
    Then I should see the clicked activity with package name: '<packageName>', extra title: '<extraTitle>' and extra text '<extraText>'

    Examples:
      | appName | packageName           | extraTitle              | extraText                                     |
      | Gmail   | com.google.android.gm | Secondary text to share | Secondary shared link: http://www.google.com  |
      | Facebook| com.facebook.katana   | Tertiary text to share  | Tertiary shared link: http://www.facebook.com |

  @pdf-reader-apps-installed
  Scenario: Showing a 'PDF reader' activity chooser
    When I tap on the 'Preview PDF' button
    Then I should see an activity chooser with text 'Preview PDF'
    And I should see at least one activity item
    And I should not see an empty view

  @pdf-reader-apps-installed
  Scenario: Tapping on a 'PDF reader' activity item
    Given I see an activity chooser with 'Preview PDF' items
    When I tap on an activity item
    Then I should see the clicked 'PDF reader' activity

  @sharing-apps-installed
  Scenario: Showing a 'tracking' activity chooser
    When I tap on the 'Track clicked items' button
    Then I should see an activity chooser with text 'Share via'
    And I should see at least one activity item
    And I should not see an empty view

  @sharing-apps-installed
  Scenario: Tapping on a 'tracking' activity item
    Given I see an activity chooser with 'Track clicked items' items
    When I tap on an activity item
    Then I should see a tracking message

  Scenario: Showing an activity chooser with default empty view
    When I tap on the 'Show default empty view' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'Action couldn't be completed.'
    And I should not see an action button in empty view

  Scenario: Showing an activity chooser with custom empty view message
    When I tap on the 'Show empty view with custom text' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'No activities found!'
    And I should not see an action button in empty view

  Scenario: Showing an activity chooser with custom empty view message from resource
    When I tap on the 'Show empty view with custom text from resource' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'Custom empty title'
    And I should not see an action button in empty view

  Scenario: Showing an activity chooser with custom empty view action
    When I tap on the 'Show empty view with custom action' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'Action couldn't be completed.'
    And I should see an action button in empty view with text 'Resolve'

  Scenario: Showing an activity chooser with custom empty view action text
    When I tap on the 'Show empty view with custom action text' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'Action couldn't be completed.'
    And I should see an action button in empty view with text 'Click me!'

  Scenario: Showing an activity chooser with custom empty view action text from resource
    When I tap on the 'Show empty view with custom action text from resource' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should see empty view message 'Action couldn't be completed.'
    And I should see an action button in empty view with text 'Click me, please'

  Scenario: Showing an activity chooser with custom empty view layout
    When I tap on the 'Show empty view with custom view' button
    Then I should see an activity chooser with text 'Share via'
    And I should see an empty view
    And I should not see an empty view message
    And I should not see an action button in empty view
    And I should see a custom empty view layout
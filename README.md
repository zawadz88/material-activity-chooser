# Android Material Activity Chooser [![Build Status](https://travis-ci.org/zawadz88/material-activity-chooser.svg?branch=master)](https://travis-ci.org/zawadz88/material-activity-chooser) [![codecov.io](https://codecov.io/github/zawadz88/material-activity-chooser/branch/master/graph/badge.svg)](https://codecov.io/github/zawadz88/material-activity-chooser)

This library allows to use Material activity choosers from Jelly Bean+ to e.g. share a text or a link using a nice Material dialog.
Material activity choosers are displayed as bottom sheet dialogs.
Bottom sheets are described here: https://material.google.com/components/bottom-sheets.html 
The actual implementation of activity choosers varies depending on the OS version.
This library tries to mimic the activity chooser version you can see on Android Nougat e.g. on Nexus 6P.

## Download (from JCenter)
```groovy
compile 'com.github.zawadz88:material-activity-chooser:0.2.2'
```

## Sample app
<a href='https://play.google.com/store/apps/details?id=com.github.zawadz88.sample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width='240px'/></a>

## Supported features
  - showing the activity chooser as a bottom sheet dialog on all OS versions starting from Jelly Bean (API 16)+
  - a smooth animation when the items appear in the bottom sheet
  - setting custom bottom sheet titles
  - setting different intents per activity, e.g. to have different messages for different applications when sharing
  - use custom styles for the bottom sheet
  - setting a custom message/button text for the empty view when no activities are found
  - setting a custom view when no activities are found
  - adding a button which triggers a PendingIntent when clicked when no activities are found
  - adding custom actions when an activity was clicked
  - going to the chosen activity settings on long-click
  
## Screenshots

### Showcase
<img src ="./screenshots/showcase.gif" width="360" height="640"/>

### Sharing with MaterialActivityChooser
<img src ="./screenshots/share.png" width="360" height="640"/>

### Sharing with the system IntentChooser on Nougat for comparison
<img src ="./screenshots/stock_share_on_nougat.png" width="360" height="640"/>
  
### Sharing with MaterialActivityChooser with custom style
<img src ="./screenshots/styled_share.png" width="360" height="640"/>

### Sharing with MaterialActivityChooser when no activities found
<img src ="./screenshots/empty_view.png" width="360" height="640"/>

### Sharing with MaterialActivityChooser when no activities found with a custom view
<img src ="./screenshots/empty_view_custom.png" width="360" height="640"/>
  
## Getting started

### Add Activity Chooser activity to AndroidManifest.xml

In AndroidManifest.xml add:

```xml
    <activity
        android:name="com.github.zawadz88.activitychooser.MaterialActivityChooserActivity"
        android:theme="@style/MACTheme" />
```

### Showing an activity chooser
To show an activity chooser you need to pass it the intent as follows:

```java
    new MaterialActivityChooserBuilder(context)
        .withIntent(someIntent)
        .show();
```

## Choosing a custom dialog title
You can set a custom bottom sheet dialog title with either a String or a String resource ID, e.g.

```java
    new MaterialActivityChooserBuilder(context)
        .withIntent(someIntent)
        .withTitle("Some custom title")
        .show();
```

or

```java
    new MaterialActivityChooserBuilder(context)
        .withIntent(someIntent)
        .withTitle(R.string.custom_share_title)
        .show();
```

## Using alternative intents for selected apps
It is possible to use alternative intents for selected applications instead of the main intent 
e.g. assuming you wanted to share a message, but you wanted to have a slightly different message for common Mail apps.
This can be done by e.g.

```java
    new MaterialActivityChooserBuilder(context)
        .withIntent(shareIntent)
        .withSecondaryIntent(alternativeShareIntent,
            "com.google.android.gm" /* GMail */,
            "com.google.android.apps.inbox" /* Inbox */,
            "com.microsoft.office.outlook" /* Microsoft Outlook */,
            "com.google.android.email" /* Default mail app */)
        .show();
```

## Choosing a custom empty view message
You can set a custom bottom sheet dialog empty view title with either a String or a String resource ID, e.g.

```java
    new MaterialActivityChooserBuilder(context)
        .withIntent(someIntent)
        .withEmptyViewTitle("Some custom empty view title")
        .show();
```

or

```java
    new MaterialActivityChooserBuilder(context)
        .withIntent(someIntent)
        .withEmptyViewTitle(R.string.custom_empty_view_title)
        .show();
```

## Showing a button on empty view
You can show a button on the empty view which will trigger a PendingIntent once clicked. To do so you need to provide the PendingIntent with/without a button title, e.g.

```java
    new MaterialActivityChooserBuilder(context)
        .withIntent(someIntent)
        .withEmptyViewAction(pendingIntent)
        .show();
```

or

```java
    new MaterialActivityChooserBuilder(context)
        .withIntent(someIntent)
        .withEmptyViewAction("Custom button title", pendingIntent)
        .show();
```

or

```java
    new MaterialActivityChooserBuilder(context)
        .withIntent(someIntent)
        .withEmptyViewAction(R.string.custom_button_title, pendingIntent)
        .show();
```

## Setting a custom empty view
You can inflate an entirely custom empty view as well, e.g.

```java
    new MaterialActivityChooserBuilder(context)
        .withIntent(someIntent)
        .withEmptyViewCustomView(R.layout.layout_custom_empty_view)
        .show();

```

## Styling the dialog to your own needs
You can override the default styling of the Activity Chooser to your own needs.
The easiest way is to override the ```MACTheme``` and replace the styles in the custom attributes. For more info see the sample app.

## Disabling the item animation
By default there is a layout animation on the RecyclerView which animates the items as they show up.
It can be however disabled by extending the ```MACTheme``` and overriding ```mac_bottomSheetRecyclerViewStyle``` with a style with this animation disabled, e.g.
```java
    <style name="NoAnimationRecyclerViewStyle" parent="MACBottomSheetRecyclerView">
        <item name="android:layoutAnimation">@null</item>
    </style>
```

## Tracking when an activity was clicked
You might want to track/log when an activity has been selected to handle your intent. To do so you can extend the default MaterialActivityChooserActivity.

```java
public class TrackingActivityChooserActivity extends MaterialActivityChooserActivity {

    ...

    @Override
    public void onActivityClicked(ResolveInfo activity) {
        Toast.makeText(this, "Application clicked: " + activity.activityInfo.packageName, Toast.LENGTH_SHORT).show();
        super.onActivityClicked(activity);
    }
}
```

which needs to be declared in the AndroidManifest.xml

```xml
    <activity
        android:name=".TrackingActivityChooserActivity"
        android:theme="@style/MACTheme" />
```

and also declared when building the activity chooser:

```java
    new MaterialActivityChooserBuilder(this)
        .withIntent(getDefaultShareIntent())
        .withActivity(TrackingActivityChooserActivity.class)
        .show();
```

## Testing
The library is tested mainly with Espresso + Cucumber as well as a few unit tests. 

### Instrumentation tests
To run instrumentation tests you need to execute:

```bash
./gradlew connectedDebugAndroidTest 
```

This additionally fetches the Cucumber reports from the device and saves them to ```sample/build/reports/cucumber```.

It is also recommended to install _Cucumber for Java_ and _Gherkin_ plugins in Android Studio for better Cucumber integration inside of the IDE.

### Unit tests
To run unit tests you need to execute:

```bash
./gradlew testDebugUnitTest 
```

### Code coverage
Tests can be also executed with code coverage. To do so execute:

```bash
./gradlew jacocoTestReport 
```

The coverage report can be then found at ```sample/build/reports/jacoco```. This task cleans the project, runs the unit tests & instrumentation tests and at the end it creates the report.

### Running selected scenarios only for instrumentation tests
All the tested scenarios have Cucumber tags. You can run a specific test by using this tag. 
Assuming you wanted to run only the scenario with tag _@sample-scenario-22_ you would do the following:

* From command line:

```bash
./gradlew connectedDebugAndroidTest -Ptags="@sample-scenario-22"
```

* In Android Studio you need to edit the current Android Tests run configuration and under _Extra options_ enter:

```
-e tags @sample-scenario-22
```

## Notes
This library comes with a number of dependencies.<br/>
It relies on the BottomSheetBehavior from Android Support Design Library and it uses a RecyclerView for displaying a list of items.

## License
Copyright 2016 Piotr Zawadzki
    
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    
&nbsp;&nbsp;&nbsp;&nbsp;[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)
    
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

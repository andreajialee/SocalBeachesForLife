# SocalBeachesForLife

Create a mobile application "SoCal Beaches for Life" for CSCI-310

**Important Information**
We ran our code with a Pixel 2 API 33 Emulator, instead of the normal Nougat with API 24. Our emulator would not load google maps with the old device, so we had to change the emulator.

We fetched the user's current location, but for some reason it works sometimes & doesn't work other times. We set a default location (USC) for the app to still launch even if location permissions and other permissions aren't accessible.
Many of the APIs were deprecated, so our code isn't perfect. 


**Preface**

We will be creating an Android App called SoCalBeach4Life which will heavily rely on Google Maps API to display the closest beaches to the user from 	their current location. 

**Introduction**

Many people new to the Southern California area have the desire to go to the beach yet many of them do not know what the closest beach to them is or how to get there. In our app, SoCalBeaches4Life, the user will be able to login, get directions to beaches, leave reviews, and more. 

**User Requirements**

_Title: Register, Login, Logout_

Description: Users will be able to register an account. After registering, the user will be able to login to their profile. From here, they can manage different aspects and choose what to do. The user can also login and logout. 

_Title: Route to Beaches_

Description: Users will be able to display a route from their current location to a beach of their choosing through Google Maps. The user will be able to select from at least two parking lots closest to the beach of their choosing. The user will be able to see the ETA of the given route.

_Title: View Nearby Restaurants_

Description: Users will be able to see a radius centered on the beach measuring from 1000, 2000, or 3000 feet that includes nearby restaurants with markers. When a marker is clicked the user will be able to see the rating and opening hour information for that location.

_Title: View Beach Review_

Description: Users will be able to view other people’s reviews of beaches and see the beach’s rating out of 5 stars.

_Title: Write Beach Review_

Description: Users will be able to write a review of the beach by leaving a rating out of 5 stars, and may additionally add comments, suggestions, pictures with descriptions, and etc. anonymously or with their name. The user will be able to post and delete their reviews.

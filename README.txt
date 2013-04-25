Abraham Eliares Dela Cruz
Sayasinh "Joey" Phommasone
Zack Warburg

Winter 2013 Ben Zhou
CS176B Homework 3: Facebok/Twitter Aggregator

Application name: FTAgg
App Status: Complete

Basic Functionality:

MainActivity: You will be brought to a main page with two buttons on the top corners
one for Facebook and the other for Twitter. By clicking either button,
you will be brought to the main page for the respective interface.

Facebook: After clicking the "Facebook" button, it will pop up 3 buttons, an unimplemented "Facebook
button, a "Twitter" button which brings you to the Twitter Activity, and a "Login to Facebook"
button. Clicking the "Login to Facebook" button initiates the login process which requires user
information, as well as asking for user Permissions.

*If the user is NOT logged in, no feed will be displayed and the user will be required to login
to Facebook.
*If the user is already logged in, a feed will automatically render with the user's information
TO RELOAD THE PAGE: simply switch to the Twitter activity and switch back to Facebook, this will
for the activity to reload the most recent posts on Facebook.

Twitter: After clicking the "Twitter" button, it will also pop up 3 buttons, a "Reload Feed" button,
a "Facebook" button which brings you to the Facebook activity, and a "Login to Twitter" activity.
Regardless if the user is logged in, the feed can only be announced if the "Reload Feed" button
is pressed. Similiar to Facebook, pressing login will initial the login process, asking for the
user's information and permissions.
*If the user is NOT LOGGED IN, pressing "reload" will prompt the user to login to Twitter.
*if the user is LOGGED IN, "reload" will then render the user's Twitter feed.

Possible Issues and Problems : None.
As far as we've tested, we were able to accomplish everything that was required of the assignment.
Both login/logout functionality were achieve for BOTH social media networks and the App is registered
with both Facebook and Twitter to communicate with their systems. We were also able to implement reloading
of the feeds, although Facebook is implemented to automatically render at the onCreate of the activities
where the Twitter side is done manually. We are able to pull all required information into the fields
view ListViews, TextViews, and ImageViews. Text and images are displayed on the feed with their respective
users.

**Rendering of the images in the ListView is dependant on the internet connection, so if the app is used on a
network with weak signal, then the app will respond by pausing to load the images.


We have not done rigorous tests to see what will force our app to crash, but we are able to achieve all that
was said to be required without error.

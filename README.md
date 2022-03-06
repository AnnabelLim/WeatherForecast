# WeatherForecast

USING THIS APP

This app retrieves a 7 Day weather forecast for a user location.

To use this app, you will need internet access.
You also need to grant the app location permission.

After downloading this app from Github,
connect your device to your computer via USB and run the code in Android Studio.

LIMITATIONS

These are the application shortcuts implemented to simplify the design:

1.	We use one denormalized table to hold the data.
For example, the city and country are repeated with every forecast.
To design this right, the location data (city and country) should probably be stored in a separate table.

2.	We put all the database information in one file (@Entity, @Dao and @Database)

3.	Not all data from the Weather Forecast API is stored in the database.  We only capture enough data to display in a detail view.

4.	The app is limited to portrait mode. 

5.	There is no repository layer, the view model calls DAO directly.

6.	Data flows only one way: server to database, database to user interface.

7.	Data from server is retrieved only once, when the user opens the app.   In reality, weather data should be retrieved periodically to keep it current.

8.	When you open the app the very first time (so there is no data in the database yet) and you have no internet connection or you do not grant location permission, you will see a very blank screen, something that should not happen in a production app.


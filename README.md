# Week5WeekendGooglePlacesAPI
Google Places API App using MVP and Dagger

This version of the google places api project uses the MVP design pattern and Dagger dependency injection.

One issue is that the places api allow limited amount of query per day so i think at the moment only one query can be sent using 
my places api key and after dat it will keep saying Over qery limit


App uses google places web service API to get all the places new your location and display them in a recycler view based on category. 
you can choose the place type using a menu then the result will be shown on a map with markers and also in the recycler view you can 
swipe the recycler view item either left or right to open the place in google map for directions select an item and it will 
take you to detail page for the item

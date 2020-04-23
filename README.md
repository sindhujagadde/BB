# BB
**Showing markers on map from JSON file**
---------------
Libraries Used:
---------------
1)  implementation 'com.intuit.sdp:sdp-android:1.0.6'
An android SDK that provides a new size unit - sdp (scalable dp). This size unit scales with the screen size. This librabry has been used in order to support all the screen devices with different resolution.

2) implementation 'com.l4digital.fastscroll:fastscroll:2.0.1'
As city.json file contains large amount of data(around 200k entries), I'm not able to see all the data with fast scroll using simple recycler view which android provides, so for faster scroll I have used this library.

3)implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
This library worked like a converter which uses GSON for serialization to and from JSON.

4) implementation'com.google.android.gms:play-services-maps:17.0.0'
This librabry has been used to display map.

---------------------
How does search works
---------------------
I have used Filterable interface for filtering data of recyclerview.

----------
Filtering
----------
This method will perform filtering arraylist. Whenever I type something this method has been called and in this method we have to traverse through our arraylist against what ever text is written in search area.In this method we have taken one string which contains the text which we have to filter from arraylist.

-First we have checked that if search area is blank then it'll return whole list.
-If we got any thing in search area, we have stored it in charString. Now we need to traverse the list for filter, in this I have make condition that if my arraylist object value starts with what we have typed in edit text OR it contains what we have typed in edit text, I have added it in filteredList, performFiltering method and will return filtered list.

-In publishResults method we will get filtered arraylist, now we just have to give that filtered list to main arraylist and need to notify adapter using notifyDataSetChanged() method, by this method adapter will be notified and you will able to see the filtered results.

-------------
Problem Faced
-------------
-Due to huge amount of data in cities.json file, loading of the list has been a big task for me. I tried my best to load it with in minimal time.
-Tried sorting and searching the list in alphabetical order but the application was way too slow(May be due to the huge json file).



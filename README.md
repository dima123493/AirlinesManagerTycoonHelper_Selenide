# Airlines Manager Tycoon Helper

The inspiration for this project was the idea of automation
of daily routine actions in the game from [Playrion Gamestudio](https://www.playrion.com) 
called [Airlines Manager – Tycoon](https://www.playrion.com/airlines-manager/).

_Airlines Manager is a free aviation management game._

_In the game, you manage your airline like a real tycoon and strategize to become the greatest CEO in the aviation world!_

This project implements idea of simplifying day to day tasks like managing route prices,
maintain your planes and so on. 

##### Frameworks used
* [Apache POI](https://poi.apache.org)
* [Selenide](https://selenide.org)
* [Lombok](https://projectlombok.org)


Your journey as a user starts from [airlinesManagerConfiguration.properties](src/main/resources/airlinesManagerConfiguration.properties)
file where you need to settle all required info of your company.
First of all you need to provide your login credentials so the program can manipulate your data.
You also should provide other details, so be sure to check instructions there.

### Take off

After basic configuration you can proceed to class called [AllRoutesDetailsGathering](src/main/java/AllRoutesDetailsGathering.java).
The idea of this class is to gather basic route details of specified hub from website.
Such info as airport destination name, economy, business, first classes and cargo demand as well as their ideal prices.

This information is needed for further steps.

_**Option for new or missed routes added.**_

Oftentimes after running this class not all routes info was gathered, also 
after you buy new routes you had to track them and input manually into file or rerun class allover again.

This was irritating and force you proceed with unnecessary info gathering which could take a long time.

Such inconvenience was resolved by adding new [ManageNewRoutePrices](src/main/java/AllRoutesDetailsGathering.java) class.
Now you can just run it and collect data which were missing. Such a relief, isn't it? :)

### Satisfied customers and inspired CEO

After all required info is gathered you can start to dive into the best strategy for your company.
Start [ReadFromExelAndUseDataOnPerfectSeatFinderWebsite](src/main/java/ReadFromExelAndUseDataOnPerfectSeatFinderWebsite.java) class.

There is a website that finds the best configuration of planes amount and seats in it to maximize your profit from the route.

The idea of this class is using data from previous steps to insert values on this site. Website process your data and finds the best configuration 
of daily route waves, sets and prices.

There you need to specify name of planes that you want to use for the routes and their max distance. Add your logic in accordance with your preferences and fleet.

Please, proceed to [finderWebsiteConfiguration.properties](src/main/resources/finderWebsiteConfiguration.properties) file to configure your settings there.

### Time to put it into practice

Having all the data collected it is time to provide reasonable prices for our customers.
Now you can proceed to [ManageAllRoutePrices](src/main/java/AllRoutesDetailsGathering.java) class.

The idea of this class is to read data from file and implement ideal price for each route.

_Be aware that it applies prices only, and you need to manage your waves separately :(_ 

### Scheduling

We all want to use our planes on FULL (100%) capacity. That is why [RouteTimeTableOptimizer](src/main/java/AllRoutesDetailsGathering.java) class was created.
The idea of this class is to group routes by seat configuration and perform as many waves per day as possible where this configuration is used.

BEWARE: If route takes more than 24 hours - this class not gonna works since it will each time try to create a new plane. 

_Explanation:_

Let's imagine you have 2 routes with same seat configuration (ZAD, KIV).
Flight to ZAD takes 8:45h and needs 5 waves to satisfy demand and to KIV - 2:45h and 4 waves.
The scheduling will perform 2 waves for ZAD (takes 17:30 h) and 2 waves for KIV (takes 5:30 h).
So that 17:30 + 5:30 = 23:00h. That means plane have free 1h. Since 
not all demand was covered new plane will be created and rest of waves will be performed.
If any wave with same configuration as in ZAD and KIV will be found and which will take 1 hour of less 
that will be added to previous plane with spare time left. So if route to LWO takes 
0:45 h and seat configuration is same as in plane with ZAD and KIV, LWO will be added in that plane and 
spare time of such plane will be 24:00 - (23:00 + 00:45) = 15 minutes.


### Last but not least

We all know that working planes are the key of each company. To keep your planes operable we need to maintain it regularly.

Here is what [PlanesManagement](src/main/java/RouteDetailsGathering.java) class created for.

The idea of this class is to collect links to all of your planes, open it and decide which type of maintenance 
is suitable for exact plane.

### Conclusion

This project is used for personal use and do not encroach to be
the perfect one. So if you up to it - you can contribute ♥

More yet to come!

Thank you for your attention and have fun!



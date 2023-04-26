# Airlines Manager Tycoon Helper

The inspiration for this project was the idea of automation
of daily routine actions in the game from [Playrion Gamestudio](https://www.playrion.com) 
called [Airlines Manager – Tycoon](https://www.playrion.com/airlines-manager/).

_Airlines Manager is a free aviation management game._

_In the game, you manage your airline like a real tycoon and strategize to become the greatest CEO in the aviation world!_

This project implements idea of simplifying day to day tasks like managing route prices,
maintain your panes and so on. 

##### Frameworks used
* [Apache POI](https://poi.apache.org)
* [Selenide](https://selenide.org)


Your journey as a user starts from [airlinesManagerConfiguration.properties](src/main/resources/airlinesManagerConfiguration.properties)
file where you need to settle all required info of your company.
First of all you need to provide your login credentials so the program can manipulate your data.
You also should provide other details, so be sure to check instructions there.

### Take off

After basic configuration you can proceed to class called [RouteDetailsGathering](src/main/java/RouteDetailsGathering.java).
The idea of this class is to gather basic route details of specified hub from website.
Such info as airport destination name, economy, business, first classes and cargo demand as well as their ideal prices.

This information is needed for further steps.

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
Now you can proceed to [ManageRoutePrices](src/main/java/RouteDetailsGathering.java) class.

The idea of this class is to read data from file and implement ideal price for each route.

_Be aware that it applies prices only, and you need to manage your waves separately :(_ 

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



## ALEA Challenge
Simple REST api that interacts with [pokeapiV2](https://pokeapi.co/docs/v2), it provides 3 functions to retrieve a list of the 5 pokemons with highest height, weight or base_experience
This service runs in ```localhost:8080```, the different endpoints that provides are (accepting method GET):
- ```localhost:8080/client/highest```
- ```localhost:8080/client/heaviest```
- ```localhost:8080/client/moreExperienced```

### How to build and run
There is a Makefile that will help to build, test and run the project, also the required commands can be run manually. These are the options, they have to be executed in the root of the project:
- ```make build```: build the project, equivalent to ```./gradlew build```
- ```make test```: tests the project, equivalent to ```./gradlew test && ./gradlew jacocoTestReport```
- ```make run```: starts the application, it has to have been built, equivalent to ```java -jar build/libs/challenge-0.0.1-SNAPSHOT.jar```
- ```make clean```: cleanup, equivalent to ```./gradlew clean```

### Implementation
It has a ClientService in charge of retrieving and serving the lists. 

#### Cache
The first time a list is requested it has to use the API (Pokeapi) to fetch each pokemon, this is a really expensive and slow operation, in order to avoid unnecessary operations a cache is set to store the sets of pokemons with highest stats. Independently of the set asked in the operation the 3 lists are created and cached.  
This cache is not configured with an expiration mechanism, is not needed since the list of pokemons wont change much. It can be optimized by resetting the cache from time to time (once a week would be more than enough) but for the purpose of this project is not needed since restarting the application also restarts the cache.

#### Algorithm
To calculate the set of 5 pokemons with higest stats ```TreeSet``` classes are used along with custom comparators for the Pokemon class. This allows to sort the pokemons with a given cathegory easilly and effitiently.

### Tests
Unit tests have been implemented with almost 100% coverage. Happy paths are completely covered, unhappy paths are not but in this case is not that important since any failure is handled the same way thanks to the try catch block in the main service methods.
Test and coverage reports will be available in ```build/reports```
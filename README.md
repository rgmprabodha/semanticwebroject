# semanticwebroject

## Two web projects
  * extractdata project
    1. Extract Json data
    2. Generate the model 
    3. Save in Fueski server
    
  * bicycleSharingStations project
    1. Web project contains Web site and REST API to query the data
      
## How to Run the projects

Two projects are supposed to compile and run. One project is to create a triple store and second one is for display the data. To create the Data store,
1. Run Fueski server
2. Add new data store, name: bicycle_stations
3. Open the project extractData and open the package, complete (extractdata\src\main\java\complete)
4. Run ‘CreateModel.java’ program to extract static data of SAINT-ETIENNE, LYON, TOULOUSE and NANTES cities, Wait until the process completes, it gives a success message on console.
5. Run DynamicSaintEtienne.java, DynamicLyon.java, DynamicToulouse.java and DynamicNantes.java programs to extract dynamic data. Wait until the process completes. Each step will give success message on console.

Now you have extracted necessary data and saved RDF triplet and Fueski triple store. If you see the http://localhost:3030/bicycle_stations, you can see the data. Web project should be working properly now. Web project is developed using Spring boot and Maven. First enter command mvn clean install, then run the project with mvn spring-boot:run command. Now browse the http://localhost:8080 to see the web site. Select a city in dropdown list, you can see the data. 

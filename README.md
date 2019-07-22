# ao1screen
a screening  project for ao1 solution

How can You sort csv data using this project?

0. be sure You have Java 8 sdk installed on your system (it would be better to have a Gradle as well =))
1. if You have Gradle installed run Gardle's jar task in the project 
2. a result of the succesfully completed jar task is price.selector-1.0-SNAPSHOT.jar 
3. move that file to your data storage 
4. run: java -jar task is price.selector-1.0-SNAPSHOT.jar -> You will  get a list of arguments to be set
5. according to the previous punkt You can run for example:    
   java -jar task is price.selector-1.0-SNAPSHOT.jar  -d <source directory 1> -d <source directory 2> -r report.csv -s 3
   the command above will make: 
      0. run 2 reading threads to get data from <source directory 1> and <source directory 2> separately
      1. run 1 thread to serialize these data into Items and divide them to 3 groups
      2. run 3 sorting threads to sort that data separately (there will be no more then 1000 Items sorted in any of these thread)
      4. merge these 3 pre sorted results in one list when all the csv files are read
      5. write down this list into report.csv

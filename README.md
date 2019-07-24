# ao1screen
a screening  project for ao1 solution

an idea is to have 3 separate phases:
   * read csv files from a directory. only one thread reads data from the directory. if you have data on N devices it is possible to read from that N devices using exactly N threads. 
   * serialize csv strings into Items and split them on groups according to their productId. there could be as many threads as you want.
   * sort the groups in separate threads. every group should be confined to some thread. every thread have no more than 1000 Items sorted. 
   * merge results

How can You sort csv data using this project?

0. be sure You have Java 8 sdk installed on your system (it would be better to have a Gradle as well =))
1. if You have Gradle installed run Gardle's jar task in the project 
2. a result of the succesfully completed jar task is price.selector-1.0-SNAPSHOT.jar 
3. move that file to your data storage 
4. run: java -jar task is price.selector-1.0-SNAPSHOT.jar -> You will  get a list of arguments to be set
5. according to the previous punkt You can run for example:    
   java -jar task is price.selector-1.0-SNAPSHOT.jar  -d <source directory 1> -d <source directory 2> -r report.csv -s 3
   the command above will make: 
      
      * run 2 reading threads to get data from <source directory 1> and <source directory 2> separately (they read csv files only)
      * run 1 thread to serialize these data into Items and divide them to 3 groups
      * run 3 sorting threads to sort that data separately (there will be no more then 1000 Items sorted in any of these thread)
      * merge these 3 pre sorted results in one list when all the csv files are read
      * write down this list into report.csv

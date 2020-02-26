Devo Coding Exercise - TfIdf
==================================

### Big O time and Space Complexity
+ Time complexity by document is: O(n) where n is the sum of the number of terms to analyze and m the number of words in the document 
(algorithm is running in a parallel stream some performance improvements may happen)
+ Space complexity by document is: O(n) where n is the sum of the number of terms and the number of words in the document

### Requirements
+ java 8
+ maven 3 
### How to build the app
+ Go to app root directory and execute:
```
mvn clean package
```
+ Move to target directory and execute:
```
java -jar tf-1.0-SNAPSHOT.jar -d <directory> -t <"terms"> -n <max number of results> -p <time period in seconds>
``` 
+ Enjoy!
<h1>2- Exploring Your Favorite Programming Language</h1>

<h2>Directory:</h2>

- spread.pdf: the specification for the spreadsheet module, task 1

- 2: the executable for task 2

- aux: contains auxiliary files used to generate the executable.



<h2>How to Run the Executable:</h2>
  From the command line, run ./2 and enter valid JSON values. The program will quit if invalid JSON is entered. When finished inputting JSON, to end the Standard in, press ^D. 


<h2>Within the src folder in the aux:</h2>

- Main.java: Uses Jackson JSON package to parse JSON. Main.main takes in the user input and stores it in a String to be parsed by the function, parseJSON. parseJSON takes in the un-parsed, input JSON String and an empty ArrayList<String>, a list of the String JSON representations of each valid JSON value found in the input. Using Jackson JSON ObjectMapper and JSON Parser, the input is turned into JSON Node objects and their JSON String representations are added to the ArrayList. The ArrayList is then printed back to the console in the order it was inputed, indexed in descending order, using the printValues function. If invalid JSON is input, an error will be thrown and the application will quit.
- jackson-annotations-2.9.6.jar: included for the use of the other two .jar files that follow
- jackson-core-2.9.6.jar: included for use of JSON Factory and JSON Parser
- jackson-databind-2.9.6.jar: included for use of JSON Object Mapper

<h2>How to Run Tests</h2> 
The full path to the test file is 2/aux/test/MainTest.java.
The test file contains multiple JUnit tests within the MainTest class. We've tested for the following cases:

Strings

- Single String JSON Value
- Empty String
- Single String across multiple lines
- Multiple String on one line

Numbers

- Single Number JSON Value
- Multiple Numbers on multiple lines

Arrays

- Single Array JSON Value on one line
- Single Array across multiple lines
- Multiple Arrays on one line

Objects

- Single Object JSON Value on one line
- Object across multiple lines
- Multiple Objects on one line

Mixed Values with Single and Multiple Lines

- Multiple Values on one line
- Multi-line input
- Multi-line input
- Multi-line input with and Array spanning multiple lines
- Different types next to each other, i.e String against an Array on one line

# Team Members:

1. Harsh Singh
2. Anush Yadav
3. Akanksha Ramesh
4. Aishwarya Venkatesh

# Jar Files Location
https://github.com/harshs08/tika-parser/tree/harsh/res



# Programming Language used:
Java for tasks 1, 2, 3, 5
Python for task 4

# Operating System used:
Mac OS X/ Linux 


# Java Compiling Instructions:

1. Unzip the project
2. Move to the project folder.
	 Eg (In Linux Environment): cd /path/to/folder/tika-parser
3. Add the jar files fastutil-6.3.jar, guava-13.0-rc1.jar, tika-app-1.6.jar in the /tika-parser/res directory
4. Compile using the command:
	
		javac -cp $CLASSPATH$ src/main/java/com/parse/deduplication/*.java src/main/java/com/parse/tika/*.java

	
	Eg:
	
		javac -cp "./res/*" src/main/java/com/parse/deduplication/*.java src/main/java/com/parse/tika/*.java


# Java Execution Instructions:

1. Run the java file with following command.

		java -cp $CLASSPATH$ com.parse.tika.App [input folder] [deduplication switch]

	*	[input folder] - String: Path to the folder containing all the TSV files
	*	[deduplication switch] - int: Selector to chose whether to execute the crawler with or without Deduplication.
	0 => Without, 1 => With 2=> Near Duplicates(partially implemented).

	Eg.
		
		java -cp "./src/main/java/:./res/*" com.parse.tika.App ./src/main/resources/input 1


# Python Compiling Instructions:

Download and installation instruction:

1.	Install and download ETLLib per the instructions, here:   https://github.com/chrismattmann/etllib/blob/master/README.md
2.	Place the customtsvtojson.py in the following folder path PROJECT_PATH /etllib/etl


Python Execution Instructions:

1.	Go to PROJECT_PATH /etllib/etl/
[ cd PROJECT_PATH /etllib/etl/ ]
2.	Type the following command
		
		python customtsvtojson.py -d <directory of tsv files> -p <text file of list of headers > -f true -o job

	a.	Custom command i/p added
	
		-d : path of the directory of tsv files to be parsed
		-p : path of the text file which contains the list of headers for each row of tsv data  
		-f  :flag to generate json data[ pass “-f true” to generate json files with total json and unique json count ; do not pass “-f ” to not to generate json
3.	O/P expected
('total unique files till now = ', 276269, ' total file count = ', 12483611)

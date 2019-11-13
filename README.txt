By: Anas Aleid
Homework 2

Running my program on aws: https://youtu.be/NNzVOKhez0g
Notes:
1- YOU NEED JAVA 1.8. If you don't have java 1.8 on your VM, install it before running this.
2- As of now, this mapreduce will only parse <inproceedings> or <article> elements but not both. I could get it to do both,
but I've spent an extreme amount of time on this assignment and I need to start working on assignments for other classes
As a work around, you can change what elements are being parsed from the config file. If you want to parse articles instead,
comment out the startTag and endTag that are set to inproceedings and uncomment the ones that are set to article.
3- My mapreduce job takes at least 10 minutes to finish on my machine.
4- I used a class named "XmlInputFormat" that helps with splitting an XML file such that it remains well formed. The work done in this class
is not my own. All credit for this class goes to the original creator.


Steps to run program:
1- Clone the repo
2- Import the repo as an existing project with intellij. Once you get to the modules part of importing the project, unselect the "main" folder it
displays as a module. This will create "symbol is not defined" errors later on if you add this folder as a module. IntelliJ thinks it is a
module but it's not.
3- run the command "sbt assembly" in Intellij's terminal. (this will create a jar in the target\scala-2.12\ directory)
4- upload the jar it creates to your VM.
5- Create an input folder and place the dblp.xml file inside within your VM
6- run the hadoop jar command on the jar you uploaded to the VM. An example of the command I was running looks like this
"hadoop jar anas_aleid_hw2-assembly-0.1.jar MapAndReduce /tmp/input /tmp/output". MapAndReduce is the class that contains main.
The input directory contains the dblp.xml file. The output directory should not exist, but if it does it will automatically be deleted.
7- It took at least 10 minutes for my mapreduce job to finish. So just wait for it.
8- Once finished, there should be a file called nodeFile.csv in the input directory you specified. Put that file into gephi and it should
produce a graph.

How to use the nodeFile.csv with Gephi:
1- Open gephi and create a new project.
2- Select File -> import spreadsheet -> open nodeFile.csv
3- Make sure "Separator:" is set to "Comma" and "Import as:" is set to "Edges table" then click next.
4- Confirm the import settings are correct.
5- If an import report pops up, click "More options" and set the "Edges merge strategy" to "Average" so that it doesn't count duplicate relations.
Then click ok.
6- In the "overview" tab, there should not be a graph representing each professor and his connections to any other coauthor for the subset
of XML elements we parsed.
7- If you want the nodes to be weighted based on their connections, you can go to the ranking tab on the left side of gephi and set the attribute to
Degree. Set a color range that will differentiate the number of connections for each node then hit apply on the bottom right of that panel. 
This doesn't change the size of the node, but I feel like the representations should be equivalent


example inproceedings graph output picture : https://i.gyazo.com/37674e44ce62b8409d405c57f932a7a0.png
example article graph output picture : https://i.gyazo.com/383eac3ba979fc044b3fd39712685b95.png

Example nodeFile.csv:

Source, Target, Weight, Type
Barbara Di Eugenio,Bing Liu,	1, Undirected
Barbara Di Eugenio,Luc Renambot,	1, Undirected
Barbara Di Eugenio,Ouri Wolfson,	1, Undirected
Bhaskar DasGupta,Ouri Wolfson,	4, Undirected
Bing Liu,Barbara Di Eugenio,	1, Undirected
Bing Liu,Philip S. Yu,	2, Undirected
Chris Kanich,Jason Polakis,	1, Undirected
Chris Kanich,Philip S. Yu,	2, Undirected
Jason Polakis,Chris Kanich,	1, Undirected
Luc Renambot,Barbara Di Eugenio,	1, Undirected
Mark Grechanik,Ugo Buy,	3, Undirected
Ouri Wolfson,Barbara Di Eugenio,	1, Undirected
Ouri Wolfson,Bhaskar DasGupta,	4, Undirected
Ouri Wolfson,Philip S. Yu,	4, Undirected
Philip S. Yu,Bing Liu,	2, Undirected
Philip S. Yu,Chris Kanich,	2, Undirected
Philip S. Yu,Ouri Wolfson,	4, Undirected
Ugo Buy,Mark Grechanik,	3, Undirected


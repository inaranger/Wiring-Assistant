# ACM-Contest Problem: Wiring-Assistant

## Description
This is a Solution for a Programming Problem of the ACM-Contest written in Scala. \
Please review Wiring-Assistant.pdf to understand the Problem-Description

## How to use
**Option 1: execute jar file**
1. Download [java](https://www.java.com/en/download/) Version 8 or up
2. run `java -jar wiring-assitant.jar` in the root Folder to start project
3. Enter the Input over multiple Lines as explained in the Problem Description
4. Enter `0 0` in a separate Line to calculate the Solution
5. you will be given an Output, consisting of a Problem Description, Animation of the found Path and\
the Number of Minimal crossings for each instance

**Option 2: Build Project**
1. Download [sbt](https://www.scala-sbt.org/) Version 1.10.1 or up
2. Call `sbt run` in the root Folder to build project
3. Follow from Step 3 onwards of Option 1

## Example Input
The following input can be used as an example
```
5 1000000000 
500 2344 90002 2344 7023 0 7023 999999999 0 8000 999999999 8000 56 765 999 765 333333 6789 333333 7983
0 55 989989989 989989989
8 10
0 0 1 0 4 0 4 6 0 5 5 5 8 2 8 6 6 4 6 7 7 6 7 8 2 6 5 6 7 8 9 8
6 8 5 1
6 3
0 0 0 2 2 0 2 2 0 0 2 0 0 2 2 2 0 1 1 1 1 1 2 1
1 0 1 2
0 0
```
this should produce 3 Outputs\ 
The Number of Minimal Crossings should be 2, 1 and 4 respectively
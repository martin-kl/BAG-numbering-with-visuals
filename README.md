# BAG-numbering-with-visuals

Numbering algorithm with visual output for BAG (Bezirksarbeitsgemeinschaft) Hollabrunn.

---
## Description

This program is used by the BAG Hollabrunn to generate random starting numbers for the march music competition while respecting the given dependencies.
The data about the participating orchestras (DE: Kapellen) as well as the dependencies and restrictions are also managed by the program.

by Martin & Maximilian

--- 

## Running the program

Two ways to run the program:  
- either start the main method in the ``Main.java`` file  
- or use the included jar file (current version from commit cfa8753afc5a1b128218c5cef0b518194c8a2500): navigate to the folder containing the file and start it via
```
java -jar bag_numbering.jar
```

### Locations of the stored data:
- When starting the program from the main method, the data is loaded from [kapellen.json](kapellen.json) and [dependencies.json](dependencies.json).
- When using the jar file, the data is loaded from the directory of the jar file, i.e. [kapellen.json](bag_numbering_jar_v1.0/kapellen.json) and the dependencies from  [dependencies.json](bag_numbering_jar_v1.0/dependencies.json)

Note: while these files can be manipulated manually, it is advised to do this directly inside the program to ensure compatibility between the two files.
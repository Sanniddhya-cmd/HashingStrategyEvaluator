HashingStrategyEvaluator
==========================

A Java-based hashing algorithm evaluator that systematically tests 14 distinct configurations using various combinations of:

- Hash functions: Division-based and student-defined
- Bucket sizes: 1 and 3
- Collision resolution strategies: Linear probing, Quadratic probing, and Chaining

This tool processes an input file of integer keys, inserts them into hash tables under different schemes, and evaluates performance metrics including collisions, comparisons, runtime, and load factor. All results are written to a single output file for easy comparison.

--------------------------------------------------

Project Structure
-----------------
.
├── HashLabMain.java                  # Main class to run all hashing experiments
├── HashTable.java                    # Core hashing logic and evaluation
├── input_108.txt                     # Sample input file (integer keys)
├── AllHashingResults_input_108.txt  # Sample output file with performance metrics
├── .gitignore                        # Ignores compiled files and IDE configs
└── README.txt                        # Project description and usage instructions

--------------------------------------------------

How to Compile and Run
-----------------------
1. Compile the Java files:
   javac HashLabMain.java HashTable.java

2. Run the program:
   java HashLabMain

3. When prompted, provide:
   - The full path to the input file (e.g., input_108.txt)
   - The desired name for the output file (e.g., AllHashingResults.txt)

--------------------------------------------------

Sample Output
-------------
Each scheme outputs:
- Hashing method and strategy
- Number of collisions
- Total comparisons
- Load factor
- Final hash table contents

--------------------------------------------------

Customization
--------------
You can modify:
- mod values: Adjust prime numbers used in division hashing
- bucketSize: Toggle between 1 and 3
- Strategies: Add or remove linear, quadratic, chaining modes
- Hash method: Extend or refine the "student" hash function logic

--------------------------------------------------

Use Cases
----------
This tool is ideal for:
- Understanding trade-offs in hash table implementations
- Comparing load handling and collision behavior
- Educational demonstrations in data structures or algorithm classes

--------------------------------------------------

License
--------
This project is open source. You are free to use, modify, and distribute it for non-commercial purposes. Consider adding an MIT or Apache 2.0 license for broader use.

--------------------------------------------------

Author
-------
Sanniddhya Bardhan
MS Bioinformatics
Johns Hopkins University

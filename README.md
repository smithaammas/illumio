Author: Smitha Ammassamveettil

Flow Log Analyzer
=================

This program analyzes given flow log file and generate a report containing two parts as given below

-- Tag Counts: This section counts the number of occurrences of each tag found in the flow log.
-- Port/Protocol Combination Counts: This section counts the number of occurrences of each unique combination of port and protocol found in the
flow log.

Input Files:
============
Below are the input files used by the program, and are configured in

- flow_log.txt: The file containing the flow log data.
Assumption --  that flow-log fields will be in the table format given by AWS as
https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html#flow-logs-fields. The dstport has to be 6th token and protocol has to be 7th.
Any other formats will generate incorrect results

- lookup_table.csv: A CSV file that maps port and protocol combinations to tags.
Data is copied from the program definition given

- protocol_lookup_table.csv (optional): A CSV file that maps protocol numbers to protocol names (used for better readability in the output report).
Note: protocol_lookup_table is needed since lookup_table.csv has mapping with protocol names (e.g. TCP) while flow_log gives protocol as numbers. This mapping file
maps what Amazon protocol numbers to names. For more details, ref https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml

Note: LookupConfig. For simplicity, the values of these files are stored as variables in the LookupConfig, and in reality it can be loaded from
a config/resource file for making the names easily configurable

Output File:
===========
- output.txt: The file containing the analysis report.
Note: For simplicity, the following format for output file is used.
In reality, we could name this in a unique format, by extracting the name of the flowlog file - entire, or part - so that the output can be easily
bound to input filename. And also avoids overwriting. To keep program simple and readable, this is not added, and simply using output.txt as the sole output file.

How to Use:
==========
- Extract all the source code and corresponding mapping files (flow_log.txt, lookup_table.csv, protocol_lookup_table.csv etc) into a java IDE
- Use or replace the content of flow_log.txt, lookup table (lookup_table.csv), and optional protocol lookup table (protocol_lookup_table.csv)
 in the same directory as the Java file.
- Compile all the Java file source codes
- Run the program using the following command: java FlowLogAnalyzer
Note: For simplicity, these instructions are for running the code from an engineer perspective. For productionalization, the java files needs to be
executable, for example export as  executable JAR for similar that kicks off the Main java class FlowLogAnalyzer.


Source Code:
============

The source code for the Flow Log Analyzer program consists of the following Java classes:

FlowLogAnalyzer: This class is the main entry point for the program. It creates a FlowLogParser object and calls its parseAndWriteResults()
method to perform the analysis and write the output to a file.

FlowLogParser: This class parses the flow log file, lookup table, and optional protocol lookup table. It then iterates through the flow log
entries and assigns tags based on the lookup table. Finally, it calculates the tag and port/protocol combination counts and writes the results
to the output file.

FlowLogUtil: This class provides utility functions for loading data from the lookup tables and writing the analysis report to the output file.

ErrorCodes: This class defines error codes used for exception handling.

LookupConfig: This class stores the configuration details for the program, such as the file names for the flow log, lookup tables, and output file.


General Notes
=============
Given the request that the assessment shouldn’t take longer than 2(ish) hours, some of more polishes and production quality is
not added INTENTIONALLY to the source code. The source should be definitely fine tuned for productionalization by including critical designs such as
(a) threading for concurrently analyzing multiple flow logs
(b) Seamless configuration changes without recompilation by loading from resource files
(c) Avoiding overwriting of output files by unique naming
(d) Exporting results to a central storage location so that jobs can be distributed in multiple servers
(e) Archiving old input/output files
(f) Enabling program to read multiple flowlogs same time, and asynchronously kicking off threads to process them in parallel
(g) Automated Test cases are NOT added to keep effort low

Throughout my code, I've put my implementation decisions in comments (why did I use xyz data structure or xyz reader etc). Please have a look at those comments in the java files.
I have tested the code with several input files (from a smaller one to the large one as attached).


Assumptions:
1. While reading the words from the input file, I have trimmed the words for any leading or trailing white spaces.
2. There is no other filter which I have put on the words I've read from the input file, because no criteria was given in the question about what is a word. So even "!!!!!!!!!" is a word.
3. I have put words in output file in such a way that there is one word in each line of the file.


Implementation strategy:
The idea which I've thought of to solve this problem is a bit similar to Hadoop's. There are two steps
1. The input file (very large enough) is read and is broken into temporary files (based on a defined threashold).
2. Then the temporary file are merged to create the output file.
In the first stepI used HashSet and before creating the temp file, I have sorted the data. Set makes sure there are no duplicates. Due to performance reasons, I didn't use TreeSet in first phase. Please read comments in the code for more details.
In the second step, all the temp files are opened and one word from each of them is read one by one. All tem files are already sorted, sso now its only about merging them into the final output file. Please read comments in the code for more details.


Features not included:
There were additional things not included right now, but the application can be extended more to include these:
1. If there is only one temp file created (because size of input file is less than the threshold for temp file), then that temp file should be renamed to the output file, rather than creating a new result file.
2. I want to include logging, more checks throughout the code and I want to include more resources related metrices in my Memory Tracker.
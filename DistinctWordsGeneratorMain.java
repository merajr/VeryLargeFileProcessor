package meraj;

import java.io.IOException;

/**
 * This application is developed to read huge input file (very large file that
 * even doesn't get fit in physical memory, and it generates an output file
 * containing unique words from the input file.
 * 
 * @author Meraj
 *
 */
public class DistinctWordsGeneratorMain {

	public static void main(String args[]) {

		if (args.length != 2) {
			System.out.println(
					"Please provide two file names in runtime arguments, 1. input file name, 2. output file name:");
			System.exit(1);
		}

		// MemoryTracker is a thread that prints the memory status after every 10
		// seconds.
		MemoryTracker tracker = new MemoryTracker();
		tracker.start();
		try {
			// the method call to read the input file and generate temporary files
			int noOfFiles = new LargeFileReader().read(args[0]);

			// the method call to process temporary files and produce output files
			if(noOfFiles > 0) {
				new FileReducer().reduce(args[1], noOfFiles);
			}

			// once the output file is generated, MemoryTracker thread is interrupted and it
			// terminates.
			tracker.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

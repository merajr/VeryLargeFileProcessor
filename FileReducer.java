package meraj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * The reducer class reads the temp files through merging and generates the
 * output file.
 * 
 * @author mrasool
 *
 */
public class FileReducer {

	/**
	 * Reads from temp files and generates output file.
	 * 
	 * @param outputFile
	 *            the final output file
	 * @param noOfFiles
	 *            number of temp files
	 * @throws IOException
	 *             related to IO operations
	 */
	public void reduce(String outputFile, int noOfFiles) throws IOException {

		BufferedReader buffers[] = new BufferedReader[noOfFiles];
		try {
			for (int i = 1; i <= noOfFiles; i++) {
				buffers[i - 1] = new BufferedReader(
						new FileReader(Constants.TEMP_FILE_NAME + (i) + Constants.TEXT_FILE_NAME));
			}

			/*
			 * Here I'm using TreeSet because:
			 * 1. the size of TreeSet here is equal to number of temp files generated,
			 * so the size is not much. So O(logn) insertion is OK.
			 * 2. TreeSet makes sure that the words are unique and sorted.
			 * The HashMap here keeps track of which word is coming from which temp file.
			 * Size of HashMap here is also equal to number of temp files.
			 */
			TreeSet<String> now = new TreeSet<String>();
			Map<String, Integer> tracker = new HashMap<String, Integer>();
			for (int i = 0; i <= noOfFiles - 1; i++) {
				BufferedReader buffer = buffers[i];
				String nowWord = buffer.readLine();
				if (nowWord != null) {
					while (now.contains(nowWord)) {
						nowWord = buffer.readLine();
						if (nowWord == null) {
							break;
						}
					}
					if (nowWord != null) {
						now.add(nowWord);
						tracker.put(nowWord, i);
					}
				}
			}

			reduceAndWrite(outputFile, now, tracker, buffers, noOfFiles);
		}

		finally {
			for (BufferedReader buffer : buffers) {
				if (buffer != null) {
					buffer.close();
				}
			}
		}

		// At the end of all operations (output file is generated), the tem files being generated are deleted.
		removeTempFiles(noOfFiles);
	}

	private void reduceAndWrite(String outputFile, TreeSet<String> now, Map<String, Integer> tracker,
			BufferedReader buffers[], int noOfFiles) throws IOException {

		long before = System.currentTimeMillis();

		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

		System.out.println("The reduced output is being written in :" + outputFile);

		String nextWord, prevWord;

		try {
			while (!now.isEmpty()) {
				nextWord = now.first();
				bw.write(nextWord + "\n");
				int scIndex = tracker.get(nextWord);
				prevWord = nextWord;
				nextWord = buffers[scIndex].readLine();
				if (nextWord != null) {
					while (now.contains(nextWord)) {
						nextWord = buffers[scIndex].readLine();
						if (nextWord == null) {
							break;
						}
					}
					if (nextWord != null) {
						tracker.put(nextWord, scIndex);
						now.add(nextWord);
					}
				}
				tracker.remove(prevWord);
				now.remove(prevWord);
				// System.out.println(tracker);
			}
			bw.flush();

			//TODO The next four lines could be moved into a separate utility method
			long after = System.currentTimeMillis();
			long time = after - before;
			long timeInSec = time / 1000;

			System.out.println("Time spent in reducing results and writing the output (in seconds): " + timeInSec);

		} finally {
			if (bw != null) {
				bw.close();
			}
		}
	}

	private void removeTempFiles(int noOfFiles) {
		System.out.println("Deleting temp files (created during the processing) before exiting.");
		File tempFile;
		StringBuilder fileName = new StringBuilder();
		for (int i = 1; i <= noOfFiles; i++) {
			fileName.append(Constants.TEMP_FILE_NAME + (i) + Constants.TEXT_FILE_NAME);
			tempFile = new File(fileName.toString());

			if (tempFile.exists()) {
				tempFile.delete();
			}
			fileName.delete(0, fileName.length());
		}
	}
}

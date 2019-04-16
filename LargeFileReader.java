package meraj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is a file reader for the large input file, such large that it
 * doesn't get fit into memory. Please read the comments inside for the choices
 * of structures and code strategies used.
 * 
 * @author mrasool
 *
 */
public class LargeFileReader {

	/**
	 * This method reads the large input file to generate the sorted temporary
	 * files. The input files is read line by line and after a certain input is
	 * read, a sorted temporary file is generated. The number of temporary files
	 * generated depends on the size of input file.
	 * 
	 * @param inputFile
	 *            the input file
	 * @return number of temporary files generated
	 * @throws IOException
	 *             related to IO operations used
	 */
	public int read(String inputFile) throws IOException {
		long before = System.currentTimeMillis();
		/*
		 * I used HashSet to contain words from input file. I compared between TreeSet
		 * and HashSet and decided to use HashSet because its faster. Since its a set,
		 * so it makes sure that only contain unique words.
		 */
		Set<String> set = new HashSet<String>();

		/*
		 * I had to take decision of using BufferedReader to read the huge input file
		 * after I did some research on different options of input streams and readers.
		 * At the end, I had two options to choose from, java.util.Scanner and
		 * BufferedReader. BufferedReader had the advantage so I used it.
		 */
		BufferedReader buff = null;
		int index = 0;
		try {
			File input = new File(inputFile);
			System.out.println("Going to read input file:" + inputFile + ", size (bytes):" + input.length());
			buff = new BufferedReader(new FileReader(input));
			String line;
			while ((line = buff.readLine()) != null) {
				String[] words = line.split(" ");
				for (String word : words) {
					set.add(word.trim());
				}
				/*
				 * Once the count of unique words reach a specific size (right now set to 5M
				 * words), a new temp file is generated and the set is cleared. I have taken the
				 * idea similar to Hadoop.
				 */
				if (set.size() > Constants.SET_SIZE) {
					createTempFile(set, ++index);
				}
			}
			if (set.size() > 0) {
				createTempFile(set, ++index);
			}

		} finally {
			if (buff != null) {
				buff.close();
			}
		}

		if (index > 0) {
			System.out.println("There are " + index + " temporary files created.");

			// TODO The next four lines could be moved into a separate utility method
			long after = System.currentTimeMillis();
			long time = after - before;
			long timeInSec = time / 1000;

			System.out.println(
					"Time spent in reading the input files and creating temporary files (in seconds): " + timeInSec);

		}

		return index;
	}

	private void createTempFile(Set<String> set, int index) throws IOException {

		String fileName = Constants.TEMP_FILE_NAME + (index) + Constants.TEXT_FILE_NAME;

		/*
		 * Converting HashSet into ArrayList and sorting it, was the fastest way. I
		 * compared its performance with TreeSet (which is already a sorted data
		 * structure) but TreeSet is slower.
		 */
		List<String> list = new ArrayList<String>();
		list.addAll(set);
		Collections.sort(list);
		File file = new File(fileName);
		// BufferedWriter is the fastest and the best option to write large data into
		// files.
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));

		try {
			for (String next : list) {
				// I am intentionally putting newline character at the end of each word. That
				// way each word is written in one line.
				bw.write(next + "\n");
			}
			bw.flush();
			System.out.println("Created temp file:" + fileName + ", size:" + file.length() + " bytes.");
			set.clear();
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
	}
}

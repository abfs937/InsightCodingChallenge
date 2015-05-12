

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

/*----------------------------------------------------------------
 *  Author:        Rajiv Porumalla
 *  Written:       5/6/2015
 *  Last updated:  5/10/2015
 *
 *  Compilation:   javac -d src/classes src/RunningMedian.java
 *  Execution:     java RunningMedian ../../wc_input ../../wc_output/med_result.txt
 *  
 *  Calculate running median for the number of words per line of text. 
 *  Consider each line in a text file as a new stream of words, and 
 *  find the median number of words per line, up to that point 
 *  (i.e. the median for that line and all the previous lines). 
 *  If there are multiple files in that directory, the files should 
 *  be combined into a single stream and processed by your running median 
 *  program in alphabetical order. The resulting running median for 
 *  each line should then be written to a text file named med_result.txt 
 *  in the wc_output directory.
 * 
 *
 *
 *----------------------------------------------------------------*/
public class RunningMedian {

	private Map<String, String> fileList;					// HashMap with the list of input files	
	private Map<String, String> sortedFileList;				// HashMap with list of sorted input files
	private ArrayList<Integer> words;						// ArrayList containing number of words in each line
	private ArrayList<Double> runningMedian;				// ArrayList containing running medians
	
	
	/**
	 * Stores the list of input file names and sorts them alphabetically
	 * 
	 * @param inputDir
	 */
	public void sortFileNames(File inputDir) {
		File[] listOfFiles = inputDir.listFiles();
		
		for (File file : listOfFiles) {
				if (file.isFile())
					fileList.put(file.getName(), file.getAbsolutePath());	// Map contains filenames (key) and filepath (value)
		}
		
		sortedFileList = new TreeMap<String, String>(fileList);				// Map containing file names sorted in alphabetical order
	}
	
	
	/**
	 * Gets data from files sorted in alphabetical order to SequenceInputStream
	 * and reads lines from the stream one by one and calculates running median
	 * 
	 * @throws IOException 
	 * 
	 */
	public void getCombinedData_calcMedian() throws IOException {
		Vector<InputStream> inputStreams = new Vector<InputStream>();
		int count;
		Double med;
		String line;
		StringTokenizer tokenizer;
		
		for (Map.Entry<String, String> file : sortedFileList.entrySet()) {
			
			try {
				inputStreams.add(new ByteArrayInputStream(readFile(file.getValue()).getBytes()));	// Adds input streams to the vector
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		Enumeration<InputStream> en = inputStreams.elements();	// Used to specify input streams to SequenceInputStram
		SequenceInputStream sis = new SequenceInputStream(en);	// Represents concatenation of input streams
		InputStreamReader isr = new InputStreamReader(sis);		// Reads bytes and decodes them into characters
		BufferedReader br = new BufferedReader(isr);			// Wrapping an InputStreamReader within a BufferedReade

		while ((line = br.readLine()) != null) {				// Reads a line of text
			tokenizer = new StringTokenizer(line);				// Constructs a string tokenizer using default delimiter set
			count = 0;
			while (tokenizer.hasMoreTokens()) {					// Tests if there are more tokens available from this tokenizer's string
				count += 1;
				tokenizer.nextToken();							// The next token from this string tokenizer
			}
			words.add(count);									// Add word count in each line
			Collections.sort(words);							// Sort word count in increasing order
			med = median(words);								// Calculate median
			runningMedian.add(med);								// Add running median to list
		}
		
		br.close();												// Closes the stream
	}
	
	/**
	 * Reads files line by line and adds a new line after each line and returns it as a string
	 * 
	 * @param file
	 * @return file contents as string
	 * @throws IOException
	 */
	private String readFile(String file) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));	// Creates a buffering character-input stream
	    String line = null;
	    StringBuilder stringBuilder = new StringBuilder();	// Constructs a string builder
	    String ls = System.getProperty("line.separator");

	    while((line = reader.readLine()) != null) {
	        stringBuilder.append(line);						// Appends the line
	        stringBuilder.append(ls);						// Appends new line
	    }
	    reader.close();
	    return stringBuilder.toString();					// Return as string
	}
	
	
	/**
	 * Calculates median
	 * 
	 * @param sortedList
	 * @return
	 */
	public double median(ArrayList<Integer> sortedList) {
		double med;
		
		if (sortedList.size() % 2 == 0)
		    med = ((double)sortedList.get(sortedList.size()/2) + (double)sortedList.get(sortedList.size()/2 - 1))/2;
		else
		    med = (double) sortedList.get(sortedList.size()/2);
		
		return med;
	}
	
	
	/**
	 * Write running medians to output file
	 * 
	 * @param file
	 */
	public void writeRunningMediansToFile(String file) {
		Writer writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(file));
			for (Double med : runningMedian) {
				writer.write(med.toString());
				writer.write("\n");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
        } finally {
           try {
        	   writer.close();
           } catch (Exception ex) {
        	   ex.printStackTrace();
           }
        }
	}
	
	
	/**
	 * This is RunningMedian main method which makes use of 
	 * RunningMedian class methods
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final File inputFilesLoc = new File(args[0]);	// Location of input files
		
		RunningMedian rm = new RunningMedian();
		
		// Instantiate class attributes
		rm.fileList = new HashMap<String, String>();
		rm.words = new ArrayList<Integer>();
		rm.runningMedian = new ArrayList<Double>();
		
		rm.sortFileNames(inputFilesLoc);				// Stores and sorts the file names
		
		try {
			rm.getCombinedData_calcMedian();			// Calculate running medians
		} catch (IOException e) {
			e.printStackTrace();
		}
		rm.writeRunningMediansToFile(args[1]);			// Writes running median output to output file
	}

}

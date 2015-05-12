

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;


/*----------------------------------------------------------------
 *  Author:        Rajiv Porumalla
 *  Written:       5/6/2015
 *  Last updated:  5/10/2015
 *
 *  Compilation:   javac -d src/classes src/WordCount.java
 *  Execution:     java WordCount ../../wc_input ../../wc_output/wc_result.txt
 *  
 *  Counts all the words from the text files contained in a directory named 
 *  wc_input and outputs the counts (in alphabetical order) to a file named 
 *  wc_result.txt, which is placed in a directory named wc_output
 *
 *
 *----------------------------------------------------------------*/
public class WordCount {
	private Map<String, List<Integer>> mapperList;	// HashMap with words as keys and value as an ArrayList of 1's
	private Map<String, Integer> mapper;			// HashMap with words as keys and value as sum of 1's of ArrayList
	private Map<String, Integer> sortedMapper;		// HashMap with words sorted in alphabetical order

	
	/**
	 * Gets the words in each line and adds it as keys to the HashMap
	 * and each word occurrence is added to corresponding ArrayList 
	 * value as 1
	 * 
	 * 
	 * @param line
	 */
	public void Mapper(String line) {
		StringTokenizer tokenizer = new StringTokenizer(line);
		String word;
		List<Integer> l;
		
		while (tokenizer.hasMoreTokens()) {
			word = tokenizer.nextToken().toLowerCase().replaceAll("[^\\w]", "").replace("_", "");	// Convert all words to lower case and remove non-alphabet characters
			
			if (word.length() > 0) {
				l = mapperList.get(word);															// Get the value associated with key (word)
				if (l == null) 																		// Check if there is a value (list) associated with the key (word)
					mapperList.put(word, l = new ArrayList<Integer>());								// Creates ArrayList as value for the key (word)
				l.add(1);																			// For each word occurrence add 1 to the ArrayList
			}
		}
	}
	
	
	/**
	 * Adds up the 1's in the ArrayList of individual occurrences of words 
	 * and stores in a HashMap with word as key and number of occurrences 
	 * as value
	 * 
	 */
	public void countOccurrences() {
		String key;
		
		// Get an iterator
	    Iterator<String> keySetIterator = mapperList.keySet().iterator();
	    
	    // Display elements
        while(keySetIterator.hasNext()) {
        	key = keySetIterator.next();
        	
        	// Add word (key) and number of occurrences (value) to hashmap
        	if (mapperList.get(key).size() > 1)
        		mapper.put(key, sum(mapperList.get(key)));
        	else
        		mapper.put(key, 1);
        }
	}

	
	/**
	 * Sums up the entries in the List of integers
	 * 
	 * @param list of integers
	 * @return sum of integers in list
	 */
	public Integer sum(List<Integer> list) {
	     Integer sum = 0; 
	     
	     for (Integer i : list)
	         sum = sum + i;
	     return sum;
	}
	
	
	/**
	 * Reads multiple files in the input directory. The text in all the files are 
	 * further sent to mapper function 
	 * 
	 * @param inputDir
	 */
	public void readFromFiles(File inputDir) {
		String text = "";
		int read, N = 1024 * 1024;
		char[] buffer = new char[N];
		
		File[] listOfFiles = inputDir.listFiles();		// Stores in an array the list of files in a directory
		BufferedReader br = null;
		
		// Reading file content
		for (File file : listOfFiles) {
			try {
			    FileReader fr = new FileReader(file);
			    br = new BufferedReader(fr);			// Creates a buffering character-input stream
	
			    while(true) {
			        read = br.read(buffer, 0, N);		// Reads characters into destination buffer array
			        text += new String(buffer, 0, read);// Allocates a new String that contains characters from a subarray of the character array argument
	
			        if(read < N) {
			            break;
			        }
			    }
			} catch(Exception ex) {
			    ex.printStackTrace();
			} finally {
				if (br != null)
					try {
						br.close();				// Closes the stream
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}

		Mapper(text);							// Call mapper function
	}
	
	
	/**
	 * Writes the WordCount output to a file
	 * 
	 * @param outputFile
	 */
	public void writeToFile(String outputFile) {
		Writer writer = null;
		String key;
		
		// Get an iterator
	    Iterator<String> keySetIterator = sortedMapper.keySet().iterator();
		try {
            writer = new BufferedWriter(new FileWriter(outputFile));		// Writer object to write to file
            while(keySetIterator.hasNext()) {								// Check for keys presence in hashmap
            	key = keySetIterator.next();
            	writer.write(key + "\t" + sortedMapper.get(key));			// Write key to output file
            	writer.write("\n");											// Write newline to output file
            }
        } catch (IOException ex) {
          	ex.printStackTrace();
        } finally {
           try {
        	   writer.close();												// Close writer object
           } catch (Exception ex) {
        	   ex.printStackTrace();
           }
        }
	}

	
	/**
	 * This is the main method which makes use of 
	 * WordCount class methods 
	 * 
	 * @param args
	 * @return Nothing
	 */
	public static void main(String[] args) {
		final File inputFilesLoc = new File(args[0]);	// Location of input files
		
		WordCount wc 	= new WordCount();
		
		// Instantiate class attributes
		wc.mapperList 	= new HashMap<String, List<Integer>>();
		wc.mapper 		= new HashMap<String, Integer>();
		
		wc.readFromFiles(inputFilesLoc);				// Reads input file data and maps each word with arraylist of occurrences
        wc.countOccurrences();							// Counts the occurrence of each word
        wc.sortedMapper = new TreeMap<String, Integer>(wc.mapper);	// Sorts the words in alphabetical order
        wc.writeToFile(args[1]);						// Writes WordCount output to output file
	}
}

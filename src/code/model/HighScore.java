package code.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class HighScore implements Iterator<String> {

	private Scanner _scan;
	
	public HighScore(String filename) {
		try {
			_scan = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("Sorry, I could not open a file named \""+filename+"\".");
		}
	}
	
	public boolean hasNext() {
		return _scan.hasNext();
	}
	
	public String next() {
		return _scan.nextLine();
	}
	
	public void close() {
		_scan.close();
	}
  
}

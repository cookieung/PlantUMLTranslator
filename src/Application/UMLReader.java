package Application;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * It's a word counter machine.
 * @author Salilthip 5710546640
 *
 */
public class UMLReader {

	
	/**
	 * For count the number of word from url.
	 * @param url is a source of String.
	 * @return the number of word
	 */
	public String readAllLine(URL url) {
		InputStream in =null;
		try {
			in = url.openStream( );
		} catch (IOException e) {

		}
		return readAllLine(in);
	}


	/**
	 * For count the number of word from InputStream.
	 * @param inputStream is a source of string.
	 * @return the number of syllables.
	 */
	public String readAllLine(InputStream inputStream){
		String str ="";
		Scanner scanner = new Scanner(inputStream);
		
		while(scanner.hasNext()){
			String wd = scanner.nextLine();
			str+=wd+"\n";
		}

		return str;
	}
	
	public String fileName(String url) {
		String[] res = url.split("/");
		String[] rs = res[res.length-1].split("\\.");
		return rs[0];
	}
	

	

}



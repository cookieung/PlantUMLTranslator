package Controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class WriterCSP {

	
	public void writing(String output,String pathSave) {
        try {
            //Whatever the file path is.
            File statText = new File(pathSave);
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            w.write(output);
            w.close();
        } catch (IOException e) {
            System.err.println("Have problem about writing!!!");
        }

    }

}

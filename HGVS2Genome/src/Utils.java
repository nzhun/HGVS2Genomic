import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class Utils {

	public static void write(String filename,String string,boolean append){
		    BufferedWriter output = null;
	        try {
	            File file = new File(filename); 
	            output = new BufferedWriter(new FileWriter(file,append));
	            output.write(string);
	        } catch ( IOException e ) {
	            e.printStackTrace();
	        } finally {      
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			
	          
	        }
		
	}

	public static void writeHeader(String vcf_file, boolean b) throws IOException {
		    BufferedWriter output = null;
		    output = new BufferedWriter(new FileWriter(vcf_file,b));
			FileInputStream fstream = new FileInputStream("header.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				output.write(strLine+"\n");
			}
			br.close();
	        output.close();
				
	          
	        
		
	}
}

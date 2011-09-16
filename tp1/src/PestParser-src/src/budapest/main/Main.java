package budapest.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import budapest.pest.ast.proc.Program;
import budapest.pest.parser.ParseException;
import budapest.pest.parser.PestParser;
import budapest.pest.pesttocvc3.PestToCVC3Exception;
import budapest.pest.pesttocvc3.PestToCVC3Translator;
import budapest.pest.predtocvc3.PredToCVC3Translator;

public class Main {

	public static enum OutputType { CONSOLE, FILE };
	
	//Options... (In the future should be taken from args. Not now since this is better for debugging)
	private static Boolean isDebugging = true;
	private static Boolean executeCVC3 = true;
	private static OutputType outputType = OutputType.FILE;
	private static String inputFile = "tests/test9.pest";
			
	public static void main(String[] args) {
				
		try
		{
			Program p = GetProgramFromFile(inputFile);
			String cvc3String = GetCVC3StringFromProgram(p);
						
			if(!executeCVC3) 
			{
				if(outputType == OutputType.CONSOLE)
					System.out.println(cvc3String);
				else
					PrintStringToNewFile(cvc3String, GetFileNameWithExtension(inputFile, "cvc3"));
			}
			else 
			{
				//Saving the cvc3 query in a file...
				String cvc3File = GetFileNameWithExtension(inputFile, "cvc3");
				PrintStringToNewFile(cvc3String, cvc3File);
				
				//Executing the query from that file...
				String result = ExecuteCVC3WithQuery(cvc3File);
				result = cvc3String + "\nCVC3 Result >> " + result;
				
				if(outputType == OutputType.CONSOLE) {
					System.out.println(result);
					
					//If the user wanted the console output, delete the cvc3 temporal file...
					File tempFile = new File(cvc3File);
					if(tempFile.exists())
						tempFile.delete();
				}
				else
					PrintStringToNewFile(result, GetFileNameWithExtension(inputFile, "cvc3result"));
			}
		}
		catch(PestToCVC3Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	private static String ExecuteCVC3WithQuery(String query) throws PestToCVC3Exception {
		try
		{
			String[] args = {"cvc3/cvc3.exe", query};
			Process p = Runtime.getRuntime().exec(args);
		    int exitValue = p.waitFor();
		    String result = "";
		    InputStream is = exitValue == 0 ? p.getInputStream() : p.getErrorStream();
		    BufferedReader input = new BufferedReader(new InputStreamReader(is));
            String line=null;
            while((line=input.readLine()) != null) {
                result += line + "\n";
            }
            input.close();
			return result;
		}
		catch(Exception e)
		{
			ThrowPestToCVC3Exception("Ha ocurrido un error al tratar de ejecutar cvc3 con el query " + query + ".", e);
		}
		return null;
	}
	
	private static String GetFileNameWithExtension(String fileName, String extension) {
		String result = "";
		int extensionIndex = fileName.lastIndexOf(".");
		if(extensionIndex == -1)
			result = fileName;
		else {
			result = fileName.substring(0, extensionIndex + 1) + extension;
		}
		return result;
	}
	
	private static void PrintStringToNewFile(String str, String fileName) throws PestToCVC3Exception {
		try
		{
			FileOutputStream fos = new FileOutputStream(fileName);
			PrintWriter writer = new PrintWriter(fos);
			writer.println(str);
			writer.close();
			fos.close();
		}
		catch(Exception e)
		{
			ThrowPestToCVC3Exception("Ha ocurrido un error al tratar de salvar el query cvc3 en el archivo " + fileName + ".", e);
		}
	}
	
	private static String GetCVC3StringFromProgram(Program p) throws PestToCVC3Exception {
		try
		{
			//Pred vc = new PestVCGenerator().execute(p);
			new PestToCVC3Translator().execute(p);
			return "CVC";
		}
		catch(Exception e)
		{
			ThrowPestToCVC3Exception("Ha ocurrido un error al tratar de obtener la condicion de verification en sintaxis cvc3.", e);
		}
		return null;
	}
	
	private static Program GetProgramFromFile(String inputFile) throws PestToCVC3Exception {
		Program p = null;
		try 
		{
			FileInputStream fis = new FileInputStream(inputFile);
			PestParser parser = new PestParser(fis);
			try 
			{
				p = parser.Program();
			} 
			catch (ParseException e) 
			{
				ThrowPestToCVC3Exception("Ha ocurrido un error mientras se parseaba el archivo " + inputFile + ".", e);
			}
			finally {
				fis.close();
			}
		} 
		catch (FileNotFoundException e) 
		{
			ThrowPestToCVC3Exception("No se ha encontrado el archivo " + inputFile + ".", e);
		} 
		catch (IOException e) 
		{
			ThrowPestToCVC3Exception("Ha ocurrido un error al tratar de parsear el programa en el archivo " + inputFile + ".", e);
		}
		return p;
	}
	
	private static void ThrowPestToCVC3Exception(String message, Exception e) throws PestToCVC3Exception {
		if(isDebugging){
			message += " Stack trace: " + e.getStackTrace();
		}
		throw new PestToCVC3Exception(message);
	}

}

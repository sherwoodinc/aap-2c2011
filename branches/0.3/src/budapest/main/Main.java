package budapest.main;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import budapest.pest.ast.proc.Program;
import budapest.pest.parser.ParseException;
import budapest.pest.parser.PestParser;
import budapest.pest.pesttocvc3.PestToCVC3Exception;
import budapest.pest.typeinference.PestProgramTypeInferenceResult;
import budapest.pest.typeinference.PestTypeInferenceManager;

public class Main {

	public static enum OutputType { CONSOLE, FILE };
	
	private static String inputFile = "tests/testInference.pest";
			
	public static void main(String[] args) {
		/*for (String inputFile : args)
		{*/
			try
			{
				Program p = GetProgramFromFile(inputFile);
				System.out.print(p.toString());
				
				PestTypeInferenceManager inferenceManager = new PestTypeInferenceManager();
				PestProgramTypeInferenceResult result = inferenceManager.infere(p);
				System.out.println(result.toString());
			
			}
			catch(PestToCVC3Exception e)
			{
				System.out.println(e.getMessage());
			}
		/*
		}
		*/
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
				ThrowException("Error occurred while parsing file " + inputFile + ".", e);
			}
			finally {
				fis.close();
			}
		} 
		catch (FileNotFoundException e) 
		{
			ThrowException("Could not find file " + inputFile + ".", e);
		} 
		catch (IOException e) 
		{
			ThrowException("Error occurred while trying to parse program in file " + inputFile + ".", e);
		}
		return p;
	}
	
	private static void ThrowException(String message, Exception e) throws PestToCVC3Exception {
		
		StringWriter stackAsString = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stackAsString);
		e.printStackTrace(printWriter);
		message += " Stack trace: " + stackAsString.toString();
				
		throw new PestToCVC3Exception(message);
	}

}

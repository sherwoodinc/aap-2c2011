package budapest.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.proc.Program;
import budapest.pest.parser.ParseException;
import budapest.pest.parser.PestParser;
import budapest.pest.predtocvc3.PredToCVC3Translator;
import budapest.pest.vcgenerator.PestVCGenerator;

public class Main {

	public static void main(String[] args) {
		
		FileInputStream fis;
		Program p;
		try 
		{
			fis = new FileInputStream("tests/test5.pest");
			PestParser parser = new PestParser(fis);
			try 
			{
				p = parser.Program();
			} 
			catch (ParseException e) 
			{
				e.printStackTrace();
				return;
			}
			fis.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
				
		try
		{
			Pred vc = new PestVCGenerator().execute(p);
			String cvc3string = new PredToCVC3Translator().execute(vc);
			System.out.println(cvc3string);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		
	}

}

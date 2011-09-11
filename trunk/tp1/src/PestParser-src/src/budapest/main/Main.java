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
			fis = new FileInputStream("tests/testSeqAssign.pest");
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
			Pred verificationCondition = new PestVCGenerator().execute(p);
			String cvc3string = verificationCondition.accept(new PredToCVC3Translator(), null);
			System.out.println("QUERY " + cvc3string + ";");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		
	}

}

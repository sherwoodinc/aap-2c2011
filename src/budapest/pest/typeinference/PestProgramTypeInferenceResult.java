package budapest.pest.typeinference;

import java.util.ArrayList;
import java.util.List;

public class PestProgramTypeInferenceResult {

	public List<PestProcedureTypeInferenceResult> procsInference;
	
	public boolean succeeded;
	
	public String message;
		
	public PestProgramTypeInferenceResult()
	{
		 procsInference = new ArrayList<PestProcedureTypeInferenceResult>();
	}
	
	public String toString()
	{
		String result = message + "\n";
		for(PestProcedureTypeInferenceResult proc : procsInference)
		{
			result += "\n" + proc.toString();
		}
		return result;
	}
}

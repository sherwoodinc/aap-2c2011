package budapest.pest.typeinference;

public class PestProcedureTypeInferenceResult {

	public final String procedureName;
	
	public final PestTypedContext context;
	
	public final boolean succeeded;
	
	public final String message;
		
	public PestProcedureTypeInferenceResult(String procedureName, PestTypedContext context, boolean succeeded, String message)
	{
		this.procedureName = procedureName;
		this.context = context;
		this.succeeded = succeeded;
		this.message = message; 
	}
	
	public String toString()
	{
		String result = "Procedure: " + procedureName;
		result += "\n---------------------\n";
		result += message;
		if(succeeded && context != null)
		{
			result += "\nVariable typing: " + context.toString();
		}
		return result;
	}
}

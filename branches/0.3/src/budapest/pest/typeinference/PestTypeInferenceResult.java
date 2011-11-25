package budapest.pest.typeinference;

public class PestTypeInferenceResult {

	public final PestTypedContext context;
	
	public final boolean succeeded;
	
	public final String message;
		
	public PestTypeInferenceResult(PestTypedContext context, boolean succeeded, String message)
	{
		this.context = context;
		this.succeeded = succeeded;
		this.message = message; 
	}
}

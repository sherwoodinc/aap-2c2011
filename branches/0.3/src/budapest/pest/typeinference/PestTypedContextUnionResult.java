package budapest.pest.typeinference;

public class PestTypedContextUnionResult {

	public final boolean succeeded;
	
	public final String message;
	
	public PestTypedContextUnionResult(boolean succeeded, String message)
	{
		this.succeeded = succeeded;
		this.message = message;
	}
}

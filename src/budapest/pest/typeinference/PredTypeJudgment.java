package budapest.pest.typeinference;

import budapest.pest.ast.pred.Pred;
import budapest.pest.dump.pest.PredPrinter;

public class PredTypeJudgment {

	public final boolean isValid;
	
	public final PestType type;
	
	public final PestTypedContext context;
	
	public final Pred pred;
	
	public final String status;
		
	public PredTypeJudgment(PestType type, PestTypedContext context, Pred pred, boolean isValid, String status)
	{
		this.type = type;
		this.context = context;
		this.pred = pred;
		this.isValid = isValid;
		this.status = status;
	}
	
	public PredTypeJudgment(boolean isValid, String status)
	{
		this(null, null, null, isValid, status);
	}
	
	public PredTypeJudgment(PestType type, PestTypedContext context, Pred pred)
	{
		this(type, context, pred, true, "");
	}
	
	public String toString()
	{
		String retValue = ""; 
		if(isValid)
		{
			String predToString = pred.accept(new PredPrinter(), null);
			retValue = "Pred " + predToString + " is of type " + type.getTypeName();
		}
		else
		{
			retValue = status;
		}
		return retValue;
	}
}

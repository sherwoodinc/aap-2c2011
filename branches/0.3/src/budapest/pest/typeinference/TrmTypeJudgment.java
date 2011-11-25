package budapest.pest.typeinference;

import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.dump.pest.TrmPrinter;

public class TrmTypeJudgment {

	public final boolean isValid;
	
	public final PestType type;
	
	public final PestTypedContext context;
	
	public final Trm term;
	
	public final String status;
		
	public TrmTypeJudgment(PestType type, PestTypedContext context, Trm term, boolean isValid, String status)
	{
		this.type = type;
		this.context = context;
		this.term = term;
		this.isValid = isValid;
		this.status = status;
	}
	
	public TrmTypeJudgment(boolean isValid, String status)
	{
		this(null, null, null, isValid, status);
	}
	
	public TrmTypeJudgment(PestType type, PestTypedContext context, Trm term)
	{
		this(type, context, term, true, "");
	}
	
	public String toString()
	{
		String retValue = ""; 
		if(isValid)
		{
			String termToString = term.accept(new TrmPrinter(), null);
			retValue = "Term " + termToString + " is of type " + type.getTypeName();
		}
		else
		{
			retValue = status;
		}
		return retValue;
	}
}

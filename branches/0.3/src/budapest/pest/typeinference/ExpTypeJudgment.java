package budapest.pest.typeinference;

import budapest.pest.ast.exp.Exp;
import budapest.pest.dump.pest.ExpPrinter;

public class ExpTypeJudgment {

	public final boolean isValid;
	
	public final PestType type;
	
	public final PestTypedContext context;
	
	public final Exp expression;
	
	public final String status;
		
	public ExpTypeJudgment(PestType type, PestTypedContext context, Exp expression, boolean isValid, String status)
	{
		this.type = type;
		this.context = context;
		this.expression = expression;
		this.isValid = isValid;
		this.status = status;
	}
	
	public ExpTypeJudgment(boolean isValid, String status)
	{
		this(null, null, null, isValid, status);
	}
	
	public ExpTypeJudgment(PestType type, PestTypedContext context, Exp expression)
	{
		this(type, context, expression, true, "");
	}
	
	public String toString()
	{
		String retValue = ""; 
		if(isValid)
		{
			String expressionToString = expression.accept(new ExpPrinter(), null);
			retValue = "Expression " + expressionToString + " is of type " + type.getTypeName();
		}
		else
		{
			retValue = status;
		}
		return retValue;
	}
}

package budapest.pest.typeinference;

import budapest.pest.ast.exp.Exp;

public final class PestTypeJudgment {
	
	public final PestType type;
	
	public final PestTypedContext context;
	
	public final Exp expression;
	
	public PestTypeJudgment(PestType type, PestTypedContext context, Exp expression)
	{
		this.type = type;
		this.context = context;
		this.expression = expression;
	}
}

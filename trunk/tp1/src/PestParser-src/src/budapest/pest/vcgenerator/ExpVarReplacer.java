package budapest.pest.vcgenerator;

import budapest.pest.ast.exp.BoolExp;
import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.exp.IntExp;
import budapest.pest.ast.visitor.ExpVisitor;

public class ExpVarReplacer extends ExpVisitor<Exp, VarReplacement> {

	public Exp visit(BoolExp n, VarReplacement arg) {
		return n.accept(new BoolExpVarReplacer(), arg);
	}
	
	public Exp visit(IntExp n, VarReplacement arg) {
		return n.accept(new IntExpVarReplacer(), arg);
	}
}

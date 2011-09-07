package budapest.pest.vcgenerator;

import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.BoolExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.ast.visitor.ExpVisitor;

public class BoolExpVarReplacer extends ExpVisitor<BoolExp, VarReplacement> {

	public BoolExp visit(BinBoolExp n, VarReplacement arg) {
		return new BinBoolExp(n.line,
				n.column,
				n.left.accept(this, arg),
				n.op,
				n.right.accept(this, arg));
	}
	
	public BoolExp visit(NotBoolExp n, VarReplacement arg) {
		return new NotBoolExp(n.line,
				n.column,
				n.subExp.accept(this, arg));
	}
	
	public BoolExp visit(RelBoolExp n, VarReplacement arg) {
		return new RelBoolExp(n.line,
				n.column,
				n.left.accept(new IntExpVarReplacer(), arg),
				n.op,
				n.right.accept(new IntExpVarReplacer(), arg));
	}
}

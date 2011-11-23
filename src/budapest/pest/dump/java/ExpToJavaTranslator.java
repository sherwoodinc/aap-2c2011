package budapest.pest.dump.java;

import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.VarExp;
import budapest.pest.dump.pest.ExpPrinter;

public final class ExpToJavaTranslator extends ExpPrinter {

	@Override
	public String visit(BinBoolExp n, Void arg) {
		String ret = "((" + n.left.accept(this, arg) + " != 0";
		ret += " " + boolOp(n.op) + " ";
		ret += n.right.accept(this, arg) + " != 0)";
		ret += " ? 1 : 0)";
		return ret;
	}
	
	@Override
	public String visit(RelBoolExp n, Void arg) {
		String ret = "(" + n.left.accept(this, arg);
		ret += " " + relOp(n.op) + " ";
		ret += n.right.accept(this, arg);
		ret += " ? 1 : 0)";
		return ret;
	}
	
	@Override
	public String visit(NotBoolExp n, Void arg) {
		return "(" + n.subExp.accept(this, arg) + " == 0 ? 1 : 0)";
	}
	
	@Override
	public String visit(VarExp n, Void arg) {
		return n.name + "[0]";
	}
	
	private String relOp(RelBoolExp.Operator op) {
		switch(op) {
		case EQ:
			return "==";
		case GE:
			return ">=";
		case GT:
			return ">";
		case LE:
			return "<=";
		case LT:
			return "<";
		case NEQ:
			return "!=";
		}
		return null;
	}

	private String boolOp(budapest.pest.ast.exp.BinBoolExp.Operator op) {
		switch(op) {
		case AND:
			return "&&";
		case OR:
			return "||";
		}
		return null;
	}
	
}

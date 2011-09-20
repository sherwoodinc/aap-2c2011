package budapest.pest.dump.pest;

import budapest.pest.ast.exp.ArithIntExp;
import budapest.pest.ast.exp.ArrayAccessIntExp;
import budapest.pest.ast.exp.ArraySizeIntExp;
import budapest.pest.ast.exp.BinBoolExp;
import budapest.pest.ast.exp.LiteralIntExp;
import budapest.pest.ast.exp.NegIntExp;
import budapest.pest.ast.exp.RelBoolExp;
import budapest.pest.ast.exp.NotBoolExp;
import budapest.pest.ast.exp.VarIntExp;
import budapest.pest.ast.visitor.ExpVisitor;
import budapest.util.printer.prec.Brackets;

public class ExpPrinter extends ExpVisitor<String, Void> {
	
	@Override
	public String visit(ArithIntExp n, Void arg) {
		String ret = Brackets.bracketsIfNeeded(n, n.left, this, arg);
		ret += " ";
		
		switch(n.op) {
		case ADD:
			ret += "+";
			break;
		case DIV:
			ret += "/";
			break;
		case MUL:
			ret += "*";
			break;
		case SUBS:
			ret += "-";
			break;
		}
		
		ret += " ";
		ret += Brackets.bracketsIfNeeded(n, n.right, this, arg);
		return ret;
	}
	
	@Override
	public String visit(BinBoolExp n, Void arg) {
		String ret = Brackets.bracketsIfNeeded(n, n.left, this, arg);
		ret += " ";
		
		switch(n.op) {
		case AND:
			ret += "&&";
			break;
		case OR:
			ret += "||";
			break;
		}
		
		ret += " ";
		ret += Brackets.bracketsIfNeeded(n, n.right, this, arg);
		return ret;
	}
	
	@Override
	public String visit(RelBoolExp n, Void arg) {
		String ret = Brackets.bracketsIfNeeded(n, n.left, this, arg);
		ret += " ";
		
		switch(n.op) {
		case EQ:
			ret += "=";
			break;
		case GE:
			ret += ">=";
			break;
		case GT:
			ret += ">";
			break;
		case LE:
			ret += "<=";
			break;
		case LT:
			ret += "<";
			break;
		case NEQ:
			ret += "!=";
			break;
		}
		
		ret += " ";
		ret += Brackets.bracketsIfNeeded(n, n.right, this, arg);
		return ret;
	}

	@Override
	public String visit(NotBoolExp n, Void arg) {
		String ret = "! ";
		ret += Brackets.bracketsIfNeeded(n, n.subExp, this, arg);
		return ret;
	}

	@Override
	public String visit(NegIntExp n, Void arg) {
		String ret = "-";
		ret += Brackets.bracketsIfNeeded(n, n.subExp, this, arg);
		return ret;
	}
	
	@Override
	public String visit(LiteralIntExp n, Void arg) {
		return Integer.toString(n.value);
	}

	@Override
	public String visit(VarIntExp n, Void arg) {
		return n.name;
	}
	
	@Override
	public String visit(ArraySizeIntExp n, Void arg) {
		return "|" + n.array + "|";
	}
	
	@Override
	public String visit(ArrayAccessIntExp n, Void arg) {
		return n.array + "[" + n.index.accept(this, arg) + "]";
	}

}

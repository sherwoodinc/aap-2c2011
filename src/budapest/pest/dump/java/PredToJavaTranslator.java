package budapest.pest.dump.java;

import budapest.pest.ast.pred.BinaryPred;
import budapest.pest.ast.pred.BooleanLiteralPred;
import budapest.pest.ast.pred.QuantifiedPred;
import budapest.pest.ast.pred.RelationPred;
import budapest.pest.ast.pred.NotPred;
import budapest.pest.ast.visitor.PredVisitor;
import budapest.util.printer.Printer;

public final class PredToJavaTranslator extends PredVisitor<Void, String> {

	private int retSuffix = 0;
	private final Printer printer;
	
	public PredToJavaTranslator(final Printer printer) {
		this.printer = printer;
	}

	@Override
	public Void visit(BinaryPred n, String retv) {
		String retLeft = uniqueRetName();
		String retRight = uniqueRetName();
		
		n.left.accept(this, retLeft);
		printer.println("boolean " + retv + ";");
		switch(n.op) {
		case AND:
			printer.println("if(" + retLeft + ") {");
			printer.indent();
			n.right.accept(this, retRight);
			printer.println(retv + " = " + retRight + ";");
			printer.unindent();
			printer.println("} else {");
			printer.indent();
			printer.println(retv + " = " + retLeft + ";");
			printer.unindent();
			printer.println("}");
			break;
		case OR:
			printer.println("if(!" + retLeft + ") {");
			printer.indent();
			n.right.accept(this, retRight);
			printer.println(retv + " = " + retRight + ";");
			printer.unindent();
			printer.println("} else {");
			printer.indent();
			printer.println(retv + " = " + retLeft + ";");
			printer.unindent();
			printer.println("}");
			break;
		case IMPLIES:
			printer.println("if(" + retLeft + ") {");
			printer.indent();
			n.right.accept(this, retRight);
			printer.println(retv + " = " + retRight + ";");
			printer.unindent();
			printer.println("} else {");
			printer.indent();
			printer.println(retv + " = !" + retLeft + ";");
			printer.unindent();
			printer.println("}");
			break;
		case IFF:
			n.right.accept(this, retRight);
			printer.println(retv + " = " + retLeft + " == " + retRight + ";");
			break;
		}
		
		return null;
	}
	
	@Override
	public Void visit(QuantifiedPred n, String retv) {
		printer.print("boolean " + retv + " = ");
		
		// base case
		switch(n.type) {
		case EXISTS:
			printer.print("false");
			break;
		case FORALL:
			printer.print("true");
			break;
		}
		printer.println(";");
		
		String retSub = uniqueRetName();
		
		String i = n.var;
		String low = n.lowerBound.accept(new TrmToJavaTranslator(), null);
		String high = n.upperBound.accept(new TrmToJavaTranslator(), null);
		printer.println("for(int[] " + i + " = {" + low + "}; " + 
			i + "[0] <= " + high + "; " + i + "[0]++) {");
		printer.indent();

		n.subPred.accept(this, retSub);
		
		printer.print(retv + " = " + retv + " ");
		
		switch(n.type) {
		case EXISTS:
			printer.print("||");
			break;
		case FORALL:
			printer.print("&&");
			break;
		}
		
		printer.println(" " + retSub + ";");
		
		printer.unindent();
		printer.println("}");
		
		return null;
	}
	
	@Override
	public Void visit(BooleanLiteralPred n, String retv) {
		printer.print("boolean " + retv + " = ");
		switch(n.type) {
		case FALSEE:
			printer.print("false");
			break;
		case TRUEE:
			printer.print("true");
			break;
		}
		printer.println(";");
		
		return null;
	}
	
	@Override
	public Void visit(NotPred n, String retv) {
		String retSub = uniqueRetName();
		n.subPred.accept(this, retSub);
		
		printer.print("boolean " + retv + " = ");
		printer.print("!" + retSub);
		printer.println(";");
		
		return null;
	}
	
	@Override
	public Void visit(RelationPred n, String retv) {
		printer.print("boolean " + retv + " = ");
		printer.print(n.left.accept(new TrmToJavaTranslator(), null));
		printer.print(" ");
		
		switch(n.op) {
		case EQ:
			printer.print("==");
			break;
		case GE:
			printer.print(">=");
			break;
		case GT:
			printer.print(">");
			break;
		case LE:
			printer.print("<=");
			break;
		case LT:
			printer.print("<");
			break;
		case NEQ:
			printer.print("!=");
			break;
		}
	
		printer.print(" ");
		printer.print(n.right.accept(new TrmToJavaTranslator(), null));
		printer.println(";");
		
		return null;
	}
	
	private String uniqueRetName() {
		String retv = "ret" + retSuffix;
		retSuffix++;
		return retv;
	}

}

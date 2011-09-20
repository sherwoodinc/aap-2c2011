package budapest.util.printer.prec;

import budapest.pest.ast.exp.Exp;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.visitor.ExpVisitor;
import budapest.pest.ast.visitor.PredVisitor;
import budapest.pest.ast.visitor.TrmVisitor;

public final class Brackets {

	// Each class has a separate method. Maybe there's a nicer way to do it.
	
	public static <A> String bracketsIfNeeded(Exp out, Exp in, 
			ExpVisitor<String, A> printer, A arg) {
		if(out.getPrecedence().compareTo(in.getPrecedence()) > 0)
			return "(" + in.accept(printer, arg) + ")";
		else
			return in.accept(printer, arg);
	}
	
	public static <A> String bracketsIfNeeded(Pred out, Pred in, 
			PredVisitor<String, A> printer, A arg) {
		if(out.getPrecedence().compareTo(in.getPrecedence()) > 0)
			return "(" + in.accept(printer, arg) + ")";
		else
			return in.accept(printer, arg);
	}
	
	public static <A> String bracketsIfNeeded(Trm out, Trm in, 
			TrmVisitor<String, A> printer, A arg) {
		if(out.getPrecedence().compareTo(in.getPrecedence()) > 0)
			return "(" + in.accept(printer, arg) + ")";
		else
			return in.accept(printer, arg);
	}
	
}

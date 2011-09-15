package budapest.pest.dump.pest;

import budapest.pest.ast.proc.Procedure;
import budapest.pest.ast.proc.Program;
import budapest.pest.ast.stmt.AssertStmt;
import budapest.pest.ast.stmt.AssignStmt;
import budapest.pest.ast.stmt.AssumeStmt;
import budapest.pest.ast.stmt.BlockStmt;
import budapest.pest.ast.stmt.CallStmt;
import budapest.pest.ast.stmt.IfStmt;
import budapest.pest.ast.stmt.LocalDefStmt;
import budapest.pest.ast.stmt.LoopStmt;
import budapest.pest.ast.stmt.SkipStmt;
import budapest.pest.ast.stmt.Stmt;
import budapest.pest.dump.PestTranslator;

public final class PestPrinter extends PestTranslator {

	@Override
	public Void visit(Program n, Void arg) {
		for(Procedure proc : n.procs) {
			proc.accept(this, arg);
			printer.println();
		}
		return null;
	}
	
	@Override
	public Void visit(Procedure n, Void arg) {
		// name (params,..)
		printer.print(n.name + "(");
		printer.printInfix(n.params, ", ");
		printer.println(")");
		
		// pre
		printer.println(":? " + n.pre.accept(new PredPrinter(), arg));
		
		// post
		printer.println(":! " + n.post.accept(new PredPrinter(), arg));
		
		// touches
		if(n.touches.size() > 0) {
			printer.print(":* ");
			printer.printInfix(n.touches, ", ");
			printer.println();
		}
		
		// body
		printer.println("{");
		printer.indent();
		n.stmt.accept(this, arg);
		printer.unindent();
		printer.println("}");
		return null;
	}
	
	@Override
	public Void visit(CallStmt n, Void arg) {
		printer.print(n.procName + "(");
		printer.printInfix(n.params, ", ");
		printer.println(")");
		return null;
	}

	@Override
	public Void visit(AssignStmt n, Void arg) {
		printer.print(n.left.accept(new ExpPrinter(), arg));
		printer.print(" <- ");
		printer.println(n.right.accept(new ExpPrinter(), arg));
		return null;
	}
	
	@Override
	public Void visit(BlockStmt n, Void arg) {
		printBlockOrStmt(n);
		return null;
	}
	
	@Override
	public Void visit(IfStmt n, Void arg) {
		printer.println("if " + n.condition.accept(new ExpPrinter(), arg) + " then");
		printBlockOrStmt(n.thenS);
		
		// print else only if its not a skip stmt
		if(!(n.elseS instanceof SkipStmt)) {
			printer.println("else");
			printBlockOrStmt(n.elseS);
		}
		return null;
	}

	@Override
	public Void visit(LoopStmt n, Void arg) {
		printer.println("while " + n.condition.accept(new ExpPrinter(), arg));
		printer.indent();
		printer.println(":?! " + n.invariant.accept(new PredPrinter(), arg));
		printer.println(":# " + n.variant.accept(new TrmPrinter(), arg));
		printer.unindent();
		printer.println("do");
		printBlockOrStmt(n.body);
		return null;
	}
	
	@Override
	public Void visit(SkipStmt n, Void arg) {
		printer.println("skip");
		return null;
	}
	
	@Override
	public Void visit(AssumeStmt n, Void arg) {
		printer.println(":@@@ " + n.hypothesis.accept(new PredPrinter(), arg));
		return null;
	}
	
	@Override
	public Void visit(AssertStmt n, Void arg) {
		printer.println(":!!! " + n.query.accept(new PredPrinter(), arg));
		return null;
	}
	
	@Override
	public Void visit(LocalDefStmt n, Void arg) {
		printer.print("local " + n.left.accept(new ExpPrinter(), arg));
		printer.println(" <- " + n.right.accept(new ExpPrinter(), arg));
		return null;
	}
	
	private void printBlockOrStmt(Stmt n) {
		if(n instanceof BlockStmt) {
			printer.println("{");
			printer.indent();
			
			((BlockStmt) n).stmt.accept(this, null);
			
			printer.unindent();
			printer.println("}");
		} else {
			printer.indent();
			n.accept(this, null);
			printer.unindent();
		}
	}
	
}

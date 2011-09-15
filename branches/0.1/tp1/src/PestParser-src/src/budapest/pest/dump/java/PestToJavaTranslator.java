package budapest.pest.dump.java;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import budapest.pest.ast.exp.VarIntExp;
import budapest.pest.ast.params.ConcreteParam;
import budapest.pest.ast.params.FormalParam;
import budapest.pest.ast.params.IntConcreteParam;
import budapest.pest.ast.pred.Pred;
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

public final class PestToJavaTranslator extends PestTranslator {

	public static final String PRE_PREFIX = "pre_"; 
	
	private final Set<String> vars = new HashSet<String>();
	
	@Override
	public Void visit(Program n, Void arg) {
		Procedure main = n.getMain();
		
//		printer.println("package jasvaoutput;");
//		printer.println();
		
		printer.println("import java.io.*;");
		printer.println("import java.util.*;");
		printer.println();
		
		// supress warnings
		printer.println("public final @SuppressWarnings(\"all\") class " + 
				toJavaName(main.name) + " {");
		printer.indent();
		printer.println();
		
		printer.println("public static void main(String[] args) {");
		printer.indent();
		printer.println("BufferedReader in = " +
		"new BufferedReader(new InputStreamReader(System.in));");
		printer.println();
		
		printer.println("try {");
		printer.indent();
		
		printer.println("for(;;) {");
		printer.indent();
		// list for reading arrays
		printer.println("List<Integer> l = new ArrayList<Integer>();");
		
		for(FormalParam par : main.params) {
			printer.println("System.out.print(\"Enter value for '" 
					+ par + "': \");");
			switch(par.type) {
			case INT:
				printer.println("Integer[] " + par + // array of length 1
						" = { Integer.parseInt(in.readLine()) };"); 
				vars.add(par.name);
				break;
			}
		}
		
		printer.print(main.name + "(");
		
		for (Iterator<FormalParam> i = main.params.iterator(); i.hasNext();) {
            FormalParam p = i.next();
            printer.print(p.name);
            if (i.hasNext()) {
                printer.print(", ");
            }
        }
		
		printer.println(");");
		
		for(FormalParam par : main.params) {
			printer.println("System.out.print(\"Out value of '"
					+ par + "': \");");
			switch(par.type) {
			case INT:
				// print int
				printer.println("System.out.println(Integer.toString(" + par + "[0]));");
				break;
			}
		}
		printer.println("System.out.println();");
		printer.println("System.out.println(\"Execution ended with no problems.\");");
		printer.println("System.out.println();");
		
		printer.unindent();
		printer.println("}"); // end of infinite for
		
		printer.unindent();
		printer.println("} catch(Throwable e) { ");
		printer.indent();
		
		printer.println("System.err.println(e.getMessage());");
		printer.println("System.exit(-1);");
		
		printer.unindent();
		printer.println("}"); // end of try/catch
		
		printer.unindent();
		printer.println("}"); // end of main method
		printer.println();
		
		// print all methods
		for(Procedure proc : n.procs) {
			proc.accept(this, arg);
			printer.println();
		}
		
		printer.println();
		printer.unindent();
		printer.println("}"); // end of class
		
		return null;
	}
	
	@Override
	public Void visit(Procedure n, Void arg) {
		
		printer.print("private static final void " + n.name + "(");
		
		for (Iterator<FormalParam> i = n.params.iterator(); i.hasNext();) {
            FormalParam p = i.next();
            
            switch(p.type) {
            case INT:
            	printer.print("Integer[] "); 
            	// we use arrays of length 1 to represent integers 
            	// its the only way to have by-ref semantics in parameters
            	break;
            }
            printer.print(p.name);
            
            if (i.hasNext()) {
                printer.print(", ");
            }
        }
		
		printer.println(") {");
		printer.indent();
		
		for(FormalParam par : n.params) {
			printer.println("Integer[] " + PRE_PREFIX + par.name + 
					" = new Integer[" + par.name + ".length];");
			printer.println("System.arraycopy(" + par.name + ", 0, " +
					PRE_PREFIX + par.name + ", 0, " + par.name + ".length);");
		}
		
		addAssert(n.pre);
		
		n.stmt.accept(this, arg);
		
		addAssert(n.post);
		
		printer.unindent();
		printer.println("}");
		printer.println();
		
		return null;
	}
	
	@Override
	public Void visit(CallStmt n, Void arg) {
		printer.print(n.procName + "(");
		for (Iterator<ConcreteParam> i = n.params.iterator(); i.hasNext();) {
            ConcreteParam p = i.next();
            if(p instanceof IntConcreteParam) {
            
            	// symbolic int param
            	if(((IntConcreteParam) p).exp instanceof VarIntExp)
            		printer.print(((VarIntExp) ((IntConcreteParam) p).exp).name);
            	else // concrete int param
            		printer.print("new Integer[] { " + 
            				((IntConcreteParam) p).exp.accept(new ExpToJavaTranslator(), arg) + " }");
            }
            
            if (i.hasNext()) {
                printer.print(", ");
            }
        }
		printer.println(");");
		
		return null;
	}
	
	@Override
	public Void visit(AssignStmt n, Void arg) {
		printer.println(n.left.name + "[0] = " + 
				n.right.accept(new ExpToJavaTranslator(), null) + ";");
		return null;
	}
	
	@Override
	public Void visit(AssumeStmt n, Void arg) {
		addAssert(n.hypothesis);
		return null;
	}
	
	@Override
	public Void visit(AssertStmt n, Void arg) {
		addAssert(n.query);
		return null;
	}
	
	@Override
	public Void visit(IfStmt n, Void arg) {
		printer.println("if(" + n.condition.accept(new ExpToJavaTranslator(), arg)
			+ " != 0)");
		printBlockOrStmt(n.thenS);
		if(!(n.elseS instanceof SkipStmt)) {
			printer.println("else");
			printBlockOrStmt(n.elseS);
		}
		return null;
	}
	
	@Override
	public Void visit(LocalDefStmt n, Void arg) {
		printer.println("Integer[] " + n.left.name + " = { " + // length 1
				n.right.accept(new ExpToJavaTranslator(), arg) + " };");
		vars.add(n.left.name);
		return null;
	}
	
	@Override
	public Void visit(BlockStmt n, Void arg) {
		printBlockOrStmt(n);
		return null;
	}
	
	@Override
	public Void visit(LoopStmt n, Void arg) {
		addAssert(n.invariant);
		
		printer.println("{");
		printer.indent();

		// variant term
		String varBef = "varBef_" + n.line + "_" + n.column;
		String varNow = "varNow_" + n.line + "_" + n.column;
		printer.println("Integer " + varBef + " = Integer.MAX_VALUE;");
		
		printer.println("while(" + 
				n.condition.accept(new ExpToJavaTranslator(), arg) + " != 0) {");
		printer.indent();
		
		// check variant > 0
		printer.println("if(" + varBef + " <= 0)");
		printer.indent();
		printer.println("throw new IllegalStateException(\"Variant evaluates to \" + " + varBef + " + \" (<= 0) inside loop body\");");
		printer.unindent();
		
		addAssert(n.invariant);
		
		printBlockOrStmt(n.body);
		
		// check variant decreases
		printer.println("Integer " + varNow + " = " + n.variant.accept(new TrmToJavaTranslator(), null) + ";");
		printer.println("if(" + varBef + " <= " + varNow + ")");
		printer.indent();
		printer.println("throw new IllegalStateException(\"Loop variant does not decrease " +
				"after an execution. Previous value: \" + " + varBef + "+ \". Current value: \" + " + varNow + ");");
		printer.unindent();
		printer.println(varBef + " = " + varNow + ";");
		
		printer.unindent();
		printer.println("}"); // end of while block
		
		printer.unindent();
		printer.println("}"); // end of loop block
		
		addAssert(n.invariant);
		
		return null;
	}
	
	@Override
	public Void visit(SkipStmt n, Void arg) {
		printer.println(";");
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
	
	private void addAssert(Pred p) {
		// new scope to reuse boolean var names
		printer.println("{");
		printer.indent();
		
		p.accept(new PredToJavaTranslator(printer), "ret");
		printer.println("if(!ret)" +
				"{ throw new IllegalStateException(\"Predicate " + p + 
				" is not true.\"); }");
		
		printer.unindent();
		printer.println("}");
	}
	
	public static String toJavaName(String procName) {
		return Character.toUpperCase(procName.charAt(0)) + procName.substring(1);
	}
	
}

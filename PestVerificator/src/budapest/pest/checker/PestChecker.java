package budapest.pest.checker;

import java.util.Set;

import budapest.pest.ast.exp.VarIntExp;
import budapest.pest.ast.params.ConcreteParam;
import budapest.pest.ast.params.FormalParam;
import budapest.pest.ast.params.IntConcreteParam;
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
import budapest.pest.ast.visitor.PestVisitor;
import budapest.pest.checker.callgraph.CallGraph;
import budapest.pest.checker.symtab.ProcsInfo;
import budapest.pest.checker.symtab.ScopeSymbolTable;
import budapest.pest.checker.symtab.SymbolTable;
import budapest.pest.checker.symtab.SymbolTable.SymbolInfo.Type;
import budapest.util.report.ReportSet;

public final class PestChecker extends PestVisitor<Void, SymbolTable> {

	private final CallGraph cg = new CallGraph();
	private String analyzedProc;
	
	public final ReportSet reports;
	public final ProcsInfo pi;	

	public PestChecker(final ReportSet reports, final ProcsInfo procs) {
		this.reports = reports;
		this.pi = procs;
	}

	@Override
	public Void visit(Program n, SymbolTable arg) {
		// check errors in every procedure
		for(Procedure proc : n.procs) {
			analyzedProc = proc.name;
			proc.accept(this, null);
		}
		
		pi.setCallGraph(cg);
		
		return null;
	}
	
	@Override
	public Void visit(Procedure n, SymbolTable notUsed) {
		
		SymbolTable top = new SymbolTable();
		
		for(FormalParam p : n.params) {
			if(top.isLocal(p.name))
				reports.addErrorReport("Duplicate parameter name " + p.name, n);
			
			switch(p.type) {
			case INT:
				top.add(p.name, Type.INT);
				break;
			}
		}
		
		// check pre (if present)
		if(n.pre != null)
			n.pre.accept(new PredChecker(reports), top);
		
		// check post (if present)
		if(n.post != null)
			n.post.accept(new PredChecker(reports), top);
		
		// check that touches is subset of parameters
		if(!n.params.containsAll(n.touches))
			reports.addErrorReport("Procedure " + n.name + " touches"
					+ " variables that are not parameters", n);
		
		// check that the body does not modify any var that is not touched
		Set<String> modParams = n.stmt.modifiedVars(pi);
		modParams.retainAll(n.paramNames());
		for(FormalParam t : n.touches)
			modParams.remove(t.name);
		for(String var : modParams) 
			reports.addErrorReport("Procedure " + n.name + " touches"
					+ " variable " + var + " without declaring it", n);
		
		n.stmt.accept(this, top);
		
		cg.addNode(n.name);
		
		return null;
	}
	
	@Override
	public Void visit(CallStmt n, SymbolTable st) {
		
		// check procedure exists
		if(!pi.knows(n.procName)) {
			reports.addErrorReport("Unknown procedure " + n.procName, n);
		} else {
			
			// check parameters are well formed
			for(ConcreteParam p : n.params) {
				if(p instanceof IntConcreteParam) {
					((IntConcreteParam) p).exp.accept(new ExpChecker(reports), st);
				} 
			}
			
			// check symbolic parameters are all different
			for(int i = 0; i < n.params.size(); i++) {
				for(int j = i+1; j < n.params.size(); j++) {
					ConcreteParam p1 = n.params.get(i);
					ConcreteParam p2 = n.params.get(j);
					
					if(p1 instanceof IntConcreteParam &&
							p2 instanceof IntConcreteParam)
						if(((IntConcreteParam) p1).exp instanceof VarIntExp &&
								((IntConcreteParam) p2).exp instanceof VarIntExp)
							if(((VarIntExp) ((IntConcreteParam) p1).exp).name.
									equals(((VarIntExp) ((IntConcreteParam) p2).exp).name))
								reports.addErrorReport("Repeated integer parameter " +
										((VarIntExp) ((IntConcreteParam) p1).exp).name + 
										" generates aliasing", n);
				}
			}
			
			// check parameter types are compatible with expected types
			String paramsOk = pi.compatibleCall(n.procName, n.params);
			if(paramsOk != null) 
				reports.addErrorReport(paramsOk, n);

			// check there are no cycles in the call graph
			if(!cg.hasCycle()) {
				cg.add(analyzedProc, n.procName);
				if(cg.hasCycle())
					reports.addErrorReport("Calling " + n.procName + 
							" generates recursiveness", n);
			}
		}
		
		return null;
	}
	
	@Override
	public Void visit(LocalDefStmt n, SymbolTable st) {
		if(st.knows(n.left.name))
			reports.addErrorReport("Duplicate name " + n.left.name, n);
		
		n.right.accept(new ExpChecker(reports), st);
		
		st.add(n.left.name, Type.INT);
		
		return null;
	}

	@Override
	public Void visit(LoopStmt n, SymbolTable st) {
		n.condition.accept(new ExpChecker(reports), st);
		
		// check invariant
		n.invariant.accept(new PredChecker(reports), st);
		
		// check variant
		n.variant.accept(new TrmChecker(reports), st);
		
		n.body.accept(this, st);
		
		return null;
	}
	
	@Override
	public Void visit(IfStmt n, SymbolTable st) {
		n.condition.accept(new ExpChecker(reports), st);
		
		n.thenS.accept(this, st);
		n.elseS.accept(this, st);
		
		return null;
	}
	
	@Override
	public Void visit(AssumeStmt n, SymbolTable st) {
		n.hypothesis.accept(new PredChecker(reports), st);
		return null;
	}
	
	@Override
	public Void visit(AssertStmt n, SymbolTable st) {
		n.query.accept(new PredChecker(reports), st);
		return null;
	}
	
	@Override
	public Void visit(AssignStmt n, SymbolTable st) {
		n.left.accept(new ExpChecker(reports), st);
		n.right.accept(new ExpChecker(reports), st);
		
		return null;
	}
	
	@Override
	public Void visit(BlockStmt n, SymbolTable st) {
		n.stmt.accept(this, new ScopeSymbolTable(st));
		
		return null;
	}
	
}

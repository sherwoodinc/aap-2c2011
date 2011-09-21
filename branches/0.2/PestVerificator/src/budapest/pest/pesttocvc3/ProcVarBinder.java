package budapest.pest.pesttocvc3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import budapest.pest.ast.params.IntConcreteParam;
import budapest.pest.ast.pred.trm.Trm;
import budapest.pest.ast.proc.Procedure;
import budapest.pest.ast.proc.Program;
import budapest.pest.ast.stmt.CallStmt;
import budapest.pest.ast.stmt.Stmt;

public class ProcVarBinder {
	public Procedure callee;
	public Program program;
	public CallStmt callStmt;
	public Set<String> touchedVars;
	public Map<String, Trm> bindings;
	
	public ProcVarBinder(CallStmt call, Program prog) {
		program = prog;		
		callStmt = call;
		for (Procedure p : prog.procs) {
			if (p.name.equals(call.procName)) {
				callee = p;
				break;
			}
		}
		
		// Create parameter -> value bindings map
		bindings = new HashMap<String, Trm>();
		touchedVars = new HashSet<String>();
		for (int i=0; i < call.params.size(); ++i)
		{
			// Casting the ConcreteParameter to IntConcreteParameter...
			bindings.put(callee.params.get(i).name, ((IntConcreteParam)call.params.get(i)).exp.accept(new ExpToTrmTranslator(), null));
			if (callee.touches.contains(callee.params.get(i)))
			{
				// This var will be overwritten: assume it's a variable name
				touchedVars.add(callStmt.params.get(i).toString());
			}
		}			
	}
}

package budapest.pest.checker.symtab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import budapest.pest.ast.params.ConcreteParam;
import budapest.pest.ast.params.FormalParam;
import budapest.pest.ast.params.IntConcreteParam;
import budapest.pest.ast.pred.Pred;
import budapest.pest.ast.proc.Procedure;
import budapest.pest.checker.callgraph.CallGraph;

public final class ProcsInfo {

	private final Map<String, Procedure> procs = new HashMap<String, Procedure>();
	private CallGraph cg;
	
	public void put(Procedure p) {
		if(knows(p.name))
			throw new IllegalArgumentException("Redefinition of procedure " + p.name);
		
		procs.put(p.name, p);
	}
	
	public boolean knows(String name) {
		return procs.containsKey(name); 
	}
	
	public List<FormalParam> touches(String name) {
		if(!knows(name))
			throw new IllegalArgumentException("Unknown procedure " + name);
		
		return procs.get(name).touches;
	}
	
	public boolean touchesParam(String name, String param) {
		if(!knows(name))
			throw new IllegalArgumentException("Unknown procedure " + name);
		
		for(FormalParam fp : procs.get(name).touches)
			if(fp.name.equals(param))
				return true;
		
		return false;
	}
	
	public List<FormalParam> formalParams(String name) {
		if(!knows(name))
			throw new IllegalArgumentException("Unknown procedure " + name);
		
		return procs.get(name).params;
	}
	
	public Pred precondition(String name) {
		if(!knows(name))
			throw new IllegalArgumentException("Unknown procedure " + name);
		
		return procs.get(name).pre;
	}

	public Pred postcondition(String name) {
		if(!knows(name))
			throw new IllegalArgumentException("Unknown procedure " + name);
		
		return procs.get(name).post;
	}

	public String compatibleCall(String name, List<ConcreteParam> params) {
		if(!knows(name))
			throw new IllegalArgumentException("Unknown procedure " + name);
		
		List<FormalParam> formalParams = procs.get(name).params;
		
		if(formalParams.size() != params.size())
			return "Illegal amount of parameters calling " + name;
		
		for(int i = 0; i < params.size(); i++) {
			switch(formalParams.get(i).type) {
			case INT:
				if(!(params.get(i) instanceof IntConcreteParam))
					return "Expecting an integer parameter " + formalParams.get(i).name + 
							" calling " + name;
				break;
			}
		}
		
		return null;
	}

	public void setCallGraph(CallGraph cg) {
		this.cg = cg;
	}
	
	public CallGraph getCallGraph() {
		return cg;
	}

}

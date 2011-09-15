package budapest.pest.pesttocvc3;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import budapest.pest.ast.pred.Pred;
import budapest.pest.predtocvc3.PredVarReplacer;

public class PestVarContext {

	public PestVarContext(PestVarContext parentContext, TreeMap<String, String> beginContext) {
		InitVariables = new TreeMap<String, String>(beginContext);
		valuations = new TreeMap<String, String>();
		parent = parentContext;
	}

	public PestVarContext() {
		InitVariables = new TreeMap<String, String>();
		valuations = new TreeMap<String, String>();
		parent = null;
	}
	
	public PestVarContext(PestVarContext context) {
		InitVariables = new TreeMap<String, String>(context.InitVariables);
		valuations = new TreeMap<String, String>(context.valuations);
		parent = context.parent;
	}
	
	// Obtiene el último nombre asignado a una var
	public String getVarAssignment(String varName) throws Exception {
		if (valuations.containsKey(varName)) {
			return valuations.get(varName);
		} else if (parent != null) {
			return parent.getVarAssignment(varName);
		} else {
			throw new Exception("Variable inexistente");
		}
	}

    // Genera un nombre nuevo y lo devuelve
    public String setVarAssignment(String varName) {
            String newSnapshot = varName + String.valueOf(GlobalID);
            valuations.put(varName, newSnapshot);
            GlobalID++;
            return newSnapshot;
    }

	// Reemplaza las ocurrencias de cada variable por su
	// renombre del context, además usando contexts padres
	public Pred translate(Pred input) {
		Map<String, String> vals = allValuations();
		PredVarReplacer r = new PredVarReplacer();
		for (Entry<String, String> pair : vals.entrySet()) {
			r.visit(input, new VarReplacement(pair.getKey(), pair.getValue()));
		}
		return input;
	}

	private TreeMap<String, String> allValuations() {
		if (parent == null) {
			return new TreeMap<String, String>(valuations);
		} else {
			TreeMap<String, String> ret = parent.allValuations();
			ret.putAll(valuations);
			return ret;
		}
	}

	private PestVarContext parent = null;
	private TreeMap<String, String> InitVariables;
	private TreeMap<String, String> valuations;

	private static int GlobalID = 0;
}

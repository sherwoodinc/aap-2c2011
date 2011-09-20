package budapest.pest.pesttocvc3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class PestVarContext {

	private static TreeMap<String, Integer> varMaxVersions = new TreeMap<String, Integer>();
	
	private PestVarContext parentContext;
	private TreeMap<String, Integer> varVersions;
		
	public PestVarContext(List<String> vars) {
		varVersions = new TreeMap<String, Integer>();
		parentContext = null;
		
		for(String var : vars)
			addNewVersionOf(var);
	}
	
	public PestVarContext(PestVarContext parentContext) {
		this.parentContext = parentContext;
		varVersions = new TreeMap<String, Integer>();
	}
		
	public String addNewInstanceOf(String var) {
		return var + "_" + addNewVersionOf(var);
	}
	
	public int addNewVersionOf(String var) {
		int newVersion = getNewVersionOf(var);
		varVersions.put(var, newVersion);
		return newVersion;
	}
	
	public String getInstanceOf(String var) {
		return var + "_" + getVersionOf(var);
	}
	
	public List<String> getVarInstances() {
		List<String> result = new ArrayList<String>();
		for(String var : varVersions.keySet())
			result.add(getInstanceOf(var));
		return result;
	}
	
	public Set<String> getModifiedVars() {
		return varVersions.keySet();
	}
	
	public int getVersionOf(String var) {
		if(varVersions.containsKey(var))
			return varVersions.get(var).intValue();
		
		if(parentContext != null)
			return parentContext.getVersionOf(var);
		
		return -1;
	}
	
	private static int getNewVersionOf(String var) {
		if(varMaxVersions.containsKey(var)) {
			int newMaxVersion = varMaxVersions.get(var).intValue() + 1;
			varMaxVersions.put(var, new Integer(newMaxVersion));
			return newMaxVersion;
		}
		varMaxVersions.put(var, 0);
		return 0;
	}
}

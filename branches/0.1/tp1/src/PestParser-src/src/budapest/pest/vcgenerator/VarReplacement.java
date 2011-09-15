package budapest.pest.vcgenerator;

public class VarReplacement {
	
	public final String oldVar;
	public final String newVar;
	
	public VarReplacement(final String oldVar, final String newVar){
		this.oldVar = oldVar;
		this.newVar = newVar;
	}
	
	public String execute(String var){
		return var.equals(oldVar) ? newVar : var;
	}
}

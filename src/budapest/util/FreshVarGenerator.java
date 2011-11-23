package budapest.util;

public class FreshVarGenerator {

	private static final String name = "u__";
	private static int nextVar = 0;
	
	public static String freshVar() {
		String var = name + nextVar;
		nextVar++;
		return var;
	}
	
}

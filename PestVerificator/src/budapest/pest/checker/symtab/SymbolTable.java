package budapest.pest.checker.symtab;

import java.util.HashMap;
import java.util.Map;

import budapest.pest.checker.symtab.SymbolTable.SymbolInfo.Type;

public class SymbolTable {

	private final Map<String,SymbolInfo> locals = new HashMap<String, SymbolInfo>();
	
	public void add(String name, Type type) {
		locals.put(name, new SymbolInfo(type));
	}
	
	public boolean knows(String name) {
		return isLocal(name); 
	}
	
	public boolean isLocal(String name) {
		return locals.containsKey(name);
	}
	
	public Type type(String name) {
		return locals.get(name).type;
	}

	public static final class SymbolInfo {
		
		public static enum Type { 
			INT, ARRAY, PRED 
		}
		
		public final Type type;

		public SymbolInfo(final Type type) {
			this.type = type;
		}
		
	}
	
}

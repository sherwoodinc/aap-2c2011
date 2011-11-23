package budapest.pest.checker.symtab;

import budapest.pest.checker.symtab.SymbolTable.SymbolInfo.Type;

public final class ScopeSymbolTable extends SymbolTable {

	private final SymbolTable parent;
	
	public ScopeSymbolTable(final SymbolTable parent) {
		this.parent = parent;
	}
	
	@Override
	public boolean knows(String name) {
		return isLocal(name) || parent.knows(name);
	}
	
	@Override
	public Type type(String name) {
		if(isLocal(name))
			return super.type(name);
		else
			return parent.type(name);
	}
	
}

package budapest.pest.ast.params;

import budapest.pest.ast.Type;

public final class FormalParam {

	public final String name;
	public final Type type;

	public FormalParam(final String name, final Type type) {
		this.name = name;
		this.type = type;
	}
	
	@Override
	public String toString() {
		switch(type) {
		case INT:
			return name;
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof FormalParam 
			&& ((FormalParam) obj).name.equals(name)
			&& ((FormalParam) obj).type.equals(type);
	}
	
}

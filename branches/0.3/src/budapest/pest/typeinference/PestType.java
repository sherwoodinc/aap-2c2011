package budapest.pest.typeinference;

public abstract class PestType {
	
	public abstract String getTypeName();
	
	public boolean equals(PestType otherType)
	{
		return getTypeName().equals(otherType.getTypeName());
	}
}

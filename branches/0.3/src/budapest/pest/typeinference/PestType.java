package budapest.pest.typeinference;

public abstract class PestType {
	
	public abstract String getTypeName();
	
	public boolean isSameThan(PestType other)
	{
		return this.getClass().equals(other.getClass());
	}
	
	public boolean isSuperTypeOf(PestType other)
	{
		return this.getClass().isAssignableFrom(other.getClass());
	}
	
	public PestType minimumType(PestType other)
	{
		if(isSameThan(other) || other.isSuperTypeOf(this))
		{
			return this;
		}
		
		if(isSuperTypeOf(other))
		{
			return other;
		}
		
		return null;
	}
	
	public static PestType Top()
	{
		return new PestTopType();
	}

	public static PestType Int()
	{
		return new PestIntType();
	}
	
	public static PestType String()
	{
		return new PestStringType();
	}
}

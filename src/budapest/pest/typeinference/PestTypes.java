package budapest.pest.typeinference;

public class PestTypes 
{
	public static PestType Int()
	{
		return new PestIntType();
	}
	
	public static PestType String()
	{
		return new PestStringType();
	}
	
	public static PestType Bool()
	{
		return new PestBoolType();
	}
}

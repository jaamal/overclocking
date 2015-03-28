package services.statistics;

public class StatisticProviderException extends RuntimeException 
{
	private static final long serialVersionUID = -200441573093621214L;
	
	public StatisticProviderException(String message, Exception e) {
		super(message, e);
	}

}

package services.statistics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import models.CompressionAlgorithmType;
import models.statistics.StatisticType;
import services.statistics.mapping.IStatisticsMapping;

public class PlotParameters implements IPlotParameters
{
	private List<StatisticElement> statsParameters = null;
	private final IStatisticsMapping statisticsMapping;
	private final StatisticType xType;
	private final StatisticType yType;
	private final CompressionAlgorithmType[] algorithmTypes;
	
	//TODO: the feature of passing custom algorithmTypes is not used but requires some logic
	public PlotParameters(IStatisticsMapping statisticsMapping, StatisticType xType, StatisticType yType, CompressionAlgorithmType[] algorithmTypes) {
		this.statisticsMapping = statisticsMapping;
		this.xType = xType;
		this.yType = yType;
		this.algorithmTypes = algorithmTypes;
	}
	
	@Override
	public int hashCode()
	{
		int result = xType.hashCode();
		result += yType.hashCode() * 347;
		
		List<StatisticElement> statElements = this.getStatElements();
		for (StatisticElement element : statElements) {
			result += element.hashCode() * 17;
		}
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (!(PlotParameters.class == obj.getClass()))
			return false;
		
		PlotParameters parameters = (PlotParameters) obj;
		if (this.xType != parameters.xType || this.yType != parameters.yType)
			return false;
		
		List<StatisticElement> ourElements = this.getStatElements();
		List<StatisticElement> theirElements = parameters.getStatElements();
		if (ourElements.size() != theirElements.size())
			return false;
		
		//TODO: may be it is enough to compare hashCodes of this and parameters?
		int size = ourElements.size();
		HashSet<Integer> classHashes = new HashSet<Integer>(size);
		for (int i = 0; i < size; i++) {
			if (!classHashes.contains(ourElements.get(i).hashCode()))
				classHashes.add(ourElements.get(i).hashCode());
			if (!classHashes.contains(theirElements.get(i).hashCode()))
				classHashes.add(theirElements.get(i).hashCode());
		}
		return classHashes.size() == size;
	}

	@Override
	public StatisticType getXType()
	{
		return xType;
	}

	@Override
	public StatisticType getYType()
	{
		return yType;
	}

	@Override
	public List<StatisticElement> getStatElements()
	{
		if (statsParameters == null){
			List<StatisticElement> xElements = statisticsMapping.getClasses(xType);
			List<StatisticElement> yElements = statisticsMapping.getClasses(yType);
			
			if (algorithmTypes.length == 0){
				ArrayList<StatisticElement> result = new ArrayList<StatisticElement>();
				for (StatisticElement xElement : xElements) {
					if (yElements.contains(xElement))
						result.add(xElement);
				}
				return result;
			}
			
			ArrayList<StatisticElement> result = new ArrayList<StatisticElement>();
			for (CompressionAlgorithmType algorithmType : algorithmTypes) {
				Class<?> statsClass = statisticsMapping.getAlgorithmClass(algorithmType);
				if (xElements.contains(new StatisticElement(statsClass, null)) && yElements.contains(new StatisticElement(statsClass, null)))
					result.add(new StatisticElement(statsClass, algorithmType));
			}
			statsParameters = result;
		}
		return statsParameters;
	}
}

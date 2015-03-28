package services.statistics;

import java.util.ArrayList;
import java.util.List;

import models.PlotCurve;
import models.PlotModel;
import models.statistics.StatisticType;
import services.statistics.scaling.ICompositeStatisticScaler;
import services.statistics.scaling.ScalingResult;
import factories.IPlotFactory;

public class PlotModelBuilder implements IPlotModelBuidler
{
	List<StatisticInfo> infos;
	List<Object[]> xValues;
	List<Object[]> yValues;
	private final StatisticType xType;
	private final StatisticType yType;
	private final IPlotFactory plotFacotry;
	private final ICompositeStatisticScaler compositeStatisticScaler;
	
	public PlotModelBuilder(IPlotFactory plotFacotry, ICompositeStatisticScaler compositeStatisticScaler, StatisticType xType, StatisticType yType) {
		this.plotFacotry = plotFacotry;
		this.compositeStatisticScaler = compositeStatisticScaler;
		this.xType = xType;
		this.yType = yType;
		xValues = new ArrayList<Object[]>();
		yValues = new ArrayList<Object[]>();
		infos = new ArrayList<StatisticInfo>();
	}
	
	//TODO: organize this code
	@Override
	public void append(StatisticInfo statisticInfo)
	{
		infos.add(statisticInfo);
		xValues.add(statisticInfo.xValues);
		yValues.add(statisticInfo.yValues);
	}

	@Override
	public PlotModel build()
	{
		if (xValues.size() == 0 || yValues.size() == 0)
			return plotFacotry.createEmpty(xType.unitType.name, yType.unitType.name);
		
		ScalingResult xScaledValues = compositeStatisticScaler.scale(xValues, xType);
		ScalingResult yScaledValues = compositeStatisticScaler.scale(yValues, yType);
		
		if (xScaledValues.values.size() != yScaledValues.values.size())
			throw new RuntimeException("Assert statistic arrays of different length.");
		int curvesNumber = xScaledValues.values.size();
		PlotCurve[] curves = new PlotCurve[curvesNumber];
		for (int i = 0; i < curvesNumber; i++) {
			int[][] points = new int[xScaledValues.values.get(i).length][2];
			for (int j = 0; j < xScaledValues.values.get(i).length; j++) {
				points[j][0] = xScaledValues.values.get(i)[j];
				points[j][1] = yScaledValues.values.get(i)[j];
			}
			curves[i] = new PlotCurve(points, infos.get(i).algorithmType);
		}
		return plotFacotry.create(curves, xScaledValues.unit, yScaledValues.unit);
	}

}

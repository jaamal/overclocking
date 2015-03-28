package factories;

import models.PlotCurve;
import models.PlotModel;

public class PlotFactory implements IPlotFactory {

	@Override
	public PlotModel create(PlotCurve[] curves, String xLabel, String yLabel)
	{
		return new PlotModel(curves, xLabel, yLabel);
	}

	@Override
	public PlotModel createEmpty(String xLabel, String yLabel)
	{
		return new PlotModel(new PlotCurve[0], xLabel, yLabel);
	}
}

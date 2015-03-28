package factories;

import models.PlotCurve;
import models.PlotModel;

public interface IPlotFactory {
    PlotModel create(PlotCurve[] curves, String xLabel, String yLabel);
    PlotModel createEmpty(String xLabel, String yLabel);
}

package models;

public class PlotModel
{
	public final PlotCurve[] curves;
	public final String xLabel;
	public final String yLabel;
	
	public PlotModel(PlotCurve[] curves, String xLabel, String yLabel) {
		this.curves = curves != null && curves.length > 0 ? curves : new PlotCurve[0];
		this.xLabel = xLabel;
		this.yLabel = yLabel;
	}

	@Override
	public int hashCode()
	{
		int result = 0;
		for (int i = 0; i < this.curves.length; i++)
			result += 31 * curves[i].hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
        	return false;
        if (!(PlotModel.class == obj.getClass()))
        	return false;
        PlotModel model = (PlotModel) obj;
        //TODO: may be curves link is null 
        if (this.curves.length != model.curves.length)
        	return false;
        for (int i = 0; i < this.curves.length; i++) {
        	if (!this.curves[i].equals(model.curves[i]))
        		return false;
        }
        return true;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < this.curves.length; i++) {
			builder.append(this.curves[i].toString() + ", ");
		}
		builder.append("]");
		return builder.toString();
	}
}

package models;

import models.CompressionAlgorithmType;

public class PlotCurve
{
	public final int[][] points;
	public final String caption;
	public final String color;
	public final CompressionAlgorithmType type;
	
	public PlotCurve(int[][] points, CompressionAlgorithmType algorithmType) {
		this.points = points != null ? points : new int[0][0];
		this.caption = algorithmType.caption;
		this.color = algorithmType.color;
		this.type = algorithmType;
	}

	@Override
	public int hashCode()
	{
		int result = type.hashCode();
		for (int i = 0; i < this.points.length; i++)
			result += 31 * points[i].hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
        	return false;
        if (!(PlotCurve.class == obj.getClass()))
        	return false;
        PlotCurve curve = (PlotCurve) obj;
        if (this.type != curve.type)
        	return false;
        if (this.points.length != curve.points.length)
        	return false;
        for (int i = 0; i < this.points.length; i++) {
        	for (int j = 0; j < this.points[i].length; j++)
        		if (this.points[i][j] != curve.points[i][j])
        			return false;
        }
        return true;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < this.points.length; i++)
			builder.append(this.points[i][0] + ", " + this.points[i][1]);
		builder.append("]");
		return builder.toString();
	}
}

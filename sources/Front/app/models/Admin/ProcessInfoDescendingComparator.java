package models.Admin;

import java.util.Comparator;
import com.jezhumble.javasysmon.ProcessInfo;

public class ProcessInfoDescendingComparator implements Comparator<ProcessInfo>
{
	@Override
	public int compare(ProcessInfo o1, ProcessInfo o2)
	{
		return (int) (o2.getTotalBytes() -o1.getTotalBytes());
	}
}

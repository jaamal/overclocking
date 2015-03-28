package dataContracts;

import java.util.UUID;

//NOTE: don't rename this factory since it is critical to running unit tests !!! :)
public interface IIDFactory
{
	UUID getDeterministicID(String input);
	UUID create();
	UUID getEmpty();
}

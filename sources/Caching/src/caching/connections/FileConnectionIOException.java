package caching.connections;

import java.io.File;

public class FileConnectionIOException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public FileConnectionIOException(File file, Exception source) {
		super("Fail to work with file '" + file.getName() + "'", source);
	}
}

package handlers;

import java.util.List;
import models.FileInfo;
import database.DBException;
import database.IDBContext;

public class FileInfoHandler implements IFileInfoHandler {
    private IDBContext dbContext;

    public FileInfoHandler(IDBContext dbContext) {
        this.dbContext = dbContext;
    }

    @Override
    public List<FileInfo> find() throws DBException {
        return dbContext.select(FileInfo.class);
    }

    //TODO: refactor this query, non-optimal
    @Override
    public FileInfo read(String id) throws DBException, FileInfoNotFoundException {
        List<FileInfo> fileInfos = find();
        for (FileInfo fileInfo : fileInfos) {
            if (fileInfo.getId().equals(id))
                return fileInfo;
        }
        throw new FileInfoNotFoundException(id);
    }
}

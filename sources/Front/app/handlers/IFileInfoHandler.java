package handlers;

import database.DBException;
import models.FileInfo;

import java.util.List;

public interface IFileInfoHandler {
    List<FileInfo> find() throws DBException;

    FileInfo read(String id) throws DBException, FileInfoNotFoundException;
}

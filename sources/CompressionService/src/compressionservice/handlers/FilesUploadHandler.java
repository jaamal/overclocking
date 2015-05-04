package compressionservice.handlers;

import httpservice.handlers.BaseHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import commons.files.IFile;
import commons.files.IFileManager;
import compressionservice.handlers.binding.Binder;
import compressionservice.upload.IFileUploader;

import dataContracts.ContentType;
import dataContracts.files.FileType;

public class FilesUploadHandler extends BaseHandler
{
    private IFileManager fileManager;
    private IFileUploader fileUploader;

    public FilesUploadHandler(
            IFileManager fileManager,
            IFileUploader fileUploader) {
        this.fileManager = fileManager;
        this.fileUploader = fileUploader;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getParameter("path");
        IFile file = fileManager.getFile(path);
        FileType fileType = Binder.getEnum(request, FileType.class, "fileType");
        ContentType contentType = Binder.getEnum(request, ContentType.class, "contentType");
        String fileId = fileUploader.upload(file, fileType, contentType);
        respondText(baseRequest, response, String.format("File with id %s from %s of type %s with content type %s successfully exported.", 
                fileId, path, fileType.name(), contentType.name()));
    }

    @Override
    public String getRoute() {
        return "/files/upload";
    }
}

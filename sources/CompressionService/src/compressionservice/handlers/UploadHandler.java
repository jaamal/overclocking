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

public class UploadHandler extends BaseHandler {
    private IFileUploader fileUploader;
    private IFileManager fileManager;

	public UploadHandler(IFileUploader importer, IFileManager fileManager){
		this.fileUploader = importer;
        this.fileManager = fileManager;
	}
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	    String name = request.getParameter("name");
	    IFile file = fileManager.getFile(name);
	    FileType fileType = Binder.getEnum(request, FileType.class, "fileType");
	    ContentType contentType = Binder.getEnum(request, ContentType.class, "contentType");
	    fileUploader.upload(file, fileType, contentType);
	    respondText(baseRequest, response, String.format("File %s of type %s with content type %s successfully exported.", name, fileType.name(), contentType.name()));
	}

	@Override
	public String getRoute() {
		return "/upload";
	}

}

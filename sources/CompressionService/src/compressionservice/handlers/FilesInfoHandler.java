package compressionservice.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import storage.filesRepository.IFilesRepository;
import compressionservice.handlers.binding.Binder;
import compressionservice.handlers.models.FileModel;
import dataContracts.files.FileMetadata;
import httpservice.handlers.BaseHandler;

public class FilesInfoHandler extends BaseHandler {

    private IFilesRepository filesRepository;

    public FilesInfoHandler(IFilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int from = Binder.getInt(request, "from", 0);
        int count = Binder.getInt(request, "count", 10);
        
        List<String> fileIds = filesRepository.getFileIds(from, count);
        FileMetadata[] fileMetas = filesRepository.getMeta(fileIds);
        List<FileModel> fileModels = new ArrayList<FileModel>();
        for (FileMetadata fielMeta : fileMetas) {
            fileModels.add(FileModel.create(fielMeta));
        }
        respondJson(baseRequest, response, fileModels);
    }

    @Override
    public String getRoute() {
        return "/files/info";
    }
}

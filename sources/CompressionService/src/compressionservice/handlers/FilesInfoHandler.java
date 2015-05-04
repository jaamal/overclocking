package compressionservice.handlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import storage.filesRepository.IFilesRepository;
import compressionservice.handlers.binding.Binder;
import compressionservice.handlers.models.FileModel;
import dataContracts.ContentType;
import dataContracts.files.FileBatch;
import dataContracts.files.FileMetadata;
import httpservice.handlers.BaseHandler;
import httpservice.handlers.HttpContentTypes;

public class FilesInfoHandler extends BaseHandler {

    private final static long maxFileSize = 100 *1024 * 1024;
    
    private IFilesRepository filesRepository;

    public FilesInfoHandler(IFilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] querySplits = request.getRequestURI().split("/");
        String candidateFileIdStr = querySplits[querySplits.length-1];
        
        try {
            UUID fileId = UUID.fromString(candidateFileIdStr);
            downloadFile(baseRequest, fileId, response);
        }
        catch (IllegalArgumentException e) {
            selectFileInfos(baseRequest, Binder.getInt(request, "from", 0), Binder.getInt(request, "count", 10), response);
        }
    }
    
    private void selectFileInfos(Request baseRequest, int from, int count, HttpServletResponse response) throws IOException {
        List<String> fileIds = filesRepository.getFileIds(from, count);
        FileMetadata[] fileMetas = filesRepository.getMeta(fileIds);
        List<FileModel> fileModels = new ArrayList<FileModel>();
        for (FileMetadata fielMeta : fileMetas) {
            fileModels.add(FileModel.create(fielMeta));
        }
        respondJson(baseRequest, response, fileModels);
    }
    
    private void downloadFile(Request baseRequest, UUID fileId, HttpServletResponse response) throws IOException {
        FileMetadata fileMeta = filesRepository.getMeta(fileId.toString());
        if (fileMeta.getSize() < maxFileSize) {
            PrintWriter writer = response.getWriter();
            for (FileBatch batch : filesRepository.getFileIterator(fileId.toString())) {
                writer.print(new String(batch.batchData, "UTF-8"));
            }
            if (fileMeta.getContentType() == ContentType.GZip) {
                response.addHeader("Content-Encoding", "gzip");
            }
            respondFile(baseRequest, response, HttpContentTypes.TEXT, fileMeta.getName());
        }
        else {
            respondText(baseRequest, response, String.format("Unable to download file with id %s since its size greater than %sMb.", fileId, maxFileSize / 1024 / 1024));
        }
    }

    @Override
    public String getRoute() {
        return "/files/info";
    }
}

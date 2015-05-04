package compressionservice.handlers;

import httpservice.handlers.BaseHandler;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import storage.filesRepository.IFilesRepository;

import compressionservice.handlers.binding.Binder;
import compressionservice.handlers.models.FileModel;

import dataContracts.files.FileBatch;
import dataContracts.files.FileMetadata;

public class FilesInfoHandler extends BaseHandler
{
    private final static long maxFileSize = 100 * 1024 * 1024;

    private IFilesRepository filesRepository;

    public FilesInfoHandler(IFilesRepository filesRepository)
    {
        this.filesRepository = filesRepository;
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] querySplits = request.getRequestURI().split("/");
        String candidateFileIdStr = querySplits[querySplits.length - 1];

        try {
            UUID fileId = UUID.fromString(candidateFileIdStr);
            downloadFile(fileId, response);
        } catch (IllegalArgumentException e) {
            selectFileInfos(Binder.getInt(request, "from", 0), Binder.getInt(request, "count", 10), response);
        }
    }

    private void selectFileInfos(int from, int count, HttpServletResponse response) throws IOException {
        List<String> fileIds = filesRepository.getFileIds(from, count);
        FileMetadata[] fileMetas = filesRepository.getMeta(fileIds);
        List<FileModel> fileModels = new ArrayList<FileModel>();
        for (FileMetadata fielMeta : fileMetas) {
            fileModels.add(FileModel.create(fielMeta));
        }
        respondJson(response, fileModels);
    }

    private void downloadFile(UUID fileId, HttpServletResponse response) throws IOException {
        FileMetadata fileMeta = filesRepository.getMeta(fileId.toString());
        if (fileMeta.getSize() < maxFileSize) {
            // TODO: unscalable code
            switch (fileMeta.getContentType()) {
            case PlainText:
                respondFile(response, "text/plain", (int) fileMeta.getSize(), fileMeta.getName());
                
                try (BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream(), 1024);) {
                    for (FileBatch batch : filesRepository.getFileIterator(fileId.toString())) {
                        outputStream.write(batch.batchData);
                    }
                    outputStream.flush();
                }
                break;
            case GZip:
                respondFile(response, "application/x-gzip", (int) fileMeta.getSize(), fileMeta.getName());

                try (BufferedOutputStream bufferesOutputStream = new BufferedOutputStream(response.getOutputStream(), 1024); ) {
                    for (FileBatch batch : filesRepository.getFileIterator(fileId.toString())) {
                        bufferesOutputStream.write(batch.batchData);
                    }
                    bufferesOutputStream.flush();
                }
                break;
            default:
                throw new RuntimeException(String.format("Fail to download file %s with content type %s.", fileId, fileMeta.getContentType()));
            }
        }
        else {
            respondText(response, String.format("Unable to download file with id %s since its size greater than %sMb.", fileId, maxFileSize / 1024 / 1024));
        }
    }

    @Override
    public String getRoute() {
        return "/files/info";
    }
}

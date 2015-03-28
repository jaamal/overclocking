package controllers;

import handlers.IFileInfoHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import models.FileInfo;

import org.apache.commons.io.IOUtils;

import play.Logger;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Header;
import services.blobs.IBlobService;
import configuration.Configurator;

public class TextsStorage extends Controller
{
	private static IBlobService blobService;
	
	@Before
	static void init() {
		blobService = Configurator.getContainer().get(IBlobService.class);
	}
	
	public static void run() {
		List<FileInfo> filesInfo = Configurator.getContainer().get(IFileInfoHandler.class).find();
        render("TextsStorage/textsStorage.html", filesInfo);
	}
	
	public static void download(@Required String textId) {
		if(validation.hasErrors()) {
		       params.flash();
		       validation.keep();
		       run();
		}

		try {
			response.out.write(blobService.drain(textId));
			response.contentType = "application/octet-stream";
			response.headers.put("Content-Disposition", new Header("Content-Disposition", "attachment; filename="+ textId +".txt"));
		}
		catch(Exception e) {
			Logger.error("Fail to download file with id " + textId, e);
			error(e);
		}
	}
	
	public static void submit(@Required String textId, @Required File textFile) {
		if(validation.hasErrors()) {
		       params.flash();
		       validation.keep();
		       run();
		}

		try {
			byte[] content = IOUtils.toByteArray(new FileInputStream(textFile));
			blobService.write(textId, content);
		}
		catch(Exception e) {
			Logger.error("Fail to submit file with id " + textId, e);
			error(e);
		}
	}
}

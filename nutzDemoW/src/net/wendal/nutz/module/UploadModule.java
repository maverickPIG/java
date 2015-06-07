package net.wendal.nutz.module;

import java.awt.Color;
import java.io.File;
import java.util.UUID;

import org.nutz.img.Images;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

@IocBean
@At("/upload")
public class UploadModule {
	
	private static final Log log = Logs.get();

	@At("/html4")
	@AdaptBy(type=UploadAdaptor.class, args="ioc:upload")
	public Object html4(@Param("f")TempFile tmpFile, AdaptorErrorContext errCtx) {
		if (errCtx != null) {
			log.info(errCtx.getAdaptorErr());
			return false;
		}
		if (tmpFile == null || tmpFile.getFile().length() < 1024) {
			return false;
		}
		
		log.debug(tmpFile.getMeta().getFileLocalName());
		
		File file = tmpFile.getFile();
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String dest = webPath(uuid) + "." + Files.getSuffixName(file).toLowerCase();
		String smallPath = webPath(uuid) + "_128x128." + Files.getSuffixName(file).toLowerCase();
		try {
			Images.zoomScale(file, new File(smallPath), 128, 128, Color.BLACK);
			file.renameTo(new File(dest));
		} catch (Throwable e) {
			log.info(e);
			return false;
		}
		
		
		return "/upload/images/" + uuid + "."+ Files.getSuffixName(file).toLowerCase();
	}
	
	public String webPath(String path) {
		return Mvcs.getServletContext().getRealPath("/upload/images/") + "/" + path;
	}
}

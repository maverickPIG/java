var ioc = {
		upload : {
			type : "org.nutz.mvc.upload.UploadAdaptor",
			args : [{refer : "uploadCtx"}]
		},
		uploadCtx : {
			type : "org.nutz.mvc.upload.UploadingContext",
			args : [{refer: "filePool"}],
			fields : {
				ignoreNull : true,
				maxFileSize : 10240000,
				nameFilter : ".+(jpg|png)"
			}
		},
		filePool : {
			type : "net.wendal.nutz.ext.WebFilePool",
			args : ["/upload/", 2000]
		}
};
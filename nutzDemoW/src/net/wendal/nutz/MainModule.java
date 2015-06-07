package net.wendal.nutz;

import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

@Modules(scanPackage=true)
@Ok("json")
@Fail("jsp:/500.jsp")
@IocBy(type=ComboIocProvider.class, args={
	"*org.nutz.ioc.loader.json.JsonLoader", "ioc/",
	"*org.nutz.ioc.loader.annotation.AnnotationIocLoader", "net.wendal.nutz"
})
@SetupBy(value=WendalSetup.class)
@Filters({@By(type=WendalActionFilter.class)})
public class MainModule {

}

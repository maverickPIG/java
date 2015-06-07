package net.wendal.nutz;

import net.wendal.nutz.bean.User;

import org.nutz.dao.Dao;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.resource.Scans;

public class WendalSetup implements Setup {
	
	@Override
	public void init(NutConfig config) {
		Dao dao = config.getIoc().get(Dao.class);
		for (Class<?> klass : Scans.me().scanPackage("net.wendal.nutz")) {
			if (null != klass.getAnnotation(Table.class))
				dao.create(klass, false);
		}
		
		if (dao.count(User.class) == 0) {
			User admin = new User();
			admin.setName("admin");
			admin.setPasswd("123456");
			dao.insert(admin);
		}
	}

	@Override
	public void destroy(NutConfig config) {
		
	}

}

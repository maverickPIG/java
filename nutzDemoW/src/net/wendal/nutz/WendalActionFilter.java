package net.wendal.nutz;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

public class WendalActionFilter implements ActionFilter {

	@Override
	public View match(ActionContext actionContext) {
		Object obj = actionContext.getMethodArgs();
		System.out.println(">>>>>>>>>>>   "+ obj);
		return null;
	}

}

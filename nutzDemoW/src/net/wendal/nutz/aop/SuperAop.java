package net.wendal.nutz.aop;

import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * 最最简单的Aop拦截器,事实上它什么都不做
 * @author wendal
 *
 */
@IocBean
public class SuperAop implements MethodInterceptor {

	public void filter(final InterceptorChain chain) throws Throwable {
		chain.doChain();
	}

}

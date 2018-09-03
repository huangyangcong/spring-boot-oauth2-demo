package com.luangeng.oauthserver.watchmen;

import com.luangeng.oauthserver.exception.RequestLimitException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 限制某个controller的并发数
 */
@Aspect
@Component
public class WatchmenAspect {

//    private static final Logger logger = LoggerFactory.getLogger("WatchmenAspect");

    @Autowired
    private RedisDao redisDao;


    /**
     * 切入点表达式：
     * execution(方法表达式)
     * within(类型表达式) 指定类中的任何方法
     * this(类型全限定名) 当前AOP对象实现了指定接口的任何方法
     * target(类型全限定名) 目标对象实现了指定接口的任何方法
     * args(参数类型列表) 匹配传入的参数类型
     * bean(Bean id或name通配符) 特定名称的Bean对象的执行方法
     *
     * @within(注解类型) 持有指定注解类型内的所有方法
     * @annotation(注解类型) 有该注解的方法
     * @target(注解类型) 持有目标对象类型注解的任何方法
     * @args(注解列表) 参数带该注解的方法
     */

    @Before("within(@org.springframework.stereotype.Controller *) && @target(limit)")
    public void requestLimit(final JoinPoint joinPoint, Watchmen limit) throws RequestLimitException {
//        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
        RequestDesc desc = RequestDescContextHolder.get();
        redisDao.log(desc, limit);
    }

//    @After("")
//    public void qqq(){
//
//    }
//
//    @AfterThrowing("")
//    public void qq(){
//
//    }

}

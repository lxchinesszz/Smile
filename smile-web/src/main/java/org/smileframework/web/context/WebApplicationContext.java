package org.smileframework.web.context;

import org.slf4j.Logger;
import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.bean.context.BeanDefinition;
import org.smileframework.ioc.bean.context.ConfigApplicationContext;
import org.smileframework.ioc.bean.context.ConfigurableEnvironment;
import org.smileframework.ioc.bean.context.ExtApplicationContext;
import org.smileframework.tool.logmanage.LoggerManager;
import org.smileframework.tool.string.StringTools;
import org.smileframework.web.annotation.GetMapping;
import org.smileframework.web.annotation.PostMapping;
import org.smileframework.web.annotation.RequestMethod;
import org.smileframework.web.annotation.RestController;
import org.smileframework.web.exception.BindUrlHanderException;
import org.smileframework.web.handler.WebDefinition;
import org.smileframework.web.server.dispatch.Web404Definition;
import org.smileframework.web.server.dispatch.Web405Definition;
import org.smileframework.web.server.netty.NettyBootstrapServer;
import org.smileframework.web.util.WebContextTools;
import org.smileframework.web.util.WebTools;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Package: org.smileframework.web.context
 * @Description: web上下文
 * @author: liuxin
 * @date: 2017/12/4 下午2:55
 */
@SmileComponent
public class WebApplicationContext implements ExtApplicationContext {
    private static Logger logger = LoggerManager.getLogger(WebApplicationContext.class);
    /**
     * key:url
     */
    private static Map<String, WebDefinition> webHandlerMaps = new ConcurrentHashMap<>();
    /**
     * 上下文对象
     */
    private static ConfigApplicationContext configApplicationContext;

    @Override
    public void mergeContext(ConfigApplicationContext extApplicationContext) {
        configApplicationContext = extApplicationContext;
        //TODO 获取上下文,绑定url controllerAnnotation
        Map<String, BeanDefinition> controllerBeans = configApplicationContext.getBeanByAnnotation(RestController.class);
        //TODO url和beanDefiniton
        bindUrlAndHandler(controllerBeans);
        WebContextTools.setWebApplicationContext(this);
        NettyBootstrapServer server = new NettyBootstrapServer();
        ConfigurableEnvironment configurableEnvironment = extApplicationContext.getConfigurableEnvironment();
        Integer port = Integer.parseInt(configurableEnvironment.getProperty("server.port"));
        String pid = getPid();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                server.bindPort(port, pid, extApplicationContext.getStopWatch());
            }
        });
        thread.setDaemon(true);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }


    public String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;
    }

    /**
     * 根据url和方法获取web处理器
     *
     * @param url           请求地址
     * @param requestMethod 请求方法
     * @return
     */
    public WebDefinition getWebDefinitionByUrl(String url, RequestMethod requestMethod) {
        //当不存在,返回一个默认的处理器
        WebDefinition orDefault = webHandlerMaps.get(url);
        if (orDefault == null) {
            return new Web404Definition();
        }
        RequestMethod value = orDefault.getValue();
        if (!StringTools.endsWithIgnoreCase(value.name(), requestMethod.name())) {
            //FIXME 返回默认处理器,用来返回405错误
            return new Web405Definition();
        }
        return orDefault;
    }

    /**
     * 绑定url和处理器
     */
    public void bindUrlAndHandler(Map<String, BeanDefinition> definitionMap) {
        if (definitionMap.size() <= 0) {
            throw new BindUrlHanderException("请检查是否添加url,添加url,请使用 [@RestController,@GetMapping @PostMapping]");
        }
        Consumer<Map.Entry<String, BeanDefinition>> entryConsumer = entry -> {
            BeanDefinition beanDefinition = entry.getValue();
            Class<?> controllerClass = beanDefinition.getClazz();
            RestController annotation = controllerClass.getAnnotation(RestController.class);
            String oneUrl = annotation.value();
            Method[] methods = controllerClass.getMethods();
            for (Method method : methods) {
                boolean isGet = method.isAnnotationPresent(GetMapping.class);
                if (isGet) {
                    bindGetMethod(oneUrl, method, beanDefinition);
                }
                boolean isPost = method.isAnnotationPresent(PostMapping.class);
                if (isPost) {
                    bindPostMethod(oneUrl, method, beanDefinition);
                }
            }
        };
        definitionMap.entrySet().forEach(entryConsumer);
    }

    /**
     * 绑定get请求
     *
     * @param oneUrl         一级url
     * @param method         方法
     * @param beanDefinition bean描述
     */
    public void bindGetMethod(String oneUrl, Method method, BeanDefinition beanDefinition) {
        Object controllerInstance = beanDefinition.getInstance();
        Package aPackage = beanDefinition.getClazz().getPackage();
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        String twoUrl = getMapping.value();
        String[] parameterNames = WebTools.getParameterNames(method);
        if (StringTools.isEmpty(twoUrl)) {
            throw new BindUrlHanderException("[ " + aPackage.getName() + " ]:绑定url异常,请检查,请填写需要绑定的url地址");
        }
        String realUrl = WebTools.checkUrl(oneUrl, twoUrl);
        String methodPath = method.toGenericString();
        logger.info("Mapped url:[{}],produces:[{}],consumes:[{}],paramter:{},onto:{}", realUrl, getMapping.produces(), getMapping.consumes(), parameterNames, methodPath);
        webHandlerMaps.put(realUrl, new WebDefinition(realUrl, RequestMethod.GET, getMapping.consumes(), getMapping.produces(), controllerInstance, method, parameterNames));
    }

    /**
     * 绑定post请求
     *
     * @param oneUrl         一级url
     * @param method         方法
     * @param beanDefinition bean描述
     */
    public void bindPostMethod(String oneUrl, Method method, BeanDefinition beanDefinition) {
        Object controllerInstance = beanDefinition.getInstance();
        Package aPackage = beanDefinition.getClazz().getPackage();
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        String twoUrl = postMapping.value();
        String[] parameterNames = WebTools.getParameterNames(method);
        if (StringTools.isEmpty(twoUrl)) {
            throw new BindUrlHanderException("[ " + aPackage.getName() + " ]:绑定url异常,请检查,请填写需要绑定的url地址");
        }
        String realUrl = WebTools.checkUrl(oneUrl, twoUrl);
        String methodPath = method.toGenericString();
        logger.info("Mapped url:[{}],produces:[{}],consumes:[{}],paramter:{},onto:{}", realUrl, postMapping.produces(), postMapping.consumes(), parameterNames, methodPath);
        webHandlerMaps.put(realUrl, new WebDefinition(realUrl, RequestMethod.POST, postMapping.consumes(), postMapping.produces(), controllerInstance, method, parameterNames));
    }
}

package org.smileframework.web.util;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.smileframework.tool.clazz.CastConvert;
import org.smileframework.tool.clazz.ReflectionTools;
import org.smileframework.tool.string.StringTools;
import org.smileframework.web.annotation.RequestBoby;
import org.smileframework.web.annotation.RequestHeader;
import org.smileframework.web.annotation.RequestParam;
import org.smileframework.web.handler.WebDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * @Package: org.smileframework.web.util
 * @Description: 给路由方法注入参数
 * @author: liuxin
 * @date: 2017/12/12 下午5:32
 */
public class ControllerUtils {

    public static String[] getParameterNames(Method method) {
        //declar不局限与修饰符
        Class clazz = method.getDeclaringClass();
        String methodName = method.getName();
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(clazz));
        CtMethod cm = null;
        LocalVariableAttribute attr =
                null;
        String[] paramNames = new String[0];
        try {
            CtClass cc = pool.get(clazz.getName());
            cm = cc.getDeclaredMethod(methodName);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                return null;
            }
            paramNames = new String[cm.getParameterTypes().length];
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        int pos = java.lang.reflect.Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
        }
        return paramNames;
    }

    public static Class<?>[] getParameterTypes(Method method) {
        return method.getParameterTypes();
    }

    private static Annotation getAnnotation(Annotation[] annotations, Class<? extends Annotation> annotation) {
        for (int i = 0; i < annotations.length; i++) {
            Annotation srcAnnotation = annotations[i];
            if (srcAnnotation.annotationType().isAssignableFrom(annotation)) {
                return srcAnnotation;
            }
        }
        return null;
    }


    /**
     * 参数描述在加载url时候,统一处理
     * @param webDefinition
     * @param requestParameter
     * @param headers
     * @return
     * @throws Exception
     */
    public static Object[] getArgs(WebDefinition webDefinition, Map<String, Object> requestParameter, Map<String, Object> headers) throws Exception {
        List<ReflectionTools.ParamDefinition> parameterDefinitions = webDefinition.getParamDefinitions();
        Object[] args = new Object[parameterDefinitions.size()];
        Object requestParam;
        for (int i = 0; i < parameterDefinitions.size(); i++) {
            //参数描述
            ReflectionTools.ParamDefinition paramDefinition = parameterDefinitions.get(i);
            Annotation isRequestParam = getAnnotation(paramDefinition.getDeclaredAnnotations(), RequestParam.class);
            Annotation isRequestBody = getAnnotation(paramDefinition.getDeclaredAnnotations(), RequestBoby.class);
            Annotation isRequestHeader = getAnnotation(paramDefinition.getDeclaredAnnotations(), RequestHeader.class);
            if (isRequestParam != null) {
                //当使用RequestParam 则value必须指定
                String requestName = ((RequestParam) isRequestParam).value();
                String requestValue = (String) requestParameter.get(requestName);
                if (StringTools.isEmpty(requestValue)) {
                    throw new IllegalArgumentException("缺少请求参数:" + requestName);
                }
                String argIndexStr = paramDefinition.getArgIndex();
                int argIndex = Integer.parseInt(argIndexStr);
                //转换参数类型
                requestParam = CastConvert.cast(paramDefinition.getParamType(), requestValue);
                args[argIndex] = requestParam;
            } else if(isRequestBody != null){
                String requestValue = (String) requestParameter.get("BODY");
                if (StringTools.isEmpty(requestValue)) {
                    throw new IllegalArgumentException("请检查请求体参数信息");
                }
                String argIndexStr = paramDefinition.getArgIndex();
                int argIndex = Integer.parseInt(argIndexStr);
                //转换参数类型
                requestParam = CastConvert.cast(paramDefinition.getParamType(), requestValue);
                args[argIndex] = requestParam;
            }else if (isRequestHeader!=null){
                String argIndexStr = paramDefinition.getArgIndex();
                int argIndex = Integer.parseInt(argIndexStr);
                //转换参数类型
                args[argIndex] = headers;
            }
            else {
                String requestName = paramDefinition.getParamName();
                String requestValue = (String) requestParameter.get(requestName);
                if (StringTools.isEmpty(requestValue)) {
                    throw new IllegalArgumentException("缺少请求参数:" + requestName);
                }
                String argIndexStr = paramDefinition.getArgIndex();
                int argIndex = Integer.parseInt(argIndexStr);
                //转换参数类型
                requestParam = CastConvert.cast(paramDefinition.getParamType(), requestValue);
                args[argIndex] = requestParam;
            }
        }
        return args;
    }

    /**
     *
     * @param method
     * @param requestParameter
     * @param headers
     * @return
     * @throws Exception
     */
    public static Object[] getArgs(Method method, Map<String, Object> requestParameter,Map<String, Object> headers) throws Exception {
        List<ReflectionTools.ParamDefinition> parameterDefinitions = ReflectionTools.getParameterDefinitions(method);
        Object[] args = new Object[parameterDefinitions.size()];
        Object requestParam;
        for (int i = 0; i < parameterDefinitions.size(); i++) {
            //参数描述
            ReflectionTools.ParamDefinition paramDefinition = parameterDefinitions.get(i);
            Annotation isRequestParam = getAnnotation(paramDefinition.getDeclaredAnnotations(), RequestParam.class);
            Annotation isRequestBody = getAnnotation(paramDefinition.getDeclaredAnnotations(), RequestBoby.class);
            Annotation isRequestHeader = getAnnotation(paramDefinition.getDeclaredAnnotations(), RequestHeader.class);
            if (isRequestParam != null) {
                //当使用RequestParam 则value必须指定
                String requestName = ((RequestParam) isRequestParam).value();
                String requestValue = (String) requestParameter.get(requestName);
                if (StringTools.isEmpty(requestValue)) {
                    throw new IllegalArgumentException("缺少请求参数:" + requestName);
                }
                String argIndexStr = paramDefinition.getArgIndex();
                int argIndex = Integer.parseInt(argIndexStr);
                //转换参数类型
                requestParam = CastConvert.cast(paramDefinition.getParamType(), requestValue);
                args[argIndex] = requestParam;
            } else if(isRequestBody != null){
                String requestValue = (String) requestParameter.get("BODY");
                if (StringTools.isEmpty(requestValue)) {
                    throw new IllegalArgumentException("请检查请求体参数信息");
                }
                String argIndexStr = paramDefinition.getArgIndex();
                int argIndex = Integer.parseInt(argIndexStr);
                //转换参数类型
                requestParam = CastConvert.cast(paramDefinition.getParamType(), requestValue);
                args[argIndex] = requestParam;
            }else if (isRequestHeader!=null){
                String argIndexStr = paramDefinition.getArgIndex();
                int argIndex = Integer.parseInt(argIndexStr);
                //转换参数类型
                args[argIndex] = headers;
            }
            else {
                String requestName = paramDefinition.getParamName();
                String requestValue = (String) requestParameter.get(requestName);
                if (StringTools.isEmpty(requestValue)) {
                    throw new IllegalArgumentException("缺少请求参数:" + requestName);
                }
                String argIndexStr = paramDefinition.getArgIndex();
                int argIndex = Integer.parseInt(argIndexStr);
                //转换参数类型
                requestParam = CastConvert.cast(paramDefinition.getParamType(), requestValue);
                args[argIndex] = requestParam;
            }
        }
        return args;
    }
}


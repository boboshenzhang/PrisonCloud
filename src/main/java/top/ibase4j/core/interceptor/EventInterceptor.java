package top.ibase4j.core.interceptor;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiOperation;
import top.ibase4j.core.base.BaseService;
import top.ibase4j.core.support.context.ApplicationContextHolder;
import top.ibase4j.core.support.http.SessionUser;
import top.ibase4j.core.util.DateUtil;
import top.ibase4j.core.util.ExceptionUtil;
import top.ibase4j.core.util.WebUtil;
import top.ibase4j.model.SysEvent;

/**
 * 日志拦截器
 *
 * @author ShenHuaJie
 * @version 2016年6月14日 下午6:18:46
 */
public class EventInterceptor extends BaseInterceptor {
    private final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocalStartTime");
    protected ExecutorService executorService = Executors.newCachedThreadPool();

    private BaseService<SysEvent> sysEventService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String path = request.getServletPath();
        // 开始时间（该数据只有当前请求的线程可见）
        startTimeThreadLocal.set(System.currentTimeMillis());
        Map<String, Object> params = WebUtil.getParameterMap(request);
        logger.info("request {} parameters===>{}", path, JSON.toJSONString(params));
        WebUtil.REQUEST.set(request);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, HttpServletResponse response, Object handler,
        final Exception ex) throws Exception {
        final Long startTime = startTimeThreadLocal.get();
        final Long endTime = System.currentTimeMillis();
        String path = request.getServletPath();
        // 保存日志
        if (handler instanceof HandlerMethod) {
            try {
                String userAgent = request.getHeader("USER-AGENT");
                String clientIp = WebUtil.getHost(request);
                SessionUser session = WebUtil.getCurrentUser(request);
                if (!path.contains("/error") && !path.contains("/read/") && !path.contains("/get")
                    && !path.contains("/query") && !path.contains("/detail")
                        && !path.contains("/unauthorized") && !path.contains("/forbidden")) {
                    final SysEvent record = new SysEvent();
                    record.setMethod(request.getMethod());
                    record.setRequestUri(path);
                    record.setClientHost(clientIp);
                    record.setUserAgent(userAgent);
                    if (path.contains("/upload")) {
                        record.setParameters("");
                    } else {
                        record.setParameters(JSON.toJSONString(WebUtil.getParameter(request)));
                    }
                    record.setStatus(response.getStatus());
                    if (session != null) {
                        record.setUserName(session.getUserName());
                        record.setUserPhone(session.getUserPhone());
                        record.setCreateBy(session.getId());
                        record.setUpdateBy(session.getId());
                    }
                    final String msg = (String)request.getAttribute("msg");
                    try {
                        HandlerMethod handlerMethod = (HandlerMethod)handler;
                        ApiOperation apiOperation = handlerMethod.getMethod().getAnnotation(ApiOperation.class);
                        if (apiOperation != null) {
                            record.setTitle(apiOperation.value());
                        }
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            try { // 保存操作
                                if (StringUtils.isNotBlank(msg)) {
                                    record.setRemark(msg);
                                } else {
                                    record.setRemark(ExceptionUtil.getStackTraceAsString(ex));
                                }
                                saveEvent(record);
                            } catch (Exception e) {
                                logger.error("Save event log cause error :", e);
                            }
                        }
                    });
                } else if (path.contains("/unauthorized")) {
                    logger.warn("The user [{}] no login", clientIp + "@" + userAgent);
                } else if (path.contains("/forbidden")) {
                    logger.warn("The user [{}] no promission",
                        JSON.toJSONString(session) + "@" + clientIp + "@" + userAgent);
                } else {
                    logger.info(JSON.toJSONString(session) + "@" + path + "@" + clientIp + userAgent);
                }
            } catch (Throwable e) {
                logger.error("", e);
            }
        }
        // 内存信息
        String message = "Starttime: {}; Endtime: {}; Used time: {}s; URI: {}; ";
        logger.debug(message, DateUtil.format(startTime, "HH:mm:ss.SSS"), DateUtil.format(endTime, "HH:mm:ss.SSS"),
            String.valueOf((endTime - startTime) / 1000.00), path);
        super.afterCompletion(request, response, handler, ex);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void saveEvent(SysEvent record) {
        synchronized (EventInterceptor.class) {
            if (sysEventService == null) {
                Map<String, BaseService> map = ApplicationContextHolder.getBeansOfType(BaseService.class);
                for (String key : map.keySet()) {
                    if (key.toLowerCase().contains("sysevent")) {
                        sysEventService = map.get(key);
                    }
                }
            }
        }
        //sysEventService.update(record);
    }
}

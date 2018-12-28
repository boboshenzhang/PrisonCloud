package top.ibase4j.core.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import top.ibase4j.core.util.DateUtil;
import top.ibase4j.core.util.FileUtil;
import top.ibase4j.core.util.WebUtil;

/**
 * 跨站请求拦截
 * @author ShenHuaJie
 * @since 2018年7月27日 上午10:58:14
 */
public class CsrfFilter implements Filter {
    private Logger logger = LogManager.getLogger();

    // 白名单
    private List<String> whiteUrls;

    private int _size = 0;

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("init CsrfFilter..");
        // 读取文件
        String path = CsrfFilter.class.getResource("/").getFile();
        whiteUrls = FileUtil.readFile(path + "white/csrfWhite.txt");
        _size = null == whiteUrls ? 0 : whiteUrls.size();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest)request;
            // 获取请求url地址
            String url = req.getRequestURL().toString();
            String referurl = req.getHeader("Referer");
            if (WebUtil.isWhiteRequest(referurl, _size, whiteUrls)) {
                chain.doFilter(request, response);
            } else {
                // 记录跨站请求日志
                logger.warn("跨站请求---->>>{} || {} || {} || {}", url, referurl, WebUtil.getHost(req),
                    DateUtil.getDateTime());
                WebUtil.write(response, 308, "错误的请求头信息");
                return;
            }
        } catch (Exception e) {
            logger.error("doFilter", e);
        }
    }

    @Override
    public void destroy() {
        logger.info("destroy CsrfFilter.");
    }
}

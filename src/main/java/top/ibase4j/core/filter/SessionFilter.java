package top.ibase4j.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import top.ibase4j.core.support.http.SessionUser;
import top.ibase4j.core.util.ShiroUtil;
import top.ibase4j.core.util.WebUtil;

/**
 * SESSION会话转存REQUEST
 * @author ShenHuaJie
 * @since 2018年7月22日 上午9:29:55
 */
public class SessionFilter implements Filter {
    private Logger logger = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init SessionFilter.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        SessionUser sessionUser = ShiroUtil.getCurrentUser();
        if (sessionUser != null) {
            WebUtil.saveCurrentUser((HttpServletRequest)request, sessionUser);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("destroy SessionFilter.");
    }
}

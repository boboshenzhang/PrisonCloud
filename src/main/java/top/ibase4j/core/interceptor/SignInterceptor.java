package top.ibase4j.core.interceptor;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import top.ibase4j.core.support.http.HttpCode;
import top.ibase4j.core.util.FileUtil;
import top.ibase4j.core.util.WebUtil;

/**
 * 签名验证
 * @author ShenHuaJie
 * @since 2018年5月12日 下午10:40:38
 */
public class SignInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LogManager.getLogger();
    // 白名单
    private List<String> whiteUrls;
    private int _size = 0;

    public SignInterceptor() {
        // 读取文件
        String path = SignInterceptor.class.getResource("/").getFile();
        whiteUrls = FileUtil.readFile(path + "white/signWhite.txt");
        _size = null == whiteUrls ? 0 : whiteUrls.size();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 请求秘钥的接口不需要签名
        String url = request.getRequestURL().toString();
        String refer = request.getHeader("Referer");
        if (refer != null && refer.contains("/swagger") || WebUtil.isWhiteRequest(url, _size, whiteUrls)) {
            logger.info("SignInterceptor skip");
            return true;
        }
        String sign = request.getHeader("sign");
        if (sign == null) {
            return WebUtil.write(response, HttpCode.NOT_ACCEPTABLE.value(), "请求参数未签名");
        }
        // 获取参数
        Map<String, Object> params = WebUtil.getParameterMap(request);
        String[] keys = params.keySet().toArray(new String[]{});
        Arrays.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (!"dataFile".equals(key)) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key).append("=").append(params.get(key));
            }
        }
        // 验证信息摘要MD5加密字节转十六进制字符串
        String encrypt = DigestUtils.md5Hex(URLEncoder.encode(sb.toString(), "UTF-8"));
        if (!encrypt.toLowerCase().equals(sign.toLowerCase())) {
            return WebUtil.write(response, HttpCode.UNAUTHORIZED.value(), HttpCode.UNAUTHORIZED.msg());
        }
        logger.info("SignInterceptor successful");
        return true;
    }
}

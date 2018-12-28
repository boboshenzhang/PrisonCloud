package top.ibase4j.core.interceptor;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import top.ibase4j.core.Constants;
import top.ibase4j.core.support.http.HttpCode;
import top.ibase4j.core.support.http.SessionUser;
import top.ibase4j.core.support.security.coder.HmacCoder;
import top.ibase4j.core.util.CacheUtil;
import top.ibase4j.core.util.DataUtil;
import top.ibase4j.core.util.FileUtil;
import top.ibase4j.core.util.PropertiesUtil;
import top.ibase4j.core.util.SecurityUtil;
import top.ibase4j.core.util.WebUtil;

/**
 * 签名验证
 * @author ShenHuaJie
 * @since 2018年5月12日 下午10:40:38
 */
public class TokenInterceptor extends BaseInterceptor {
    private SignInterceptor signInterceptor;
    // 白名单
    private List<String> whiteUrls;
    private int _size = 0;

    public TokenInterceptor() {
        signInterceptor = new SignInterceptor();
        // 读取文件
        String path = TokenInterceptor.class.getResource("/").getFile();
        whiteUrls = FileUtil.readFile(path + "white/tokenWhite.txt");
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
            if (signInterceptor.preHandle(request, response, handler)) {
                return super.preHandle(request, response, handler);
            }
            return WebUtil.write(response, HttpCode.UNAUTHORIZED.value(), HttpCode.UNAUTHORIZED.msg());
        }
        // 获取token
        String token = request.getHeader("token");
        if (DataUtil.isEmpty(token)) {
            return WebUtil.write(response, HttpCode.NOT_ACCEPTABLE.value(), "缺少签名必须条件");
        }
        String sign = request.getHeader("sign");
        if (sign == null) {
            return WebUtil.write(response, HttpCode.NOT_ACCEPTABLE.value(), "请求参数未签名");
        }
        // 判断token是否过期
        String cacheKey = Constants.TOKEN_KEY + SecurityUtil.encryptMd5(token);
        SessionUser session = (SessionUser)CacheUtil.getCache().get(cacheKey);
        if (DataUtil.isEmpty(session)) {
            return WebUtil.write(response, HttpCode.NOT_EXTENDED.value(), "密钥已过期");
        } else {
            request.setAttribute(Constants.CURRENT_USER, session);
            CacheUtil.getCache().expire(cacheKey, PropertiesUtil.getInt("APP-TOKEN-EXPIRE", 60 * 60 * 24 * 5));
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
            // 验证信息摘要HmacMD5加密字节转十六进制字符串
            String encrypt = SecurityUtil.encryptHMAC(HmacCoder.MD5, URLEncoder.encode(sb.toString(), "UTF-8"), token);
            if (!encrypt.toLowerCase().equals(sign.toLowerCase())) {
                return WebUtil.write(response, HttpCode.UNAUTHORIZED.value(), HttpCode.UNAUTHORIZED.msg());
            }
        }
        logger.info("SignInterceptor successful");
        return super.preHandle(request, response, handler);
    }
}

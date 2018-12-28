/**
 * 
 */
package top.ibase4j.core.base;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

/**
 * 
 * @author ShenHuaJie
 * 
 * @version 2016年6月3日 下午2:30:14
 * 
 */
public interface PublicityBaseMapper<T extends PublicityBaseModel> extends com.baomidou.mybatisplus.mapper.BaseMapper<T> {

	List<String> selectIdPage(@Param("cm") T params);

	List<String> selectIdPage(@Param("cm") Map<String, Object> params);

	List<String> selectIdPage(RowBounds rowBounds, @Param("cm") Map<String, Object> params);

	List<String> selectIdPage(RowBounds rowBounds, @Param("cm") T params);
}

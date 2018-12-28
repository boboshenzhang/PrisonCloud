/**
 * 
 */
package top.ibase4j.core.util;

/**
 * @author acer
 *
 */
public class NodeMapper {
	/**
	 * 枚举Pdf地址
	 * @author wcx
	 */
	public enum node {
		js_shb("http://10.0.0.91/prison-punishment/jiashizuifanController.do?printShb&pdf=pdf&id="), 
		js_jcyjys("http://10.0.0.91/prison-punishment/jiashizuifanController.do?printJysJcyA&pdf=pdf&id="),
		js_jys("http://10.0.0.91/prison-punishment/jiashizuifanController.do?printJysA&pdf=pdf&id="),
		js_wth("http://10.0.0.90/prison-punishment/jiashizuifanController.do?printWtsfjdcpg&pdf=pdf&mdid="), 
		
		jx_shb("http://10.0.0.91/prison-punishment/jianxingzuifanController.do?printShb&pdf=pdf&id="), 
		jx_jcyjys("http://10.0.0.91/prison-punishment/jianxingzuifanController.do?printJysJcyA&pdf=pdf&id="),
		jx_jys("http://10.0.0.91/prison-punishment/jianxingzuifanController.do?printJysA&pdf=pdf&id="),
		
		jw_zdspb("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printZdspb&pdf=pdf&id="),
		jw_jcs("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printJcs&pdf=pdf&id="),
		jw_wts("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printWts&pdf=pdf&id="),
		jw_wxxpg("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printWxxpg&pdf=pdf&id="),
		jw_zgscb("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printZgscb&pdf=pdf&id="),
		jw_hsqkb("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printHsqkb&pdf=pdf&id="),
		jw_hswth("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printHswth&pdf=pdf&id="),
		jw_gs("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printGs&pdf=pdf&id="),
		jw_gsjg("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printGsjg&pdf=pdf&id="),
		jw_jds("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printJds&pdf=pdf&id="),
		jw_byjds("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printByjds&pdf=pdf&id="),
		jw_yjs("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printYjs&pdf=pdf&id="),
		jw_spb("http://10.0.0.91/prison-punishment/jianwaizhixingzuifanController.do?printSpb&pdf=pdf&id="),
		xf_rjdjb("http://10.0.0.91/prison-punishment/rujiandengjiController.do?rujiandengjiGet&pdf=pdf&id="),
		
		xf_fj("http://10.0.0.91/prison-punishment/imageController.do?down&fileId="), 
		zf_fj("http://10.0.0.91/prison-criminal/imageController.do?down&fileId="),
		admin_Hyjl("http://10.0.0.65/prison-admin/conferenceController.do?print&pdf=pdf&id=");
		private String name;
		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		private node(String name) {
			this.name = name;
		}

		public String getName(String name) {
			for (node c : node.values()) {
				if (c.getName(name).equals(name)) {
					return c.name;
				}
			}
			return null;
		}
		public String toString() {
			return name;// 重写toString方法
		}

	}
}

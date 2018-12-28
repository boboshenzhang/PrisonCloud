package top.ibase4j.core.base;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class PrisonBaseModel implements Serializable {
	@TableId(value = "ID",type=IdType.UUID)
	private String id;
	@TableField(exist = false)
	private List<String> ids;
	@TableField(exist = false)
	private String keyword;
	@TableField(exist = false)
	private Boolean newRecord = false;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Boolean getNewRecord() {
		return newRecord;
	}

	public void setNewRecord(Boolean newRecord) {
		this.newRecord = newRecord;
	}
}

package dts_project.property;

public class Property {
	String group;
	String id;
	String name;
	String type;
	String category;
	boolean isReadOnly;
	boolean isHidden;
	boolean isArray;
	boolean isSimple;
	String unit;
	String value;
	String description;

	public String getId() {
		return id;
	}

	// TODO 可能不需要setId方法
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 构造属性，其余值为默认值
	 *
	 * @param id   属性的id
	 * @param name 属性名
	 * @param type 属性类型
	 */
	public Property(String id, String name, String type) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.category = "A";
		this.value = "";
	}

	public Property(String id, String name, String type, String group) {
		this.group = group;
		this.id = id;
		this.name = name;
		this.type = type;
		this.category = "A";
		this.value = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean readOnly) {
		isReadOnly = readOnly;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean hidden) {
		isHidden = hidden;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean array) {
		isArray = array;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroup() {
		return group;
	}

	public boolean isSimple() {
		return isSimple;
	}

	public void setSimple(boolean simple) {
		isSimple = simple;
	}
}

package dts_project.property.propertyDescriptor.textPropertyDescriptor;

public class UnitFactory {
    /**
     * 根据属性类型返回单位，如加速度返回m2
     *
     * @param type 属性的类型
     * @return 属性的单位
     */
    public String getUnit(String type) {
        return "m";
    }
}

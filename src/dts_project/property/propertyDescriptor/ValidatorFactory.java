package dts_project.property.propertyDescriptor;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class ValidatorFactory {
    /**
     * 根据属性的类型返回文本验证器（用于验证文本的合法性）
     *
     * @param type 属性的类型
     * @return 文本验证器
     */
    public ICellEditorValidator getValidator(String type) {
        return null;
    }
}

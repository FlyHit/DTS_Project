package dts_project.property.propertyDescriptor;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class ValidatorFactory {
    /**
     * 根据属性的类型返回文本验证器（用于验证文本的合法性）
     *
     * @param type 属性的类型
     * @return 文本验证器
     */
    public ICellEditorValidator getValidator(String type, String unit) {
        return new UnitValidator(unit);
    }
}

/**
 * 验证输入文本所带的单位是否合法 1. 仅输入数字，合法 2. 数字+（空白符）+单位，合法
 */
class UnitValidator implements ICellEditorValidator {
    private String unit;

    public UnitValidator(String unit) {
        this.unit = unit;
    }

    @Override
    public String isValid(Object value) {
        boolean hasPoint = true;

        String text = (String) value;
        text = text.trim(); // 去掉头尾空白符

        if (text.length() == 0) {
            return "非法的属性值";
        }

        // 必须以数字开头
        if (!Character.isDigit(text.charAt(0))) {
            return "非法的属性值";
        }

        for (int i = 0; i < text.toCharArray().length; i++) {
            char c = text.charAt(i);
            if (!Character.isDigit(c)) {
                if (c == '.' && hasPoint) {
                    hasPoint = false;
                    continue;
                }
                String maybeUnit = text.substring(i).trim();
                if (!maybeUnit.equals(unit)) {
                    System.out.println(maybeUnit);
                    System.out.println(unit);
                    return "非法的属性值";
                }
            }
        }

        return null;
    }
}

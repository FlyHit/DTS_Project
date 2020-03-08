package dts_project.property.propertyDescriptor.textPropertyDescriptor;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class CTextCellEditor extends TextCellEditor {
    private String unit;

    public CTextCellEditor(Composite parent, String unit) {
        super(parent);
        this.unit = unit;
    }

    @Override
    protected Object doGetValue() {
        String value = text.getText();
        boolean hasPoint = true;

        // 如果有单位，则在文本后面加上单位
        if (!unit.equals("")) {
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (!Character.isDigit(c)) {
                    if (c == '.' && hasPoint) {
                        hasPoint = false;
                        continue;
                    }
                    value = value.substring(0, i) + " " + unit;
                    return value;
                }
            }
        }

        return value + " " + unit;
    }


}

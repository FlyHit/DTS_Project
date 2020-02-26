package dts_project.views.mainView;

import dts_project.property.Property;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示模型的测试视图，模型属性将显示在属性视图
 */
public class MainView extends ViewPart {
    public static final String ID = "dts_project.views.mainView";
    private ListViewer listViewer;
    private List<Property> list;
    private List<List> lists;

    public MainView() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void createPartControl(Composite parent) {
        listViewer = new ListViewer(parent);
        listViewer.setContentProvider(new ArrayContentProvider());
        listViewer.setLabelProvider(new CLabelProvider());
        list = new ArrayList<>();
        list.add(new Property("name", "Name", "Text", "Property"));
        list.add(new Property("age", "Age", "Text", "Event"));
        Property property = new Property("age", "Age", "Text", "Property");
        property.setCategory("B");
        property.setSimple(true);
        list.add(property);
        list.add(new Property("gender", "Gender", "Text", "Property"));
        list.add(new Property("tel", "Tel", "Text", "Event"));
        lists = new ArrayList<>();
        lists.add(list);
        lists.add(list);
        lists.add(list);
        lists.add(list);
        listViewer.setInput(lists);
        getSite().setSelectionProvider(listViewer);
    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub

    }

}

package dts_project.views.propView;

import dts_project.property.Property;
import dts_project.views.eventView.EventView;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CPropertySheetPage extends PropertySheetPage {
    private String description;
    private StyledText text;
    private String partId;

    public CPropertySheetPage(String partId, StyledText text) {
        this.partId = partId;
        this.text = text;
    }

    @Override
    public void handleEntrySelection(ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            Optional.ofNullable((PropertySheetEntry) structuredSelection.getFirstElement()).ifPresent(entry -> {
                updateDescription(entry.getDisplayName(), entry.getDescription());
            });
        }
        super.handleEntrySelection(selection);
    }

    public void updateDescription(String name, String description) {
        StyleRange range = new StyleRange();
        range.start = 0;
        range.length = name.length();
        range.fontStyle = SWT.BOLD;

        text.setText(name + "\n" + description + getSite());
        text.setStyleRange(range);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            Object element = structuredSelection.getFirstElement();

            if (element != null) {
                List<Property> list = (List) element;
                List<Property> properties = new ArrayList<>();
                List<Property> events = new ArrayList<>();

                for (Property property : list) {
                    switch (property.getGroup()) {
                        case "Property":
                            properties.add(property);
                            break;
                        case "Event":
                            events.add(property);
                            break;
                        default:
                            break;
                    }
                }

                List<List> fileterList = new ArrayList<>();
                switch (partId) {
                    case PropView.ID:
                        fileterList.add(properties);
                        selection = new StructuredSelection(fileterList);
                        break;
                    case EventView.ID:
                        fileterList.add(events);
                        selection = new StructuredSelection(fileterList);
                        break;
                    default:
                        break;
                }
            }

        }
        super.selectionChanged(part, selection);
    }

    @Override
    public void setPropertySourceProvider(IPropertySourceProvider newProvider) {
        super.setPropertySourceProvider(newProvider);
    }
}

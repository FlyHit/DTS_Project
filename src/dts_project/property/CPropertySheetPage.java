package dts_project.property;

import CWidget.searchBox.ISearchController;
import CWidget.searchBox.SearchBox;
import dts_project.views.eventView.EventView;
import dts_project.views.propView.PropView;
import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CPropertySheetPage extends PropertySheetPage {
	private SearchBox searchBox;
	private ISearchController controller;
	private StyledText text;
	private String partId; // 持有该page的视图的ID
	private SashForm sashForm;
	private Composite propComposite;
	private Composite propPageComposite;
	private Composite helpComposite;

	public CPropertySheetPage(String partId) {
		this.partId = partId;
	}

	@Override
	public void handleEntrySelection(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			// 点击属性在帮助面板显示属性描述
			Optional.ofNullable((PropertySheetEntry) structuredSelection.getFirstElement())
					.ifPresent(entry -> updateDescription(entry.getDisplayName(), entry.getDescription()));

			super.handleEntrySelection(selection);
		}
	}

	//
	private void updateDescription(String name, String description) {
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

	// 该方法可以往工具栏/菜单栏等添加item
	@Override
	public void makeContributions(IMenuManager menuManager, IToolBarManager toolBarManager,
								  IStatusLineManager statusLineManager) {
		super.makeContributions(menuManager, toolBarManager, statusLineManager);
		// 修改toolTipText和Text（menuItem的显示文本）
		ActionContributionItem categoriesItem = (ActionContributionItem) toolBarManager.find("categories");
		IAction categories = categoriesItem.getAction();
		categories.setToolTipText("排序");
		categories.setText("排序");
		ActionContributionItem filterItem = (ActionContributionItem) toolBarManager.find("filter");
		IAction filter = filterItem.getAction();
		filter.setToolTipText("显示高级属性");
		filter.setText("只显示高级属性");

		// 重新添加菜单项和工具栏项
		toolBarManager.removeAll();
		toolBarManager.add(categories);
		toolBarManager.add(filter);

		menuManager.removeAll();
		menuManager.add(categories);
		menuManager.add(filter);
	}

	@Override
	public void createControl(Composite parent) {
		sashForm = new SashForm(parent, SWT.FLAT | SWT.VERTICAL);

		propComposite = new Composite(sashForm, SWT.FLAT | SWT.BORDER);
		propComposite.setLayout(new GridLayout());
		searchBox = new SearchBox(propComposite, SWT.FLAT);
		searchBox.setController(new ISearchController() {

			@Override
			public void search(String searchContent) {
				CPropertySheetPage.this.searchProperty(searchContent);
			}
		});
		searchBox.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		propPageComposite = new Composite(propComposite, SWT.FLAT);
		propPageComposite.setLayout(new FillLayout());
		propPageComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		super.createControl(propPageComposite);

		helpComposite = new Composite(sashForm, SWT.FLAT | SWT.BORDER);
		helpComposite.setLayout(new FillLayout());
		text = new StyledText(helpComposite, SWT.FLAT);
		// TODO 这里不可编辑但是还是会显示光标，可以试试label
		text.setEditable(false);

		sashForm.setWeights(new int[]{70, 30});
	}

}

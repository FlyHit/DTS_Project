package dts_project.views.propView;

import dts_project.views.propView.actions.SortAction;
import org.eclipse.jface.action.*;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.services.IServiceLocator;

/**
 * 属性窗口的工具栏
 */
public class PropActionBar implements IActionBars {
    private ToolBarManager toolBarManager;

    public PropActionBar() {
        toolBarManager = new ToolBarManager();
        toolBarManager.add(new SortAction());
    }

    @Override
    public void clearGlobalActionHandlers() {
    }

    @Override
    public IAction getGlobalActionHandler(String actionId) {
        return null;
    }

    @Override
    public IMenuManager getMenuManager() {
        return new MenuManager();
    }

    @Override
    public IServiceLocator getServiceLocator() {
        return null;
    }

    @Override
    public IStatusLineManager getStatusLineManager() {
        return null;
    }

    @Override
    public IToolBarManager getToolBarManager() {
        return toolBarManager;
    }

    @Override
    public void setGlobalActionHandler(String actionId, IAction handler) {
    }

    @Override
    public void updateActionBars() {
    }
}

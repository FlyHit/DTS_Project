package dts_project.renderer;

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;

@SuppressWarnings({"restriction", "unused"})
public class CommonRendererFactory extends WorkbenchRendererFactory {

    private RibbonPerspectiveRenderer perspectiveRenderer;
//	private FixedSashRenderer sashRendererRenderer;

    @Override
    public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent) {
        if (uiElement instanceof MPerspective) {
            if (perspectiveRenderer == null) {
                perspectiveRenderer = new RibbonPerspectiveRenderer();
                initRenderer(perspectiveRenderer);
            }
            return perspectiveRenderer;
        } /*
         * else if (uiElement instanceof MPartSashContainer) { if (sashRendererRenderer
         * == null) { sashRendererRenderer = new FixedSashRenderer();
         * initRenderer(sashRendererRenderer); } return sashRendererRenderer; }
         */
        return super.getRenderer(uiElement, parent);
    }
}

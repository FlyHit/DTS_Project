package dts_project.ribbon;

import com.pixelduke.control.Ribbon;
import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import javafx.embed.swt.FXCanvas;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import java.io.IOException;
import java.net.URL;

public class RibbonComposite extends Composite {
    private static final String RESOURCE = "CompleteRibbonFXML.fxml";
    private Ribbon ribbon;
    private org.eclipse.swt.widgets.Button button;

    public RibbonComposite(Composite parent, int style) throws IOException {
        super(parent, style);
        setLayout(new FillLayout());

        final FXCanvas fxCanvas = new FXCanvas(this, SWT.NONE) {
            public Point computeSize(int wHint, int hHint, boolean changed) {
                getScene().getWindow().sizeToScene();
                int width = (int) getScene().getWidth();
                int height = (int) getScene().getHeight();
                return new Point(width, height);
            }
        };

        URL resource = RibbonComposite.class.getResource(RESOURCE);
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        fxCanvas.setScene(scene);
    }

    void init(Composite parent) {
        BorderPane rootNode = new BorderPane();
        Ribbon ribbon = new Ribbon();
        RibbonTab ribbonTab = new RibbonTab("Test");
        RibbonGroup ribbonGroup = new RibbonGroup();

        rootNode.setTop(ribbon);

//		Image image = new Image("https://www.baidu.com/img/superlogo_c4d7df0a003d3db9b65e9ef0fe6da1ec.png?where=super");
////		ImageView imageView = new ImageView(image);
////		Button iconButton = new Button("Bold", imageView);
//		iconButton.setContentDisplay(ContentDisplay.TOP);
//		ribbonGroup.getNodes().add(iconButton);
//
//		ribbonTab.getRibbonGroups().add(ribbonGroup);
//		ribbon.getTabs().add(ribbonTab);

    }

}

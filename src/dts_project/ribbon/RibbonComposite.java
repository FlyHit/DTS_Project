package dts_project.ribbon;

import javafx.embed.swt.FXCanvas;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import java.io.IOException;
import java.net.URL;

public class RibbonComposite extends Composite {
	private static final String RESOURCE = "/resources/RibbonCompositeFXML.fxml";

	public RibbonComposite(Composite parent, int style) throws IOException {
		super(parent, style);
		setLayout(new FillLayout());
		setBackground(new Color(parent.getDisplay(), 0, 0, 0));

		final FXCanvas fxCanvas = new FXCanvas(this, SWT.NONE) {
			public Point computeSize(int wHint, int hHint, boolean changed) {
				getScene().getWindow().sizeToScene();
				int width = (int) getScene().getWidth();
				int height = (int) getScene().getHeight();
				return new Point(width, height);
			}
		};

		URL resource = RibbonComposite.class.getResource(RESOURCE);
		System.out.println(resource);
		Parent root = FXMLLoader.load(resource);
		Scene scene = new Scene(root);
		fxCanvas.setScene(scene);
	}
}

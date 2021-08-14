package me.coley.recaf.ui.window;

import com.panemu.tiwulfx.control.dock.DetachableTabPane;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.WindowEvent;
import me.coley.recaf.BuildConfig;
import me.coley.recaf.RecafUI;
import me.coley.recaf.config.Configs;
import me.coley.recaf.ui.control.LoggingTextArea;
import me.coley.recaf.ui.panel.DockingRootPane;
import me.coley.recaf.ui.panel.WelcomePanel;
import me.coley.recaf.ui.panel.WorkspacePanel;
import me.coley.recaf.ui.prompt.WorkspaceClosePrompt;
import me.coley.recaf.ui.util.Lang;
import me.coley.recaf.workspace.Workspace;

/**
 * Main window for Recaf.
 *
 * @author Matt Coley
 */
public class MainWindow extends WindowBase {
	private final WorkspacePanel workspacePanel = new WorkspacePanel();
	private final DockingRootPane dockingRootPane = new DockingRootPane();

	/**
	 * Create the window.
	 */
	public MainWindow() {
		init();
		setTitle("Recaf " + BuildConfig.VERSION);
	}

	@Override
	protected void init() {
		super.init();
		// Let users cancel closing the window if they have prompting enabled
		addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
			if (!Configs.display().promptCloseWorkspace) {
				System.exit(0);
				return;
			}
			Workspace workspace = RecafUI.getController().getWorkspace();
			if (workspace != null && !WorkspaceClosePrompt.prompt(workspace)) {
				event.consume();
				System.exit(0);
			}
		});
	}

	@Override
	protected Scene createScene() {
		// Content
		SplitPane initialSplit;
		dockingRootPane.setPrefWidth(1080);
		dockingRootPane.createLockedTab(Lang.get("workspace.title"), workspacePanel);
		initialSplit = dockingRootPane.createNewSplit(Orientation.HORIZONTAL, 0.30);
		dockingRootPane.createTab(Lang.get("welcome.title"), new WelcomePanel());
		dockingRootPane.createNewSplit(Orientation.VERTICAL, 0.76);
		dockingRootPane.createLockedTab(Lang.get("logging.title"), LoggingTextArea.getInstance());
		// Mark main content region for new tabs
		DetachableTabPane contentWrapper = (DetachableTabPane) initialSplit.getItems().get(1);
		dockingRootPane.pushRecentTabPane(contentWrapper);
		return new Scene(dockingRootPane);
	}

	/**
	 * @return Docking panel.
	 */
	public DockingRootPane getDockingRootPane() {
		return dockingRootPane;
	}

	/**
	 * @return Panel representing the current workspace.
	 */
	public WorkspacePanel getWorkspacePanel() {
		return workspacePanel;
	}
}

package me.coley.recaf.ui.dialog;

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import me.coley.recaf.ui.util.Icons;
import me.coley.recaf.workspace.resource.Resource;

import java.util.Set;
import java.util.TreeSet;

/**
 * Confirmation dialog that operates off of a {@link #getSelectedDirectory() selected directories} when completed.
 *
 * @author Matt Coley
 */
public class DirectorySelectDialog extends ConfirmDialog {
	private final DirectoryListView directoryList = new DirectoryListView();
	private String currentDirectory;

	/**
	 * @param title
	 * 		Dialog window title.
	 * @param header
	 * 		Header text.
	 * @param graphic
	 * 		Header graphic.
	 */
	public DirectorySelectDialog(String title, String header, Node graphic) {
		super(title, header, graphic);
		GridPane.setHgrow(directoryList, Priority.ALWAYS);
		grid.add(directoryList, 0, 0);
		grid.setPrefWidth(600);
		// Ensure confirmation is only allowed when a new value is provided.
		Node confirmButton = getDialogPane().lookupButton(confirmType);
		confirmButton.setDisable(true);
		directoryList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			confirmButton.setDisable(newValue.trim().isEmpty() || newValue.equals(currentDirectory));
		});
		// Window appears with directories list focused.
		setOnShown(e -> directoryList.requestFocus());
	}

	/**
	 * Populate the directory list.
	 *
	 * @param resource
	 * 		Resource to scan for directories.
	 */
	public void populate(Resource resource) {
		// Collect packages contained in the resource
		Set<String> directories = new TreeSet<>();
		for (String fileName : resource.getFiles().keySet()) {
			int directorySeparator = fileName.lastIndexOf('/');
			if (directorySeparator > 0) {
				String directoryName = fileName.substring(0, directorySeparator);
				directories.add(directoryName);
			}
		}
		directoryList.getItems().clear();
		directoryList.getItems().addAll(directories);
		updateSelection();
	}

	/**
	 * Called to set the initial selection.
	 *
	 * @param currentDirectory
	 * 		Original package of some item.
	 */
	public void setCurrentDirectory(String currentDirectory) {
		this.currentDirectory = currentDirectory;
		updateSelection();
	}

	/**
	 * @return Current package selected by user.
	 */
	public String getSelectedDirectory() {
		return directoryList.getSelectionModel().getSelectedItem();
	}

	private void updateSelection() {
		if (currentDirectory != null && !directoryList.getItems().isEmpty()) {
			directoryList.getSelectionModel().select(currentDirectory);
		}
	}

	private static class DirectoryListView extends ListView<String> {
		private DirectoryListView() {
			setCellFactory(param -> new DirectoryListCell());
		}
	}

	private static class DirectoryListCell extends ListCell<String> {
		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setGraphic(null);
				setText(null);
			} else {
				setGraphic(Icons.getIconView(Icons.FOLDER));
				setText(item);
			}
		}
	}
}

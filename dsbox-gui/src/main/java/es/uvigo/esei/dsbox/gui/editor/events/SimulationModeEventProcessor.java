package es.uvigo.esei.dsbox.gui.editor.events;

import es.uvigo.esei.dsbox.gui.editor.HostNodeView;
import es.uvigo.esei.dsbox.gui.editor.LinkView;
import es.uvigo.esei.dsbox.gui.editor.NetworkView;
import es.uvigo.esei.dsbox.gui.editor.NodeView;
import es.uvigo.esei.dsbox.gui.editor.Position;
import es.uvigo.esei.dsbox.gui.editor.actions.EditActionDestination;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.effect.DropShadow;

public class SimulationModeEventProcessor extends BaseNetworkEventProcessor {

    private NodeView selectedNode;

    public SimulationModeEventProcessor(NetworkView networkView, EditActionDestination actionDestination) {
        super(networkView, actionDestination);
    }

    @Override
    public void onNodeSelected(NetworkNodeEvent ne) {
        selectedNode = ne.getNodeView();

        selectedNode.toFront();
        selectedNode.setCursor(Cursor.CLOSED_HAND);
        selectedNode.setEffect(new DropShadow());
    }

    @Override
    public void onNodeUnselected(NetworkNodeEvent ne) {
        if (selectedNode != null) {
            selectedNode.setCursor(Cursor.DEFAULT);
            selectedNode.setEffect(null);
            selectedNode = null;
        }
    }

    @Override
    public void onNodeClicked(NetworkNodeEvent ne) {
        //onNodeSelected(ne);  // TODO: check equivalence
    }

    @Override
    public void onNodeDoubleClicked(NetworkNodeEvent ne) {
    }

    @Override
    public void onNodeSecondaryClicked(NetworkNodeEvent ne) {

        if (ne.getNodeView() instanceof HostNodeView) {
            HostNodeView hostView = (HostNodeView) ne.getNodeView();

            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setHideOnEscape(true);
            contextMenu.setAutoHide(true);
            contextMenu.setAutoFix(true);
            contextMenu.setConsumeAutoHidingEvents(true);

            MenuItem status = new MenuItem("Host status");
            SeparatorMenuItem separator = new SeparatorMenuItem();
            MenuItem pause = new MenuItem("Pause");
            MenuItem stop = new MenuItem("Stop");

            status.setOnAction((ActionEvent ae) -> {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Status info. for host " + hostView.getHost().getName() + " is not available.", ButtonType.OK);
                alert.setTitle("Warning.");
                alert.setHeaderText("Warning.");
                alert.showAndWait();
            });

            pause.setDisable(true);
            stop.setDisable(true);

            contextMenu.getItems().addAll(status, separator, pause, stop);
            contextMenu.show(hostView, ne.getScreenPosition().getX(), ne.getScreenPosition().getY());
        }
    }

    @Override
    public void onLinkSecondaryClicked(NetworkLinkEvent ne) {
        LinkView link = ne.linkView;
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setHideOnEscape(true);
        contextMenu.setAutoHide(true);
        contextMenu.setAutoFix(true);

        MenuItem undo = new MenuItem("Undo");
        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");

        contextMenu.getItems().addAll(undo, separator, cut, copy, paste);
        Bounds linkBounds = link.getBoundsInParent();

        double menuXoffset = (ne.getPosition().getX() - networkView.getLayoutX()) - linkBounds.getMinX();
        double menuYoffset = (ne.getPosition().getY() - networkView.getLayoutY()) - linkBounds.getMinY();
        contextMenu.show(ne.getLinkView(), Side.LEFT, menuXoffset, menuYoffset);

    }

    @Override
    public void onLinkDoubleClicked(NetworkLinkEvent ne) {
        LinkView link = ne.linkView;
    }

    @Override
    public void onLinkSelected(NetworkLinkEvent ne) {
        LinkView link = ne.linkView;
    }

    @Override
    public void onLinkClicked(NetworkLinkEvent ne) {
        onLinkSelected(ne);  // TODO: check equivalence
    }

    @Override
    public void onCanvasClicked(NetworkEvent ne) {
        if (selectedNode != null) {
            selectedNode = null;
        }
    }

    private void refreshNodePosition(NodeView nv, Position newPosition) {
        networkView.moveNodeView(nv, newPosition);
    }

}

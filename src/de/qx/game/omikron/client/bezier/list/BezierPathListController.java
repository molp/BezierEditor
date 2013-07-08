package de.qx.game.omikron.client.bezier.list;

import de.qx.game.omikron.math.BezierCurve;
import de.qx.game.omikron.math.BezierPath;

/**
 * Date: 16.06.13
 * Time: 08:53
 */
public class BezierPathListController {
    private BezierPathListModel model;

    public BezierPathListController(BezierPathListModel model) {
        this.model = model;
    }

    public void addNewPath() {
        final BezierPathListEntry entry = new BezierPathListEntry("new");
        model.getEntries().add(entry);
    }

    public void removePath(int selectionIndex) {
        model.getEntries().remove(selectionIndex);
    }

    public void duplicatePath(int selectionIndex) {
        final BezierPathListEntry entry = model.getEntries().get(selectionIndex);

        final BezierPathListEntry newEntry = new BezierPathListEntry(entry.getIdentifier() + "_copy");

        BezierPath path = new BezierPath();
        for (int i = 0; i < entry.getPath().getCurveCount(); i++) {
            final BezierCurve curve = entry.getPath().getCurve(i);

            final BezierCurve newCurve = path.addCurve();
            newCurve.setPoint(BezierCurve.BezierPoint.START, curve.getPoint(BezierCurve.BezierPoint.START));
            newCurve.setPoint(BezierCurve.BezierPoint.CONTROL1, curve.getPoint(BezierCurve.BezierPoint.CONTROL1));
            newCurve.setPoint(BezierCurve.BezierPoint.END, curve.getPoint(BezierCurve.BezierPoint.END));
            newCurve.setPoint(BezierCurve.BezierPoint.CONTROL2, curve.getPoint(BezierCurve.BezierPoint.CONTROL2));
        }

        newEntry.setPath(path);

        model.getEntries().add(selectionIndex, newEntry);
    }
}

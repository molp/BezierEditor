package de.qx.game.omikron.client.bezier.editor;

import de.qx.game.omikron.datatype.MutableVector2f;
import de.qx.game.omikron.datatype.Vector2f;
import de.qx.game.omikron.math.BezierCurve;
import de.qx.game.omikron.math.BezierCurve.BezierPoint;
import de.qx.game.omikron.math.BezierPath;
import de.qx.game.omikron.client.bezier.file.BezierPathExporter;

/**
 * Date: 06.06.13
 * Time: 06:49
 */
public class BezierEditorController {

    private BezierEditorModel model;
    private BezierPathExporter exporter;

    public BezierEditorController(BezierPathExporter exporter) {
        this.exporter = exporter;

        model  = new BezierEditorModel();
    }

    public BezierEditorModel getModel() {
        return model;
    }

    public void addCurve() {
        final BezierPath path = model.getPath();
        if (path != null) {
            path.addCurve();
        }
    }

    public void removeCurve() {
        final BezierPath path = model.getPath();
        if(path != null) {
            path.removeCurve();
        }
    }

    public void exportPath(Vector2f offset) {
        System.out.println(exporter.serialize(model.getPath(), offset));
    }

    public void moveControlPoint(int selectedBezierCurve, BezierPoint selectedBezierPoint, Vector2f delta) {
        BezierPath path = model.getPath();
        BezierCurve curve = path.getCurve(selectedBezierCurve);

        // move the selected point
        curve.setPoint(selectedBezierPoint, delta.add(curve.getPoint(selectedBezierPoint)));

        // if we are moving control1 check if there is a previous curve
        if (selectedBezierPoint == BezierPoint.CONTROL1) {
            if (selectedBezierCurve > 0) {
                BezierCurve previousCurve = path.getCurve(selectedBezierCurve - 1);

                MutableVector2f diff = new MutableVector2f(curve.getPoint(BezierPoint.START))
                        .subtract(curve.getPoint(BezierPoint.CONTROL1));

                previousCurve.setPoint(BezierPoint.CONTROL2,
                        new MutableVector2f(previousCurve.getPoint(BezierPoint.END)).add(diff));
            }
        }
        // if we are moving control2 check if there is a following curve
        if (selectedBezierPoint == BezierPoint.CONTROL2) {
            if (selectedBezierCurve < path.getCurveCount() - 1) {
                BezierCurve followingCurve = path.getCurve(selectedBezierCurve + 1);

                MutableVector2f diff = new MutableVector2f(curve.getPoint(BezierPoint.END))
                        .subtract(curve.getPoint(BezierPoint.CONTROL2));

                followingCurve.setPoint(BezierPoint.CONTROL1,
                        new MutableVector2f(followingCurve.getPoint(BezierPoint.START)).add(diff));
            }
        }
    }

    public void moveEndpoint(int selectedBezierCurve, BezierPoint selectedBezierPoint, Vector2f diff) {
        BezierPath path = model.getPath();
        BezierCurve curve = path.getCurve(selectedBezierCurve);

        curve.setPoint(selectedBezierPoint, curve.getPoint(selectedBezierPoint).add(diff));

        if (selectedBezierPoint == BezierPoint.START) {
            if (selectedBezierCurve > 0) {
                BezierCurve previousCurve = path.getCurve(selectedBezierCurve - 1);
                previousCurve.setPoint(BezierPoint.END, curve.getPoint(BezierPoint.START));
            }
        }

        if (selectedBezierPoint == BezierPoint.END) {
            if (selectedBezierCurve < path.getCurveCount() - 1) {
                BezierCurve nextCurve = path.getCurve(selectedBezierCurve + 1);
                nextCurve.setPoint(BezierPoint.START, curve.getPoint(BezierPoint.END));
            }
        }
    }

    public void movePath(Vector2f diff) {
        final BezierPath path = model.getPath();

        if(path == null) return;

        for(int i = 0; i < path.getCurveCount(); i++) {
            BezierCurve curve = path.getCurve(i);

            curve.setPoint(BezierPoint.START, curve.getPoint(BezierPoint.START).add(diff));
            curve.setPoint(BezierPoint.CONTROL1, curve.getPoint(BezierPoint.CONTROL1).add(diff));
            curve.setPoint(BezierPoint.END, curve.getPoint(BezierPoint.END).add(diff));
            curve.setPoint(BezierPoint.CONTROL2, curve.getPoint(BezierPoint.CONTROL2).add(diff));
        }
    }

    public void mirrorHorizontal(float axis) {
        final BezierPath path = model.getPath();

        if(path == null) return;

        for(int i = 0; i < path.getCurveCount(); i++) {
            BezierCurve curve = path.getCurve(i);

            Vector2f point = curve.getPoint(BezierPoint.START);
            curve.setPoint(BezierPoint.START, new Vector2f(mirror(point.x(), axis), point.y()));

            point = curve.getPoint(BezierPoint.CONTROL1);
            curve.setPoint(BezierPoint.CONTROL1, new Vector2f(mirror(point.x(), axis), point.y()));

            point = curve.getPoint(BezierPoint.END);
            curve.setPoint(BezierPoint.END, new Vector2f(mirror(point.x(), axis), point.y()));

            point = curve.getPoint(BezierPoint.CONTROL2);
            curve.setPoint(BezierPoint.CONTROL2, new Vector2f(mirror(point.x(), axis), point.y()));
        }
    }

    private float mirror(float a, float b) {
        return a + 2 * (b - a);
    }

    public void mirrorVertical() {

    }
}

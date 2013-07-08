package de.qx.game.omikron.client.bezier.editor;

import de.qx.game.omikron.datatype.MutableVector2f;
import de.qx.game.omikron.datatype.Vector2f;
import de.qx.game.omikron.math.BezierCurve;
import de.qx.game.omikron.math.BezierCurve.BezierPoint;
import de.qx.game.omikron.math.BezierPath;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class BezierDragAndDropListener implements MouseListener, MouseMotionListener {

	private BezierPoint selectedBezierPoint;
	private int selectedBezierCurve = -1;

	private final DrawCallback drawCallback;
    private JComponent focus;
    private Vector2f offset;
    private BezierEditorController controller;
    private BezierEditorModel model;

    private Vector2f dragStart;
    private Vector2f dragLast;
    private int mouseButton;

    public BezierDragAndDropListener(BezierEditorController controller, BezierEditorModel model, DrawCallback drawCallback, JComponent focus, Vector2f offset) {
        this.controller = controller;
        this.model = model;
    	this.drawCallback = drawCallback;
        this.focus = focus;
        this.offset = offset;

        this.dragStart = new MutableVector2f(0f, 0f);
    }

	@Override
	public void mouseClicked(MouseEvent event) {
	}

	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent event) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
        dragStart = new Vector2f(event.getX(), event.getY());
        dragLast = new Vector2f(event.getX(), event.getY());
        mouseButton = event.getButton();

        BezierPath path = model.getPath();
        if(path == null) return;

		// find out if we clicked on something
		Vector2f v = new Vector2f(event.getX()-offset.x(), event.getY()-offset.y());
		selectedBezierPoint = null;
		selectedBezierCurve = -1;

		for (int i = 0; i < path.getCurveCount(); i++) {
			BezierCurve curve = path.getCurve(i);

			if (curve.getPoint(BezierPoint.CONTROL1).distance(v) <= 4f) {
				selectedBezierPoint = BezierPoint.CONTROL1;
				selectedBezierCurve = i;
			} else if (curve.getPoint(BezierPoint.CONTROL2).distance(v) <= 4f) {
				selectedBezierPoint = BezierPoint.CONTROL2;
				selectedBezierCurve = i;
			} else if (curve.getPoint(BezierPoint.START).distance(v) <= 4f) {
				selectedBezierPoint = BezierPoint.START;
				selectedBezierCurve = i;
			} else if (curve.getPoint(BezierPoint.END).distance(v) <= 4f) {
				selectedBezierPoint = BezierPoint.END;
				selectedBezierCurve = i;
			}
		}
		System.out.println(String.format("Selected Bezier Point '%s' on curve '%s'", selectedBezierPoint, selectedBezierCurve));

        focus.requestFocus();
	}

	@Override
	public void mouseReleased(MouseEvent event) {
	}

	@Override
	public void mouseDragged(MouseEvent event) {
        BezierPath path = model.getPath();

        if(mouseButton == MouseEvent.BUTTON1) {
            if (selectedBezierPoint != null && selectedBezierCurve > -1) {
                BezierCurve curve = path.getCurve(selectedBezierCurve);

                final int x = (int)(event.getX() - offset.x());
                final int y = (int)(event.getY() - offset.y());

                Vector2f diff = new Vector2f(x, y).subtract(curve.getPoint(selectedBezierPoint));

                switch (selectedBezierPoint) {
                case CONTROL1:
                case CONTROL2:
                    controller.moveControlPoint(selectedBezierCurve, selectedBezierPoint, diff);
                    break;
                case END:
                    controller.moveEndpoint(selectedBezierCurve, selectedBezierPoint, diff);
                    controller.moveControlPoint(selectedBezierCurve, BezierPoint.CONTROL2, diff);
                    break;
                case START:
                    controller.moveEndpoint(selectedBezierCurve, selectedBezierPoint, diff);
                    controller.moveControlPoint(selectedBezierCurve, BezierPoint.CONTROL1, diff);
                    break;
                }
                drawCallback.paintIt();
            }
        } else if(mouseButton == MouseEvent.BUTTON3) {
            final Vector2f diff = new Vector2f(event.getX(), event.getY()).subtract(dragLast);
            controller.movePath(diff);

            drawCallback.paintIt();
        }

        dragLast = new Vector2f(event.getX(), event.getY());
	}




	@Override
	public void mouseMoved(MouseEvent event) {
	}
}

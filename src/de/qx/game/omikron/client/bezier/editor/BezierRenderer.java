package de.qx.game.omikron.client.bezier.editor;

import de.qx.game.omikron.client.bezier.PrimitiveRenderer;
import de.qx.game.omikron.datatype.Vector2f;
import de.qx.game.omikron.math.BezierCurve;
import de.qx.game.omikron.math.BezierCurve.BezierPoint;
import de.qx.game.omikron.math.BezierPath;

public class BezierRenderer {

	private static final int COLOR_ACTIVE_HELP_POINT = 0xA0A0A0;
	private static final int COLOR_INACTIVE_HELP_POINT = 0x707070;
	private static final int COLOR_END_POINT = 0xE00F0F;
	private static final int COLOR_START_POINT = 0x0FE00F;
	private static final int COLOR_ACTIVE_LINE = 0xA0A0A0;
	private static final int COLOR_INACTIVE_LINE = 0x707070;
	private static final int COLOR_ACTIVE_CURVE = 0xEE11EE;
	private static final int COLOR_INACTIVE_CURVE = 0x5522AA;
	
	private int intermediatePoints = 150;

    private PrimitiveRenderer renderer;
	private BezierEditorModel model;
	
	public BezierRenderer(BezierEditorModel model, PrimitiveRenderer primitiveRenderer){
		this.model = model;
		renderer = primitiveRenderer;
	}
	
	public void render(int[] data){
        BezierPath path = model.getPath();
        if(path == null) return;

		float length = path.getTotalCurveLength();
		int divisions = (int) (length / 10f);
		
		Vector2f lastPoint = path.getPointAtDistance(0f);
		float distance = 0f;
		for(int i = 0; i< divisions; i++){
			distance = distance + 10f;
			final Vector2f point = path.getPointAtDistance(distance);
			renderer.drawLine(data, lastPoint, point, i%2==0 ? COLOR_ACTIVE_CURVE : COLOR_INACTIVE_CURVE);
			lastPoint = point;
		}
		
		for(int c = 0; c < path.getCurveCount(); c++){
			BezierCurve curve = path.getCurve(c);

			renderer.drawLine(data, curve.getPoint(BezierPoint.START), curve.getPoint(BezierPoint.CONTROL1), COLOR_ACTIVE_LINE);
			renderer.drawLine(data, curve.getPoint(BezierPoint.END), curve.getPoint(BezierPoint.CONTROL2), COLOR_ACTIVE_LINE);
			
			renderer.drawPoint(data, curve.getPoint(BezierPoint.START), COLOR_START_POINT);
			renderer.drawPoint(data, curve.getPoint(BezierPoint.END), COLOR_END_POINT);
			
			renderer.drawPoint(data, curve.getPoint(BezierPoint.CONTROL1), COLOR_ACTIVE_HELP_POINT);
			renderer.drawPoint(data, curve.getPoint(BezierPoint.CONTROL2), COLOR_ACTIVE_HELP_POINT);		
		}
	}
	
	public void increasePoints() {
		intermediatePoints++;
		System.out.println("intermediate points: " + intermediatePoints);		
	}

	public void decreasePoints() {
		intermediatePoints--;
		if (intermediatePoints < 10) {
			intermediatePoints = 10;
		}
		System.out.println("intermediate points: " + intermediatePoints);
	}
}

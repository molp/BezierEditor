package de.qx.game.omikron.client.bezier.file;

import de.qx.game.omikron.math.BezierCurve;
import de.qx.game.omikron.math.BezierCurve.BezierPoint;
import de.qx.game.omikron.datatype.MutableVector2f;
import de.qx.game.omikron.datatype.Vector2f;
import de.qx.game.omikron.math.BezierPath;

public class BezierPathExporter {

    private boolean invert = false;
    private float height = 0f;
    private float scale = 1f;

    public BezierPathExporter(boolean invert, float height, float scale) {
        this.invert = invert;
        this.height = height;
        this.scale = scale;
    }

    public BezierPathExporter(boolean invert, float height) {
        this.invert = invert;
        this.height = height;
    }

    public BezierPathExporter(float scale) {
        this.scale = scale;
    }

    public BezierPathExporter() {

    }

	/**
	 * Serializes the given {@link de.qx.game.omikron.math.BezierPath} into a {@link String}.
	 *
     * @param offset
     *            a offset that is being added to every path coordinate
     *
     * @return the serialized BezierPath as String
     */
	public String serialize(BezierPath path, Vector2f offset) {
		StringBuilder builder = new StringBuilder();
        builder.append(path.getCurveCount()).append(";");
        builder.append(invert).append(";");
		for (int i = 0; i < path.getCurveCount(); i++) {
			BezierCurve curve = path.getCurve(i);

			float x = (curve.getPoint(BezierPoint.START).x() - offset.x()) * scale;
			float y = invert((curve.getPoint(BezierPoint.START).y() - offset.y()) * scale);
			builder.append(x).append(";").append(y).append(";");

			x = (curve.getPoint(BezierPoint.CONTROL1).x() - offset.x()) * scale;
			y = invert((curve.getPoint(BezierPoint.CONTROL1).y() - offset.y()) * scale);
			builder.append(x).append(";").append(y).append(";");

			x = (curve.getPoint(BezierPoint.END).x() - offset.x()) * scale;
			y = invert((curve.getPoint(BezierPoint.END).y() - offset.y()) * scale);
			builder.append(x).append(";").append(y).append(";");

			x = (curve.getPoint(BezierPoint.CONTROL2).x() - offset.x()) * scale;
			y = invert((curve.getPoint(BezierPoint.CONTROL2).y() - offset.y()) * scale);
			builder.append(x).append(";").append(y).append(";");
		}

		return builder.toString();
	}

	/**
	 * Deserializes the given {@link String} into a {@link BezierPath}.
	 * 
	 * @param offset
	 *            an offset that is subtracted after scaling.
	 * @param scale
	 *            a scale factor that is multiplied with each component. If you
	 *            serialize with 2f you can use the same factor again. 1f /
	 *            scale is used here!
	 *
     * @return the deserialized BezierPath
	 */
	public BezierPath deserialize(String serializedValue, Vector2f offset, float scale) {
		String[] strings = serializedValue.split(";");

		BezierPath path = new BezierPath();

		int count = Integer.valueOf(strings[0]);
        boolean curveInvert = Boolean.valueOf(strings[1]);
		int index = 2;
		for (int i = 0; i < count; i++) {
			BezierCurve curve = path.addCurve();
			MutableVector2f vec = new MutableVector2f(0f, 0f);

			vec.setX(Float.valueOf(strings[index++]) * 1f / scale + offset.x());
			vec.setY(invert(Float.valueOf(strings[index++]) * 1f / scale + offset.y(), curveInvert));
			curve.setPoint(BezierPoint.START, vec);

			vec.setX(Float.valueOf(strings[index++]) * 1f / scale + offset.x());
			vec.setY(invert(Float.valueOf(strings[index++]) * 1f / scale + offset.y(), curveInvert));
			curve.setPoint(BezierPoint.CONTROL1, vec);

			vec.setX(Float.valueOf(strings[index++]) * 1f / scale + offset.x());
			vec.setY(invert(Float.valueOf(strings[index++]) * 1f / scale + offset.y(), curveInvert));
			curve.setPoint(BezierPoint.END, vec);

			vec.setX(Float.valueOf(strings[index++]) * 1f / scale + offset.x());
			vec.setY(invert(Float.valueOf(strings[index++]) * 1f / scale + offset.y(), curveInvert));
			curve.setPoint(BezierPoint.CONTROL2, vec);
		}

		return path;
	}

    private float invert(float y) {
        return invert ? height - y : y;
    }

    private float invert(float y, boolean invert) {
        return invert ? height - y : y;
    }
}

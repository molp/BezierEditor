package de.qx.game.omikron.client.bezier.file;

import de.qx.game.omikron.math.BezierCurve;
import de.qx.game.omikron.math.BezierCurve.BezierPoint;
import de.qx.game.omikron.datatype.MutableVector2f;
import de.qx.game.omikron.datatype.Vector2f;
import de.qx.game.omikron.math.BezierPath;

public class BezierPathExporter {

	/**
	 * Serializes the given {@link de.qx.game.omikron.math.BezierPath} into a {@link String}.
	 * 
	 * @param offset
	 *            a offset that is being added to every path coordinate
	 * @param scale
	 *            a scaling factor that is being multiplied to every path
	 *            coordinate
	 */
	public String serialize(BezierPath path, Vector2f offset, float scale) {
		StringBuilder builder = new StringBuilder();
		builder.append(path.getCurveCount()).append(";");
		for (int i = 0; i < path.getCurveCount(); i++) {
			BezierCurve curve = path.getCurve(i);

			float x = (curve.getPoint(BezierPoint.START).x() - offset.x()) * scale;
			float y = (curve.getPoint(BezierPoint.START).y() - offset.y()) * scale;
			builder.append(x).append(";").append(y).append(";");

			x = (curve.getPoint(BezierPoint.CONTROL1).x() - offset.x()) * scale;
			y = (curve.getPoint(BezierPoint.CONTROL1).y() - offset.y()) * scale;
			builder.append(x).append(";").append(y).append(";");

			x = (curve.getPoint(BezierPoint.END).x() - offset.x()) * scale;
			y = (curve.getPoint(BezierPoint.END).y() - offset.y()) * scale;
			builder.append(x).append(";").append(y).append(";");

			x = (curve.getPoint(BezierPoint.CONTROL2).x() - offset.x()) * scale;
			y = (curve.getPoint(BezierPoint.CONTROL2).y() - offset.y()) * scale;
			builder.append(x).append(";").append(y).append(";");
		}

		return builder.toString();
	}

	/*public BezierPath deserialize(String serializedPath) {
		String[] strings = serializedPath.split(";");

		BezierPath path = new BezierPath();

		int count = Integer.valueOf(strings[0]);
		int index = 1;
		for (int i = 0; i < count; i++) {
			BezierCurve curve = path.addCurve();
			MutableVector2f vec = new MutableVector2f(0f, 0f);

			vec.setX(Float.valueOf(strings[index++]));
			vec.setY(Float.valueOf(strings[index++]));
			curve.setPoint(BezierPoint.START, vec);

			vec.setX(Float.valueOf(strings[index++]));
			vec.setY(Float.valueOf(strings[index++]));
			curve.setPoint(BezierPoint.CONTROL1, vec);

			vec.setX(Float.valueOf(strings[index++]));
			vec.setY(Float.valueOf(strings[index++]));
			curve.setPoint(BezierPoint.END, vec);

			vec.setX(Float.valueOf(strings[index++]));
			vec.setY(Float.valueOf(strings[index++]));
			curve.setPoint(BezierPoint.CONTROL2, vec);
		}

		return path;
	}*/

	/**
	 * Deserializes the given {@link String} into a {@link BezierPath}.
	 * 
	 * @param offset
	 *            an offset that is subtracted after scaling.
	 * @param scale
	 *            a scale factor that is multiplied with each component. If you
	 *            serialize with 2f you can use the same factor again. 1f /
	 *            scale is used here!
	 * @return
	 */
	public BezierPath deserialize(String serializedValue, Vector2f offset, float scale) {
		String[] strings = serializedValue.split(";");

		BezierPath path = new BezierPath();

		int count = Integer.valueOf(strings[0]);
		int index = 1;
		for (int i = 0; i < count; i++) {
			BezierCurve curve = path.addCurve();
			MutableVector2f vec = new MutableVector2f(0f, 0f);

			vec.setX(Float.valueOf(strings[index++]) * 1f / scale + offset.x());
			vec.setY(Float.valueOf(strings[index++]) * 1f / scale + offset.y());
			curve.setPoint(BezierPoint.START, vec);

			vec.setX(Float.valueOf(strings[index++]) * 1f / scale + offset.x());
			vec.setY(Float.valueOf(strings[index++]) * 1f / scale + offset.y());
			curve.setPoint(BezierPoint.CONTROL1, vec);

			vec.setX(Float.valueOf(strings[index++]) * 1f / scale + offset.x());
			vec.setY(Float.valueOf(strings[index++]) * 1f / scale + offset.y());
			curve.setPoint(BezierPoint.END, vec);

			vec.setX(Float.valueOf(strings[index++]) * 1f / scale + offset.x());
			vec.setY(Float.valueOf(strings[index++]) * 1f / scale + offset.y());
			curve.setPoint(BezierPoint.CONTROL2, vec);
		}

		return path;
	}
}

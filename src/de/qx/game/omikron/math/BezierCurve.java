package de.qx.game.omikron.math;

import de.qx.game.omikron.datatype.MutableVector2f;
import de.qx.game.omikron.datatype.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve {

	public enum BezierPoint {
		START, CONTROL1, CONTROL2, END
	}

	private Vector2f[] points;

	private final List<Vector2f> segments;
	private float[] cumulatedLength;

	public BezierCurve(Vector2f... points) {
		this.points = new Vector2f[4];
		for (int i = 0; i < points.length; i++) {
			this.points[i] = new Vector2f(points[i]);
		}
		segments = new ArrayList<Vector2f>();

		calculateSegments();
	}

	public Vector2f getPoint(BezierPoint point) {
		return points[point.ordinal()];
	}

	public void setPoint(BezierPoint point, Vector2f vector) {
		points[point.ordinal()] = new Vector2f(vector);

		calculateSegments();
	}

	public List<Vector2f> getSegments() {
		return segments;
	}

	public float getCumulatedLengthAtSegment(int segment) {
		return cumulatedLength[segment];
	}

	public int getSegmentCount() {
		return segments.size();
	}

	public void calculateSegments() {
		// estimate the distance of the curve by adding up the distances between all the points
		int subdivisionCount = estimateSubdivisionCount();

		cumulatedLength = new float[subdivisionCount + 1];
		segments.clear();

		for (int i = 0; i <= subdivisionCount; i++) {
			float t = 1f / subdivisionCount * i;

			float oneMinusT = 1f - t;
			float oneMinusT2 = oneMinusT * oneMinusT;
			float oneMinusT3 = oneMinusT2 * oneMinusT;

			MutableVector2f v = new MutableVector2f(0f, 0f);

			MutableVector2f part1 = new MutableVector2f(points[0]).multiplyBy(oneMinusT3);
			MutableVector2f part2 = new MutableVector2f(points[1]).multiplyBy(3).multiplyBy(oneMinusT2).multiplyBy(t);
			MutableVector2f part3 = new MutableVector2f(points[2]).multiplyBy(3).multiplyBy(oneMinusT).multiplyBy(t * t);
			MutableVector2f part4 = new MutableVector2f(points[3]).multiplyBy(t * t * t);

			v.add(part1).add(part2).add(part3).add(part4);

			segments.add(v);
		}

		cumulatedLength[0] = 0f;
		for (int i = 1; i < segments.size(); i++) {
			cumulatedLength[i] = cumulatedLength[i - 1] + segments.get(i - 1).distance(segments.get(i));
		}
		int lastIndex = segments.size() - 1;
		cumulatedLength[lastIndex] = cumulatedLength[lastIndex] + segments.get(lastIndex).distance(getPoint(BezierPoint.END));
	}

	protected int estimateSubdivisionCount() {
		float distance = 0f;
		distance += points[0].distance(points[1]);
		distance += points[1].distance(points[2]);
		distance += points[3].distance(points[3]);
        return (int) distance / 2;
	}

}

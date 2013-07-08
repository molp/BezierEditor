package de.qx.game.omikron.math;

import de.qx.game.omikron.datatype.MutableVector2f;
import de.qx.game.omikron.datatype.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class BezierPath {

	private final List<BezierCurve> curves;
	
	/**
	 * C'tor
	 */
	public BezierPath(){
		curves = new ArrayList<BezierCurve>();
	}

    public Vector2f getPoint(int curve, BezierCurve.BezierPoint point){
        return curves.get(curve).getPoint(point);
    }
	
	public BezierCurve getCurve(int curve){
		return curves.get(curve);
	}
	
	public int getCurveCount(){
		return curves.size();
	}
	
	public float getTotalCurveLength(){
		float sum = 0f;

		for(int i = 0; i < curves.size(); i++) {
            BezierCurve curve = curves.get(i);
			sum += curve.getCumulatedLengthAtSegment(curve.getSegmentCount()-1);
		}
		return sum;
	}
	
	public Vector2f getPointAtDistance(float distance){
		// if the distance is negative return the start point of the first curve
		if(distance <= 0){
			return curves.get(0).getPoint(BezierCurve.BezierPoint.START);
		}
		
		// find out in which curve the distance lies
		BezierCurve curve = null;
		float offset = 0f;
		for(int i = 0; i < curves.size(); i++){
			final BezierCurve curveAtI = curves.get(i);
			if(curveAtI.getCumulatedLengthAtSegment(curveAtI.getSegmentCount()-1) + offset >= distance){
				curve = curveAtI;
				break;
			}
			offset += curveAtI.getCumulatedLengthAtSegment(curveAtI.getSegmentCount()-1);
		}
		
		// the distance was greater than the last curve's accumulated length -> return the last point of the path 
		if(curve == null){
			return curves.get(curves.size()-1).getPoint(BezierCurve.BezierPoint.END);
		}
		
		// calculate the curve-local distance to find a point for
		float localDistance = distance - offset;
		
		// find the segment in which we have to look for the point 
		int segment = -1;
		for(int i = 0; i < curve.getSegmentCount(); i++){
			if(curve.getCumulatedLengthAtSegment(i) > localDistance){
				segment = i;
				break;
			}
		}
		
		if(segment == -1){
			segment = curve.getSegmentCount()-1;
		}
		
		// interpolate between the points of segment i and i-1
		float distanceToSegmentMinusOne = curve.getCumulatedLengthAtSegment(segment-1) - localDistance;
		float distanceToSegment = curve.getCumulatedLengthAtSegment(segment) - localDistance;
		float segmentLength = distanceToSegment - distanceToSegmentMinusOne;
		
		final Vector2f b = curve.getSegments().get(segment);
		final Vector2f a = curve.getSegments().get(segment-1);
		MutableVector2f direction = new MutableVector2f(b).subtract(a);
        direction.multiplyBy((segmentLength - distanceToSegment) / segmentLength);
		
		return a.add(direction);
	}
	
	public BezierCurve addCurve(){
		if(curves.size() == 0){
			curves.add(new BezierCurve(
					new MutableVector2f(90, 90), 
					new MutableVector2f(90, 300), 
					new MutableVector2f(300, 300),
					new MutableVector2f(300, 90)));
			return curves.get(curves.size()-1);
		}

        BezierCurve lastCurve = curves.get(curves.size()-1);
        Vector2f end = lastCurve.getPoint(BezierCurve.BezierPoint.END);
        BezierCurve newCurve = new BezierCurve(
                end,
                end.add(new Vector2f(50, 50)),
                end.add(new Vector2f(100, 50)),
                end.add(new Vector2f(100, 100)));

        curves.add(newCurve);

        return newCurve;

	}

	public void removeCurve() {
		curves.remove(curves.size()-1);
	}
}

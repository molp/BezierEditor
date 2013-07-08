package de.qx.game.omikron.datatype;

public class MutableVector2f extends Vector2f{

    public static final MutableVector2f ZERO = new MutableVector2f(0f, 0f);

	private float x;
    private float y;

    public MutableVector2f(float x, float y){
        this.x = x;
        this.y = y;
    }

    public MutableVector2f(Vector2f copy) {
		this.x = copy.x();
		this.y = copy.y();
	}

    public MutableVector2f addDelta(Vector2f delta) {
    	x += delta.x();
    	y += delta.y();
    	return this;
    }

    public MutableVector2f addDelta(float dx, float dy){
    	this.x += dx;
    	this.y += dy;

    	return this;
    }

	public MutableVector2f addDeltaX(float delta){
    	x += delta;
    	return this;
    }

    public MutableVector2f addDeltaY(float delta){
    	y += delta;
    	return this;
    }

	/**
	 * Adds the given {@link Vector2f} to <b>this</b> {@link Vector2f}.
	 *
	 * @return <b>this</b>
	 */
	public MutableVector2f add(Vector2f v){
		x += v.x();
		y += v.y();

		return this;
	}

	/**
	 * Subtracts the given {@link Vector2f} to <b>this</b> {@link Vector2f}.
	 *
	 * @return <b>this</b>
	 */
	public MutableVector2f subtract(Vector2f v){
		x -= v.x();
		y -= v.y();

		return this;
	}

    public MutableVector2f multiplyBy(float t){
    	x *= t;
    	y *= t;
    	
    	return this;
    }
    
    public float length() {
    	return (float) Math.sqrt(x*x + y*y);
    }
    
    public MutableVector2f normalize() {
    	multiplyBy(1f / length());		
    	return this;
    }

    @Override
    public String toString() {
        return String.format("x=%s y=%s", x(), y());
    }

	public float x() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float y() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void setXY(float x, float y){
		this.x = x;
		this.y = y;
	}

}

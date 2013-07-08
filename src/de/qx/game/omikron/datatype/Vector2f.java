package de.qx.game.omikron.datatype;

public class Vector2f {
	public static final Vector2f ZERO = new Vector2f(0f, 0f);

	private final float x;
	private final float y;

	public Vector2f(){
		this.x = 0f;
		this.y = 0f;
	}

	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vector2f v) {
		this.x = v.x();
		this.y = v.y();
	}

	public float distance(Vector2f v) {
		return (float) Math.sqrt((v.x()-x())*(v.x()-x()) + (v.y()-y())*(v.y()-y()));
	}

	/**
	 * Adds the given {@link de.qx.game.omikron.datatype.Vector2f} to a <b>new</b> {@link de.qx.game.omikron.datatype.Vector2f}.
	 *
	 * @return <b>new {@link de.qx.game.omikron.datatype.Vector2f}</b>
	 */
	public Vector2f add(Vector2f v){
		return new Vector2f(this.x + v.x(), this.y + v.y());
	}
	
	/**
	 * Subtracts the given {@link de.qx.game.omikron.datatype.Vector2f} from this and returns a <b>new</b> {@link de.qx.game.omikron.datatype.Vector2f}.
	 *
	 * @return <b>new {@link de.qx.game.omikron.datatype.Vector2f}</b>
	 */
	public Vector2f subtract(Vector2f point) {
		return new Vector2f(this.x - point.x(), this.y - point.y());
	}
	
	/**
	 * Returns a <b>mutable copy</b> of this {@link de.qx.game.omikron.datatype.Vector2f}.
	 *
	 * @return a mutable copy!
	 */
	public MutableVector2f getMutableVector(){
		return new MutableVector2f(this);
	}

	public float x() {
		return x;
	}

	public float y() {
		return y;
	}

	public String toString() {
		return "[" + x() + ", " + y() + "]";
	}

}

package de.qx.game.omikron.client.bezier;

import de.qx.game.omikron.datatype.Vector2f;

public class PrimitiveRenderer {

	public static final int POINTSIZE = 8;
	
	private final int width;
	private final int height;

    private Vector2f offset;

    public PrimitiveRenderer(int width, int height, Vector2f offset){
		this.width = width;
		this.height = height;
        this.offset = offset;
    }

	public void drawPoint(int [] data, Vector2f point, int color){
		for(int j = -POINTSIZE/2; j < POINTSIZE/2; j++){
			for(int i = -POINTSIZE/2; i < POINTSIZE/2; i++){
				int x = (int) (point.x() + i + offset.x());
				int y = (int) (point.y() + j + offset.y());
				if(x < 0 || y < 0 ||  x >= width || y >= height){
					continue;
				}
				
				data[x + y * width] = color;
			}
		}
	}
	
	public void drawRect(int [] data, Vector2f upperLeft, Vector2f size, int color){
		Vector2f v1 = new Vector2f(upperLeft.x(), upperLeft.y());
		Vector2f v2 = new Vector2f(upperLeft.x() + size.x(), upperLeft.y());
		
		drawLine(data, v1, v2, color);
		
		v1 = new Vector2f(upperLeft.x() + size.x(), upperLeft.y() + size.y());
		
		drawLine(data, v2, v1, color);
		
		v2 = new Vector2f(upperLeft.x(), upperLeft.y() + size.y());
		
		drawLine(data, v1, v2, color);
		
		v1 = new Vector2f(upperLeft.x(), upperLeft.y());
		
		drawLine(data, v2, v1, color);
	}
	
	public void drawLine(int [] data, Vector2f start, Vector2f end, int color){
		int x = (int) (start.x() + offset.x());
		int y = (int) (start.y() + offset.y());
		int x2 = (int) (end.x() + offset.x());
		int y2 = (int) (end.y() + offset.y());
		
		int w = x2 - x;
	    int h = y2 - y;
	    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
	    if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
	    if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
	    if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
	    int longest = Math.abs(w) ;
	    int shortest = Math.abs(h) ;
	    if (!(longest>shortest)) {
	        longest = Math.abs(h) ;
	        shortest = Math.abs(w) ;
	        if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
	        dx2 = 0 ;            
	    }
	    int numerator = longest >> 1 ;
	    for (int i=0;i<=longest;i++) {
	        data[x + y * width] = color;
	        numerator += shortest ;
	        if (!(numerator<longest)) {
	            numerator -= longest ;
	            x += dx1 ;
	            y += dy1 ;
	        } else {
	            x += dx2 ;
	            y += dy2 ;
	        }
	    }
	}
}

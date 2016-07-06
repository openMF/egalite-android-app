package com.bfsi.egalite.support;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * @author Ashish
 *
 */
public class customBorderDrawable extends ShapeDrawable {
	Paint fillpaint, strokepaint;
	private static final int WIDTH = 3;

	public void RoundRect(Shape s) 
	{
		fillpaint = this.getPaint();
		strokepaint = new Paint(fillpaint);
		strokepaint.setStyle(Paint.Style.STROKE);
		strokepaint.setStrokeWidth(WIDTH);
		strokepaint.setARGB(255, 0, 0, 0);
	}

	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint fillpaint) {
		shape.draw(canvas, fillpaint);
		shape.draw(canvas, strokepaint);
	}

	public void setFillColour(int c) {
		fillpaint.setColor(c);
	}

}

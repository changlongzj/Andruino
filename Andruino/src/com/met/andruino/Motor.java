package com.met.andruino;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

public class Motor extends ImageView {

	private Bitmap background;
	private Bitmap indicator;
	private int potencia=6;
	
	public Motor(Context context) {
		super(context);
		initialization(context);
		// TODO Auto-generated constructor stub
	}
	public Motor(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialization(context);
	}

	public Motor(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialization(context);
	}
	
	private void initialization(Context context) {
		BitmapFactory.Options options;

		try {
			background = BitmapFactory.decodeResource(context.getResources(), R.drawable.slider_master);
		}
		catch (OutOfMemoryError o) {
			try {
				options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				background = BitmapFactory.decodeResource(context.getResources(), R.drawable.slider_master, options);
			}
			catch (Exception e) {
				 Log.v("Error","Motor.java - Memoria");
			}
		}
	
		
		try {
			indicator = BitmapFactory.decodeResource(context.getResources(), R.drawable.slider_master_handler);
		}
		catch (OutOfMemoryError o) {
			try {
				options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				indicator = BitmapFactory.decodeResource(context.getResources(), R.drawable.slider_master_handler, options);
			}
			catch (Exception e) {
				 Log.v("Error","Motor.java - Memoria");
			}
		}
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldh, oldw);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int desiredWidth = 75;
		int desiredHeight = 300;
		boolean heightFixed = false;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = desiredWidth;
		int height = desiredHeight;

		// Measure Width
		if (widthMode == MeasureSpec.EXACTLY) {
			// Must be this size
			width = widthSize;
		}
		else {
			if (widthMode == MeasureSpec.AT_MOST) {
				// Can't be bigger than...
				width = Math.min(desiredWidth, widthSize);
				height = width;
				heightFixed = true;
			}
			// Sinó,tindrà la mida per defecte

		}

		// Measure Height
		if (heightFixed == false) {

			if (heightMode == MeasureSpec.EXACTLY) {
				// Must be this size
				height = heightSize;
			}
			else {
				if (heightMode == MeasureSpec.AT_MOST) {
					// Can't be bigger than...
					height = Math.min(desiredHeight, heightSize);
				}
				// Sinó,tindrà la mida per defecte
			}
		}

		// MUST CALL THIS
		setMeasuredDimension(width, height);
	}
	
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		int width = getWidth();
		int height = getHeight();

		final float x = width * 0.5f;
		final float y = height * 0.5f;
		float step=height/13;//num de steps dibuixats
		
		
		background = resizeImage(background, width, height);
		indicator = resizeImage(indicator, width, height);
		canvas.drawColor(Color.TRANSPARENT);
		canvas.drawBitmap(background, 12, 0, null);
		canvas.drawBitmap(indicator, 0, (potencia)*step, null);
		Log.v("DEBUG","Steps:"+Float.toString(step));
		Log.v("DEBUG","Dibuixem a:"+Float.toString((potencia)*step));
	}
	
	
	
public Bitmap resizeImage(Bitmap image, int maxWidth, int maxHeight) {
		
		Bitmap resizedImage = null;
		
		try {
			//Alçada de la imatge
			int imageHeight = image.getHeight();

			//Si la imatge és més alta que el màxim, l'alçada serà el màxim.
			if (imageHeight > maxHeight)
				imageHeight = maxHeight;
			//L'amplada mantindrà la relació d'aspecte
			int imageWidth = (imageHeight * image.getWidth()) / image.getHeight();

			//Si la imatge és més ample que el màxim, l'amplada serà el màxim.
			if (imageWidth > maxWidth) {
				imageWidth = maxWidth;
				imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
			}

			if (imageHeight > maxHeight)
				imageHeight = maxHeight;
			if (imageWidth > maxWidth)
				imageWidth = maxWidth;

			resizedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
		}
		catch (OutOfMemoryError e) {

			e.printStackTrace();
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return resizedImage;
	}


	public void setPotencia(int potencia){
		this.potencia=6-((int)((float)potencia/100*6));
		Log.v("DEBUG","Step ser‡:"+Integer.toString(this.potencia));
	}

}

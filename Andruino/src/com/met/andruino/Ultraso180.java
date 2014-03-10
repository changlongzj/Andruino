package com.met.andruino;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Genera el component que mostra els valors de l'ultrasò en una gràfica de 180º.
 * Els punts són equidistants.
 * @author Aaron
 *
 */
public class Ultraso180 extends ImageView {

	// Número de punts de la gràfica
	public final static int numberPoints = 5;
	// Increment d'angle de cada punt de la gràfica (en radiants)
	public final static double angleStep = (float) (Math.PI / ((double) numberPoints-1));	//Vigilar si hi ha 1 sol punt!

	// Valor dels punts a pintar
	int[] distanceValues = new int[numberPoints];

	// Valor de l'offset (Perquè al centre de la gràfica no es pinta!). S'actualitza a onMeasure
	int roundOffset;

	// Configuracions dels punts i les línies
	Paint pPoint;
	Paint pLines;

	// Imatge de fons del component
	Bitmap background;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public Ultraso180(Context context) {
		super(context);
		initialization(context);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public Ultraso180(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialization(context);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public Ultraso180(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialization(context);
	}

	/**
	 * Inicialització de la classe
	 * 
	 * @param context
	 *            : Context
	 */
	private void initialization(Context context) {

		// Inicialitzem les variables
		pPoint = new Paint(); // Per pintar els punts de la gràfica
		pLines = new Paint(); // Per pintar les línies de la gràfica

		pPoint.setAntiAlias(true);
		pPoint.setColor(Color.RED);
		pPoint.setStyle(Paint.Style.FILL); // Pintarem cercles. Estaran pintats per dins

		pLines.setAntiAlias(true);
		pLines.setColor(Color.BLUE);
		pLines.setStyle(Paint.Style.STROKE);
		pLines.setStrokeWidth(2f); // Amplada de la línia

		// Carreguem el fons. El redimensionem si no hi ha prou memòria
		BitmapFactory.Options options;
		try {
			background = BitmapFactory.decodeResource(context.getResources(), R.drawable.ultraso_180_master);
		}
		catch (OutOfMemoryError o) {
			try {
				// El redimensionem
				options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				background = BitmapFactory.decodeResource(context.getResources(), R.drawable.ultraso_180_master, options);
			}
			catch (Exception e) {
			}
		}

		// TODO Per fixar uns valors de la gràfica. Borrar posteriorment
		for (int i = 0; i < numberPoints; i++) {
			// Valor aleatori entre 0 i 100
			distanceValues[i] = (int) (Math.random() * 100.0f);
		}
	}

	//TODO: Borrar?
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldh, oldw);
	}

	/**
	 * Calcula la mida que ha de tenir el component en funció dels paràmetres del layout
	 * 
	 * @param widthMeasureSpec
	 *            : Amplada especificada
	 * @param heightMeasureSpec
	 *            : Alçada especificada
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int desiredWidth = background.getWidth(); // Valor per defecte
		int desiredHeight = background.getHeight(); // Valor per defecte
		boolean heightFixed = false; // Indiquem que l'alçada no s'ha calculat encara

		// Llegim els valors que fixa el layout
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		// Fixem els valors inicials d'alçada i amplada que tindrà el component
		int width = desiredWidth;
		int height = desiredHeight;

		// Calculem l'amplada
		if (widthMode == MeasureSpec.EXACTLY) {
			// Ha de tenir un valor fixat
			width = widthSize;
		}
		else {
			if (widthMode == MeasureSpec.AT_MOST) {
				// No pot ser major de...
				width = Math.min(desiredWidth, widthSize);
				// Fixem l'alçada perquè el component sigui quadrat
				height = width;
				heightFixed = true;
			}
			// Sinó, tindrà la mida per defecte
		}

		// Sinó 'ha fixat prèviament l'alçada
		if (heightFixed == false) {
			// Calculem l'amplada
			if (heightMode == MeasureSpec.EXACTLY) {
				// Ha de tenir un valor fixat
				height = heightSize;
			}
			else {
				if (heightMode == MeasureSpec.AT_MOST) {
					// No pot ser major de...
					height = Math.min(desiredHeight, heightSize);
				}
				// Sinó, tindrà la mida per defecte
			}
		}

		// Reajustem l'offset degut al forat blanc del centre. Extret de la relació entre l'amplada de la imatge i
		// l'amplada del forat originals
		roundOffset = (int) (width * 0.298077);

		// Fixem els valors calculats
		setMeasuredDimension(width, height);
	}

	/**
	 * Funció que es crida cada cop que s'ha de pintar el component
	 */
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Llegim el valor d'alçada i amplada
		int width = getWidth();
		int height = getHeight();

		// Calculem el mapeig dels valors a la gràfica (de 0 a 100) afegint l'offset (al centre hi ha un forat)
		double maxValue = (width / 2.0f) - roundOffset;

		// Desplaçem les coordenades al centre del component, i a la part d'abaix (180º)
		final float xOffset = width * 0.5f;
		final float yOffset = height;

		// Redimensionem el fons a la mida corresponent
		background = resizeImage(background, width, height);

		// Posem la imatge al fons (centrada en el cas que la imatge sigui més estreta que l'espai que té assignat)
		canvas.drawBitmap(background, (width / 2) - background.getWidth() / 2, 0, null);

		// Variables on es guardarà la projecció de cada punt sobre l'eix X i Y (Degut a que la gràfica és circular)
		double projectionX, projectionY;
		// Variables on es guardarà el valor del mòdul de la funció
		double module;
		// Variables on es guardarà Les coordenades del punt anterior
		float lastCoordX = 0.0f, lastCoordY = 0.0f;
		
		// Pintem cada punt
		for (int i = 0; i < numberPoints; i++) {
			// Calculem la projecció en els dos eixos
			projectionX = Math.cos(angleStep * i);
			projectionY = -Math.sin(angleStep * i); // L'eix Y està invertit!

			// Calculem el mòdul del punt (de 0 a 100)
			module = ((maxValue * distanceValues[i]) / 100.0f + roundOffset);

			//Guardem la coordenada x i y finals
			float coordX = (float) (module * projectionX + xOffset);
			float coordY = (float) (module * projectionY + yOffset);
			
			// Pintem el punt (5 píxels de radi)
			canvas.drawCircle(coordX, coordY, (float) 5.0, pPoint);		//TODO: Canviar mida bola en tablet?

			// Pintem una línia entre el punt actual i l'anterior
			if (lastCoordX != 0.0 && lastCoordX != 0.0)
			{
				canvas.drawLine(coordX, coordY, lastCoordX, lastCoordY, pLines);
			}

			// Actualitzem el punt anterior
			lastCoordX = coordX;
			lastCoordY = coordY;
		}

		// No cal pintar la línia entre el primer i l'últim aquí!
	}

	/**
	 * Redimensiona un bitmap en funció del la mida màxima que pot tenir
	 * 
	 * @param image
	 *            : Imatge a redimensionar
	 * @param maxWidth
	 *            : Amplada màxima.
	 * @param maxHeight
	 *            : Alçada màxima.
	 * @return Imatge redimensionada.
	 */
	public Bitmap resizeImage(Bitmap image, int maxWidth, int maxHeight) {

		Bitmap resizedImage = null;

		try {
			// Alçada de la imatge
			int imageHeight = image.getHeight();

			// Si la imatge és més alta que el màxim, l'alçada serà el màxim.
			if (imageHeight > maxHeight)
				imageHeight = maxHeight;
			// L'amplada mantindrà la relació d'aspecte
			int imageWidth = (imageHeight * image.getWidth()) / image.getHeight();

			// Si la imatge és més ample que el màxim, l'amplada serà el màxim.
			if (imageWidth > maxWidth) {
				imageWidth = maxWidth;
				imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
			}

			// Finalment revisem que els paràmetres no excedeixin el màxim
			if (imageHeight > maxHeight)
				imageHeight = maxHeight;
			if (imageWidth > maxWidth)
				imageWidth = maxWidth;

			// Redimensionem la imatge
			resizedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
		}
		catch (OutOfMemoryError e) {
			// No hi ha prou memòria per fer-ho
			e.printStackTrace();
		}
		catch (NullPointerException e) {
			// No hi ha imatge a redimensionar
			e.printStackTrace();
		}
		catch (Exception e) {
			// Altres
			e.printStackTrace();
		}

		return resizedImage;
	}
	
	/**
	 * Retorna el valor dels punts de la gràfica
	 * @return
	 */
	public int[] getDistanceValues() {
		return distanceValues;
	}

	/**
	 * Guarda el valor de cada punt de la gràfica
	 * @param distanceValues
	 */
	public void setDistanceValues(int[] distanceValues) {
		this.distanceValues = distanceValues;
	}

	/**
	 * Retorna el número de punts que hi ha a la gràfica
	 * @return
	 */
	public static int getNumberpoints() {
		return numberPoints;
	}
	
}
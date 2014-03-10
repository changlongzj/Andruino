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
 * Genera el component que mostra els valors de l'ultras� en una gr�fica de 180�.
 * Els punts s�n equidistants.
 * @author Aaron
 *
 */
public class Ultraso180 extends ImageView {

	// N�mero de punts de la gr�fica
	public final static int numberPoints = 5;
	// Increment d'angle de cada punt de la gr�fica (en radiants)
	public final static double angleStep = (float) (Math.PI / ((double) numberPoints-1));	//Vigilar si hi ha 1 sol punt!

	// Valor dels punts a pintar
	int[] distanceValues = new int[numberPoints];

	// Valor de l'offset (Perqu� al centre de la gr�fica no es pinta!). S'actualitza a onMeasure
	int roundOffset;

	// Configuracions dels punts i les l�nies
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
	 * Inicialitzaci� de la classe
	 * 
	 * @param context
	 *            : Context
	 */
	private void initialization(Context context) {

		// Inicialitzem les variables
		pPoint = new Paint(); // Per pintar els punts de la gr�fica
		pLines = new Paint(); // Per pintar les l�nies de la gr�fica

		pPoint.setAntiAlias(true);
		pPoint.setColor(Color.RED);
		pPoint.setStyle(Paint.Style.FILL); // Pintarem cercles. Estaran pintats per dins

		pLines.setAntiAlias(true);
		pLines.setColor(Color.BLUE);
		pLines.setStyle(Paint.Style.STROKE);
		pLines.setStrokeWidth(2f); // Amplada de la l�nia

		// Carreguem el fons. El redimensionem si no hi ha prou mem�ria
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

		// TODO Per fixar uns valors de la gr�fica. Borrar posteriorment
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
	 * Calcula la mida que ha de tenir el component en funci� dels par�metres del layout
	 * 
	 * @param widthMeasureSpec
	 *            : Amplada especificada
	 * @param heightMeasureSpec
	 *            : Al�ada especificada
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int desiredWidth = background.getWidth(); // Valor per defecte
		int desiredHeight = background.getHeight(); // Valor per defecte
		boolean heightFixed = false; // Indiquem que l'al�ada no s'ha calculat encara

		// Llegim els valors que fixa el layout
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		// Fixem els valors inicials d'al�ada i amplada que tindr� el component
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
				// Fixem l'al�ada perqu� el component sigui quadrat
				height = width;
				heightFixed = true;
			}
			// Sin�, tindr� la mida per defecte
		}

		// Sin� 'ha fixat pr�viament l'al�ada
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
				// Sin�, tindr� la mida per defecte
			}
		}

		// Reajustem l'offset degut al forat blanc del centre. Extret de la relaci� entre l'amplada de la imatge i
		// l'amplada del forat originals
		roundOffset = (int) (width * 0.298077);

		// Fixem els valors calculats
		setMeasuredDimension(width, height);
	}

	/**
	 * Funci� que es crida cada cop que s'ha de pintar el component
	 */
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Llegim el valor d'al�ada i amplada
		int width = getWidth();
		int height = getHeight();

		// Calculem el mapeig dels valors a la gr�fica (de 0 a 100) afegint l'offset (al centre hi ha un forat)
		double maxValue = (width / 2.0f) - roundOffset;

		// Despla�em les coordenades al centre del component, i a la part d'abaix (180�)
		final float xOffset = width * 0.5f;
		final float yOffset = height;

		// Redimensionem el fons a la mida corresponent
		background = resizeImage(background, width, height);

		// Posem la imatge al fons (centrada en el cas que la imatge sigui m�s estreta que l'espai que t� assignat)
		canvas.drawBitmap(background, (width / 2) - background.getWidth() / 2, 0, null);

		// Variables on es guardar� la projecci� de cada punt sobre l'eix X i Y (Degut a que la gr�fica �s circular)
		double projectionX, projectionY;
		// Variables on es guardar� el valor del m�dul de la funci�
		double module;
		// Variables on es guardar� Les coordenades del punt anterior
		float lastCoordX = 0.0f, lastCoordY = 0.0f;
		
		// Pintem cada punt
		for (int i = 0; i < numberPoints; i++) {
			// Calculem la projecci� en els dos eixos
			projectionX = Math.cos(angleStep * i);
			projectionY = -Math.sin(angleStep * i); // L'eix Y est� invertit!

			// Calculem el m�dul del punt (de 0 a 100)
			module = ((maxValue * distanceValues[i]) / 100.0f + roundOffset);

			//Guardem la coordenada x i y finals
			float coordX = (float) (module * projectionX + xOffset);
			float coordY = (float) (module * projectionY + yOffset);
			
			// Pintem el punt (5 p�xels de radi)
			canvas.drawCircle(coordX, coordY, (float) 5.0, pPoint);		//TODO: Canviar mida bola en tablet?

			// Pintem una l�nia entre el punt actual i l'anterior
			if (lastCoordX != 0.0 && lastCoordX != 0.0)
			{
				canvas.drawLine(coordX, coordY, lastCoordX, lastCoordY, pLines);
			}

			// Actualitzem el punt anterior
			lastCoordX = coordX;
			lastCoordY = coordY;
		}

		// No cal pintar la l�nia entre el primer i l'�ltim aqu�!
	}

	/**
	 * Redimensiona un bitmap en funci� del la mida m�xima que pot tenir
	 * 
	 * @param image
	 *            : Imatge a redimensionar
	 * @param maxWidth
	 *            : Amplada m�xima.
	 * @param maxHeight
	 *            : Al�ada m�xima.
	 * @return Imatge redimensionada.
	 */
	public Bitmap resizeImage(Bitmap image, int maxWidth, int maxHeight) {

		Bitmap resizedImage = null;

		try {
			// Al�ada de la imatge
			int imageHeight = image.getHeight();

			// Si la imatge �s m�s alta que el m�xim, l'al�ada ser� el m�xim.
			if (imageHeight > maxHeight)
				imageHeight = maxHeight;
			// L'amplada mantindr� la relaci� d'aspecte
			int imageWidth = (imageHeight * image.getWidth()) / image.getHeight();

			// Si la imatge �s m�s ample que el m�xim, l'amplada ser� el m�xim.
			if (imageWidth > maxWidth) {
				imageWidth = maxWidth;
				imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
			}

			// Finalment revisem que els par�metres no excedeixin el m�xim
			if (imageHeight > maxHeight)
				imageHeight = maxHeight;
			if (imageWidth > maxWidth)
				imageWidth = maxWidth;

			// Redimensionem la imatge
			resizedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
		}
		catch (OutOfMemoryError e) {
			// No hi ha prou mem�ria per fer-ho
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
	 * Retorna el valor dels punts de la gr�fica
	 * @return
	 */
	public int[] getDistanceValues() {
		return distanceValues;
	}

	/**
	 * Guarda el valor de cada punt de la gr�fica
	 * @param distanceValues
	 */
	public void setDistanceValues(int[] distanceValues) {
		this.distanceValues = distanceValues;
	}

	/**
	 * Retorna el n�mero de punts que hi ha a la gr�fica
	 * @return
	 */
	public static int getNumberpoints() {
		return numberPoints;
	}
	
}
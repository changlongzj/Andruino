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
 * Genera el component que mostra els valors de l'ultras� en una gr�fica de 360�. Els punts s�n equidistants
 * 
 * @author Aaron
 * 
 */
public class Pendent extends ImageView {

	// Coordenades de la bola
	private int xValue = 50;
	private int yValue = 50;

	// Configuracions dels punts i les l�nies
	private Paint pPoint;
	private Paint pLines;

	// Imatge de fons del component
	private Bitmap background;
	private Bitmap ball;

	// Factor de redimensionament de les imatges (Perqu� la bola i el fons tinguin el mateix)
	private float resizeFactor = 1f;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public Pendent(Context context) {
		super(context);
		initialization(context);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public Pendent(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialization(context);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public Pendent(Context context, AttributeSet attrs, int defStyle) {
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
			background = BitmapFactory.decodeResource(context.getResources(), R.drawable.radar);
			ball = BitmapFactory.decodeResource(context.getResources(), R.drawable.radar_bola);
		}
		catch (OutOfMemoryError o) {
			try {
				// El redimensionem
				options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				background = BitmapFactory.decodeResource(context.getResources(), R.drawable.radar, options);
				ball = BitmapFactory.decodeResource(context.getResources(), R.drawable.radar_bola);
			}
			catch (Exception e) {
			}
		}
	}

	// TODO: Borrar?
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

		// Despla�em les coordenades al centre del component
		final float xOffset = width * 0.5f;
		final float yOffset = height * 0.5f;

		// Redimensionem el fons a la mida corresponent
		background = resizeImage(background, width, height);

		// Posem la imatge al fons (centrada en el cas que la imatge sigui m�s estreta que l'espai que t� assignat)
		canvas.drawBitmap(background, (width / 2) - background.getWidth() / 2, 0, null);

		// Posem la bola a les coordenades corresponents
		canvas.drawBitmap(ball, (xValue + xOffset) - (ball.getWidth() / 2), (yValue + yOffset) - (ball.getHeight() / 2), null);
		//XXX: Coordenades y girades!
		//XXX: Valors de X i Y limitats? En %?

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

			// Redimensionem el fons
			resizedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
			
			//La imatge ser� quadrada. Extraiem el valor del redimensionat
			resizeFactor = resizedImage.getHeight() / image.getHeight();
			
			// Redimensionem la bola
			ball = Bitmap.createScaledBitmap(ball, (int)(ball.getWidth()*resizeFactor), (int)(ball.getHeight()*resizeFactor), true);
			//XXX: Redimensionament de la bola!
			
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
	 * Retorna la posici� actual de la bola
	 * 
	 * @return
	 */
	public int getXValue() {
		return xValue;
	}

	/**
	 * Fixa l'eix X de la bola
	 * 
	 * @param xValue
	 */
	public void setXValue(int xValue) {
		this.xValue = xValue;
	}

	/**
	 * Retorna la posici� actual de la bola
	 * 
	 * @return
	 */
	public int getYValue() {
		return yValue;
	}

	/**
	 * Fixa l'eix Y de la bola
	 * 
	 * @param yValue
	 */
	public void setYValue(int yValue) {
		this.yValue = yValue;
	}

}
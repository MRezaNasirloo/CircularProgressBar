# CircularProgressBar
A subclass of {@link android.view.View} class for creating a custom circular progressBar

![enter image description here][4]

**1.** Create a file under ***values/attrs.xml***
This resources is used for setting values in XML layout file for our view

    <resources>
        <declare-styleable name="CircleProgressBar">
            <attr name="min" format="integer" />
            <attr name="max" format="integer" />
            <attr name="progress" format="integer" />
            <attr name="progressbarColor" format="color" />
            <attr name="progressBarThickness" format="dimension" />
        </declare-styleable>
    
    </resources>

**2.** Create a class and extends the View class ***CircularProgressBar.java***

    public class CircleProgressBar extends View {
    
    
    /**
     * ProgressBar's line thickness
     */
    private float strokeWidth = 4;
    private float progress = 0;
    private int min = 0;
    private int max = 100;
    /**
     * Start the progress at 12 o'clock
     */
    private int startAngle = -90;
    private int color = Color.DKGRAY;
    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;


These are the fields we need in order to draw our view

It is neccessary to add a constructor, Also we need to read values from `XML` layout file

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

In `init()` method we get and set our values from defined styleable and initialize our [Paint][2] objects.

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleProgressBar,
                0, 0);
        //Reading values from the XML layout
        try {
            strokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_progressBarThickness, strokeWidth);
            progress = typedArray.getFloat(R.styleable.CircleProgressBar_progress, progress);
            color = typedArray.getInt(R.styleable.CircleProgressBar_progressbarColor, color);
            min = typedArray.getInt(R.styleable.CircleProgressBar_min, min);
            max = typedArray.getInt(R.styleable.CircleProgressBar_max, max);
        } finally {
            typedArray.recycle();
        }
    
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(adjustAlpha(color, 0.3f));
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
    
        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);
    }
   We also use adjustAlpha method to make the background color ligher.

    private int adjustAlpha(int color, float factor) {
		int alpha = Math.round(Color.alpha(color) * factor);
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		return Color.argb(alpha, red, green, blue);
	}

**3.** It is crucial to measure our view, In order to properly draw our custom view, we need to know what size it is. So we override the `onMeasure()`

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        rectF.set(0 + strokeWidth / 2, 0 + strokeWidth / 2, min - strokeWidth / 2, min - strokeWidth / 2);
    }

You should set the size of the view to the `rectF` object in order to instruct the `canvas.draw()` method where it should to draw the view. Also it is important to call the `setMeasuredDimension()` to notify the system how big the view is going to be.

**4.** The most important part of this class is the `onDraw()` method, we should override it to draw our view in its providing [Canvas][3]

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    
        canvas.drawOval(rectF, backgroundPaint);
        float angle = 360 * progress / max;
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);
    
    }

**5.** Now add the view in your Layout file

       
    
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">
        
        <com.mrn.customprogressbar.CircleProgressBar
            android:id="@+id/custom_progressBar"
            android:layout_width="200dp"
            android:layout_height="200dp"
            android:layout_margin="10dp"
            app:progress="35"
            app:progressBarThickness="4dp"/>
        
    </LinearLayout>

**6.** Lastly add coresponsive setters and getters, The neccessary one is the `setProgress()`

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();// Notify the view to redraw it self (the onDraw method is called)
    }

That's it.


  [1]: http://i.stack.imgur.com/g0wrk.png
  [2]: http://developer.android.com/reference/android/graphics/Paint.html
  [3]: http://developer.android.com/reference/android/graphics/Canvas.html
  [4]: http://i.stack.imgur.com/hGYIp.gif
  [5]: http://goo.gl/ze4bAE

![ScreenShot](/ScreenShot-2015-01-14-233007.png)

package com.arielu.shopper.demo.NavigationElements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class PixelGridView extends View {
    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint blackPaint = new Paint();
    private boolean[][] cellChecked;
    private PathTracker tracker;

    public PixelGridView(Context context) {
        this(context, null);
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //blackPaint.setColor(randomColor());
    }

    private int randomColor(){
        Random rnd = new Random();
        return (Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public void setTracker(PathTracker tracker) {
        this.tracker = tracker;
    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }
        cellWidth = getWidth() / numColumns;
        cellHeight = getHeight() / numRows;
        cellChecked = new boolean[numColumns][numRows];

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        if (numColumns == 0 || numRows == 0) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (cellChecked[i][j]) {

                    canvas.drawRect(i * cellWidth, j * cellHeight,
                            (i + 1) * cellWidth, (j + 1) * cellHeight,
                            blackPaint);
                }
//                else{
//                   drew rect with color
//                }
            }
        }

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       if (event.getAction() == MotionEvent.ACTION_DOWN) {
           PathNode node = tracker.list.head;
            if(!node.hasNext()){
                Point p = node.getPath().getPoints().get(0);
                cellChecked[p.getY()][p.getX()] = !cellChecked[p.getY() ][p.getX()];
            }
            else {
                //iterate the nodes in the list
                node = node.getNext();
                while (node != tracker.list.tail) {//iterate path points and paint in black
                    for (int i = 0; i < node.getPath().getPoints().size(); i++) {
                        Point p = node.getPath().getPoints().get(i);
                        cellChecked[p.getY()][p.getX()] = !cellChecked[p.getY()][p.getX()];
                    }
                    node = node.getNext();
                }
                for (int i = 0; i < node.getPath().getPoints().size(); i++) {
                    Point p = node.getPath().getPoints().get(i);
                    cellChecked[p.getY()][p.getX()] = !cellChecked[p.getY()][p.getX()];
                }
            }
           invalidate();

       }

       return true;
    }
}
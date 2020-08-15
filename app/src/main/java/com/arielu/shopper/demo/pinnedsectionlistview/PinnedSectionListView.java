package com.arielu.shopper.demo.pinnedsectionlistview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

public class PinnedSectionListView extends ExpandableListView {
    public interface PinnedSection{
        /**
         * section gone
         */
        public static final int PINNED_UP_SECTION_GONE = 0;
        /**
         * section visible
         */
        public static final int PINNED_UP_SECTION_VISIBLE = 1;
        /**
         * push current section up
         */
        public static final int PINNED_UP_SECTION_PUSHED = 2;
        /**
         * bottom section gone
         */
        public static final int PINNED_DOWN_SECTION_GONE = 3;
        /**
         * bottom section visible
         */
        public static final int PINNED_DOWN_SECTION_VISIBLE = 4;
        /**
         * bottom section pulled
         */
        public static final int PINNED_DOWN_SECTION_PULL = 5;
    }
    private static final int MAX_ALPHA = 255;
    private PinnedSectionAdapter mAdapter;
    private View mBottomSectionView;
    private boolean mBottomSectionViewVisible;
    private int mBottomSectionWidth;
    private int mBottomSectionHeight;
    private View mTopSectionView;
    private boolean mTopSectionViewVisible;
    private int mTopSectionWidth;
    private int mTopSectionHeight;
    private int currentGroup,aheadGroup;
    private boolean isClickableBottom,isClickableTop;
    private GestureDetector.SimpleOnGestureListener gestureDetectorSimple;

    public PinnedSectionListView(Context context) {
        super(context);
    }

    public PinnedSectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinnedSectionListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PinnedSectionListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private void init(){
        setGroupIndicator(null);
        setOnScrollListener((OnScrollListener)mAdapter);
        setDividerHeight(0);
        gestureDetectorSimple = new GestureDetector.SimpleOnGestureListener();
    }
    public void setPinnedSections(int layout){
        setPinnedSections(layout,layout);
    }
    public void setPinnedSections(int topSectionLayout,int bottomSectionLayout){
        View t = LayoutInflater.from(getContext()).inflate(topSectionLayout, (ViewGroup) findViewById(getId()), false);
        View b = LayoutInflater.from(getContext()).inflate(bottomSectionLayout,(ViewGroup) findViewById(getId()),false);
        setPinnedSections(t,b);
    }
    public void setPinnedSections(View top,View bottom){
        setPinnedTopSection(top);
        setPinnedBottomSection(bottom);
    }
    public void setPinnedTopSection(View section){
        mTopSectionView = section;
        if(mTopSectionView!=null){
            setFadingEdgeLength(0);
        }
        requestLayout();
    }
    public void setPinnedBottomSection(View section){
        mBottomSectionView = section;
        if (mBottomSectionView!=null){
            setFadingEdgeLength(0);
        }
        requestLayout();
    }
    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        this.mAdapter = (PinnedSectionAdapter) adapter;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTopSectionView!=null){
            measureChild(mTopSectionView,widthMeasureSpec,heightMeasureSpec);
            mTopSectionWidth = mTopSectionView.getMeasuredWidth();
            mTopSectionHeight = mTopSectionView.getMeasuredHeight();
        }
        if(mBottomSectionView!=null){
            measureChild(mBottomSectionView,widthMeasureSpec,heightMeasureSpec);
            mBottomSectionWidth = mBottomSectionView.getMeasuredWidth();
            mBottomSectionHeight = mBottomSectionView.getMeasuredHeight();
        }
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //TODO: set position by translation with width and height of parent and child
        if(mTopSectionView!=null){
            mTopSectionView.layout(0,0,mTopSectionWidth,mTopSectionHeight);
            mTopSectionView.setTranslationX(getTranslationX());
            mTopSectionView.setTranslationY(getTranslationY());
            configureSectionTop(getFirstVisiblePosition());
        }
        if (mBottomSectionView!=null){
            mBottomSectionView.layout(0,0,mBottomSectionWidth,mBottomSectionHeight);
            mBottomSectionView.setTranslationX(getTranslationX());
            mBottomSectionView.setTranslationY(getTranslationY()+getHeight()-mBottomSectionView.getHeight());
            configureSectionBottom(getLastVisiblePosition());
        }
    }
    public void configureSectionBottom(final int position){
        final int group = getPackedPositionGroup(getExpandableListPosition(position))+1;
        isClickableBottom = (position!=getFlatListPosition(getPackedPositionForGroup(group)));
        if (mBottomSectionView==null){
            return;
        }
        int state,nextSectionPosition=getFlatListPosition(getPackedPositionForGroup(group+1)),lastSectionPosition = getFlatListPosition(getPackedPositionForGroup(mAdapter.getGroupCount()-1));
        if(mAdapter.getGroupCount()<=1){
            state = PinnedSection.PINNED_DOWN_SECTION_GONE;
        }else if (group == mAdapter.getGroupCount()||group > lastSectionPosition) {
        state = PinnedSection.PINNED_DOWN_SECTION_GONE;
    } else if (nextSectionPosition != -1 && position == nextSectionPosition - 1) {
        state = PinnedSection.PINNED_DOWN_SECTION_PULL;
    } else state = PinnedSection.PINNED_DOWN_SECTION_VISIBLE;
    switch (state){
        case PinnedSection.PINNED_DOWN_SECTION_GONE:{
         mBottomSectionViewVisible = false;
         break;
        }
        case PinnedSection.PINNED_DOWN_SECTION_VISIBLE:{
            mAdapter.configurePinnedSection(mBottomSectionView,group,MAX_ALPHA);
            aheadGroup=group;
            if (mBottomSectionView.getTop() != 0) {
                mBottomSectionView.layout(0, 0, mBottomSectionWidth, mBottomSectionHeight);
            }
            mBottomSectionViewVisible = true;
            break;
        }
        case PinnedSection.PINNED_DOWN_SECTION_PULL:{
            View lastView = getChildAt(getChildCount()-1);
            if (lastView == null) {
                if (mBottomSectionView.getBottom() != 0) {
                    mBottomSectionView.layout( 0, 0,mBottomSectionWidth, mBottomSectionHeight);
                }
                mBottomSectionViewVisible = true;
                break;
            }
            int top = lastView.getTop();
            int itemHeight = lastView.getHeight();
            int headerHeight = mBottomSectionView.getHeight();
            int y;
            int alpha;
            if (top > headerHeight) {
                y = (top - headerHeight);
                alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
            } else {
                y = 0;
                alpha = MAX_ALPHA;
            }
            mAdapter.configurePinnedSection(mBottomSectionView, group, alpha);

            if (mBottomSectionView.getTop() != y) {
                mBottomSectionView.layout(0, y, mBottomSectionHeight, mBottomSectionHeight + y);
            }
            mBottomSectionViewVisible = true;
            break;
        }

    }
}
    public void configureSectionTop(final int position) {
        final int group = getPackedPositionGroup(getExpandableListPosition(position));
        isClickableTop = (position!=getFlatListPosition(getPackedPositionForGroup(group)));
        if (mTopSectionView == null) {
            return;
        }
        int state, nextSectionPosition = getFlatListPosition(getPackedPositionForGroup(group + 1));
        if (mAdapter.getGroupCount() == 0) {
            state = PinnedSection.PINNED_UP_SECTION_GONE;
        } else if (position < 0) {
            state = PinnedSection.PINNED_UP_SECTION_GONE;
        } else if (nextSectionPosition != -1 && position == nextSectionPosition - 1) {
            state = PinnedSection.PINNED_UP_SECTION_PUSHED;
        } else state = PinnedSection.PINNED_UP_SECTION_VISIBLE;
        switch (state) {
            case PinnedSection.PINNED_UP_SECTION_GONE: {
                mTopSectionViewVisible = false;
                break;
            }

            case PinnedSection.PINNED_UP_SECTION_VISIBLE: {
                mAdapter.configurePinnedSection(mTopSectionView, group, MAX_ALPHA);
                currentGroup=group;
                if (mTopSectionView.getTop() != 0) {

                    mTopSectionView.layout(0, 0, mTopSectionWidth, mTopSectionHeight);
                }
                mTopSectionViewVisible = true;
                break;
            }

            case PinnedSection.PINNED_UP_SECTION_PUSHED: {
                View firstView = getChildAt(0);
                if (firstView == null) {
                    if (mTopSectionView.getTop() != 0) {
                        mTopSectionView.layout(0, 0, mTopSectionWidth, mTopSectionHeight);
                    }
                    mTopSectionViewVisible = true;
                    break;
                }
                int bottom = firstView.getBottom();
                int itemHeight = firstView.getHeight();
                int headerHeight = mTopSectionView.getHeight();
                int y;
                int alpha;
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                    alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
                } else {
                    y = 0;
                    alpha = MAX_ALPHA;
                }
                mAdapter.configurePinnedSection(mTopSectionView, group, alpha);
                if (mTopSectionView.getTop() != y) {
                    mTopSectionView.layout(0, y, mTopSectionWidth, mTopSectionHeight + y);
                }
                mTopSectionViewVisible = true;
                break;
            }

        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mTopSectionViewVisible) {
            drawChild(canvas, mTopSectionView, getDrawingTime());
        }
        if (mBottomSectionViewVisible){
            drawChild(canvas,mBottomSectionView,getDrawingTime());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN) {
            if (mTopSectionViewVisible && isClickableTop && mTopSectionView.getLeft() <= ev.getX() && mTopSectionView.getRight() >= ev.getX() && mTopSectionView.getTop() <= ev.getY() && mTopSectionView.getBottom() >= ev.getY()) {
                this.setSelectedGroup(currentGroup);
                return true;
            }
            if (mBottomSectionViewVisible && isClickableBottom && mBottomSectionView.getTranslationX() <= ev.getX() && (mBottomSectionView.getTranslationX() + mBottomSectionView.getRight()) >= ev.getX() && (mBottomSectionView.getTranslationY() + mBottomSectionView.getTop()) <= ev.getY() && (mBottomSectionView.getTranslationY() + mBottomSectionView.getBottom()) >= ev.getY()) {
                this.setSelectedGroup(aheadGroup);
                return true;
            }
        }
        return super.onTouchEvent(ev);
    }
}

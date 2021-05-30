package i.am.frustrated.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.HashMap;

public class BubbleSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    private int sections = 0;
    //private HashMap<Integer, String> sectionText;
    private String[] sectionText;
    private ArrayList<Integer> sectionPoints = new ArrayList<>();
    private int bMin = 0,bMax = 100;

    public BubbleSeekBar(Context context) {
        super(context);
    }

    public BubbleSeekBar(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    public BubbleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {

        if(sections > 0 ){

            for(int i = bMin; i <= sections; i++) {

                sectionPoints.add(i, (bMin + bMax)/sections);

            }

        }

    }

    @Override
    public synchronized void setMax(int max) {
        super.setMax(max);
        bMax = max;
    }

    @Override
    public synchronized void setMin(int min) {
        super.setMin(min);
        bMin = min;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    public int getSections() {
        return sections;
    }

    public void setSections(int sections) {
        this.sections = sections;
    }

    public String[] getSectionText() {
        return sectionText;
    }

    public void setSectionText(String[] sectionText) {
        this.sectionText = sectionText;
    }


}

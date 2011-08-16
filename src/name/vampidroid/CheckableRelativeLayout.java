package name.vampidroid;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
	
	



	private boolean mIsChecked = false;

	
	public CheckableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return mIsChecked;
	}
	

	@Override
	public void setChecked(boolean checked) {
		

		if (checked) 
			setBackgroundColor(Color.parseColor("#FFFFC445"));
		else
			setBackgroundColor(Color.BLACK);
		
	}

	@Override
	public void toggle() {
		// TODO Auto-generated method stub
		
		setChecked(!mIsChecked);
		
		
	}

}

package com.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;

public class Buttons extends Activity
{
	protected void onCreate(Bundle savedInstanceState) 
	{
		LinearLayout ll=new LinearLayout(this);
		super.onCreate(savedInstanceState);
		setContentView(ll);
		Button b=new Button(this);
		b.setText("Click");
		b.setLayoutParams(new LayoutParams(50, 30,180,40));
		ll.addView(b);
		ActionItem push =new ActionItem();
		push.setTitle("Push");
		//push.setIcon(getResources().getDrawable(R.drawable.push));
		
		
		ActionItem pull=new ActionItem();
		pull.setTitle("Pull");
		//pull.setIcon(getResources().getDrawable(R.drawable.pull));
		
		ActionItem sync=new ActionItem();
		sync.setTitle("Sync");
		//sync.setIcon(getResources().getDrawable(R.drawable.sync));
		
		final QuickAction mQuickAction 	= new QuickAction(this);
		
		mQuickAction.addActionItem(push);
		mQuickAction.addActionItem(pull);
		mQuickAction.addActionItem(sync);
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(int pos) {
				
				if (pos == 0) { //Add item selected
					Toast.makeText(Buttons.this, "Add item selected", Toast.LENGTH_SHORT).show();
				} else if (pos == 1) { //Accept item selected
					Toast.makeText(Buttons.this, "Accept item selected", Toast.LENGTH_SHORT).show();
				} else if (pos == 2) { //Upload item selected
					Toast.makeText(Buttons.this, "Upload items selected", Toast.LENGTH_SHORT).show();
				}	
			}
		});
		
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mQuickAction.show(v);
			}
		});
	}

}

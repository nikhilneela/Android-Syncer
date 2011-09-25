package com.iiitb.nikhil.testApp;

import java.io.*;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.AbsoluteLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AbsoluteLayout.LayoutParams;


public class TestAppActivity extends Activity implements OnClickListener{
	AbsoluteLayout ll;
	ScrollView scroll;
	TextView tv;

	ArrayList<String> appName,packageName;

	Process process;
	DataOutputStream os;
	FileInputStream fstream;
	DataInputStream in;
	BufferedReader br;
	
    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = new TextView(this);
        scroll = new ScrollView(this);
        ll = new AbsoluteLayout(this);
        
        ll.addView(tv);
        
        try {
			process = Runtime.getRuntime().exec("/system/bin/su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("/system/bin/ls /data/app | grep .apk  > /sdcard/temp/filelist1.txt \n");
			os.writeBytes("/system/bin/ls /mnt/asec > /sdcard/temp/filelist2.txt \n");
			os.writeBytes("/system/bin/cat /sdcard/temp/filelist1.txt /sdcard/temp/filelist2.txt > /sdcard/temp/filelist.txt");
			os.flush();
			
			fstream = new FileInputStream("/sdcard/temp/filelist.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			
			appName = new ArrayList<String>();
			packageName = new ArrayList<String>();

			String line;			
			while((line=br.readLine())!=null)
			{
				int index = line.indexOf("-");
				line = line.substring(0, index);
				packageName.add(line);
				appName.add(line.substring(line.lastIndexOf(".")+1));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        int tv_y=10,btn_y=10;
        
        for(int i=0;i<appName.size();i++)
        {
        	Button btn=new Button(this);
        	btn.setId(i);
        	btn.setText("Sync");
        	
        	btn.setLayoutParams(new LayoutParams(50, 30,180,btn_y));
        	btn.setOnClickListener(this);
        	TextView tv=new TextView(this);
        	tv.setId(i);
        	tv.setText(appName.get(i));
        	tv.setLayoutParams(new LayoutParams(200,50,10,tv_y));
        	tv.setTextSize(30);
        	ll.addView(tv);
        	ll.addView(btn);
        	
        	btn_y+=40;
        	tv_y+=40;
        	
        }      
        scroll.addView(ll);
        setContentView(scroll);   
    }
    public void onClick(View v)
	{
		String packName = packageName.get(v.getId());
		try {
			Process process1 = Runtime.getRuntime().exec("/system/bin/su");
			DataOutputStream os1 = new DataOutputStream(process1.getOutputStream());
			os1.writeBytes("/system/bin/rm /sdcard/temp/temp.txt \n");
			os1.writeBytes("/system/bin/ls -l /data/data/ | /system/xbin/grep " + packName + " > /sdcard/temp/temp.txt \n");
			
			
			Log.i("test1", packName);
//			String rsyncString = "rsync -rltD --no-p -e \" ssh -i /sdcard/newnew/final1 \" /data/data/" + packName + "nikhil@192.168.2.106:/home/nikhil/a \n";
//			os1.writeBytes(rsyncString);
//			//String mystring = "rsync -rltD --no-p -e \" ssh -i /sdcard/newnew/final1 \" /data/data/com.rovio.angrybirds nikhil@192.168.2.106:/home/nikhil/a\n";
			String mystring = "rsync -rltD --no-p -e \" ssh -i /sdcard/newnew/final1 \" /data/data/"+packName.trim()+" nikhil@192.168.2.106:/home/nikhil/a\n";
			os1.writeBytes(mystring);
			os1.flush();
			os1.close();
			process1.waitFor();
			process1.destroy();
			
			fstream = new FileInputStream("/sdcard/temp/temp.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			
			String line;			
			line=br.readLine();
			
			line = line.replaceAll("( )+"," ");
			String temp[] = line.split(" ");
			
//	
//			FileOutputStream fw = openFileOutput("/sdcard/AppStateInfo/appState.txt", MODE_WORLD_READABLE );
//			OutputStreamWriter osw = new OutputStreamWriter(fw);
//			
//			osw.write(temp[1] + ":" + temp[2] + ":" + temp[5]);
//			osw.close();
//			br.close();
//			//os.writeBytes("/system/bin/rm /sdcard/temp/temp.txt \n");
//			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    	
    	
			Log.i("Hey Sindhu Look at me",Integer.toString(v.getId()));
			Log.i("Package name",packageName.get(v.getId()));
	}
}

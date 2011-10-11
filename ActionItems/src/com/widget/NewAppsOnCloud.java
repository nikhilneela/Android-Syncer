package com.widget;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;

public class NewAppsOnCloud extends Activity implements OnClickListener{

	private static final int BUFF_LEN = 1024;
	ArrayList<String> packagesOnCloud;
	Map<String,Integer> mapping;
	AppsHelper appsHelper;
	public AbsoluteLayout ll;
	public AlertDialog.Builder d;
	ScrollView scroll;
	ManageProcess process;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		
		super.onCreate(savedInstanceState);
		packagesOnCloud = new ArrayList<String>();
		String bytesRead = "";
		mapping = new HashMap<String, Integer>();
		process = new ManageProcess();
		process.acquireRoot();
		String outputCmd="";
		String fileList[];
		bytesRead = process.readCmd(outputCmd);
		process.executeCmd("rsync  rsync://pub@192.168.2.106/pub/backup/data/app/ | cut -c 44- | grep [[:alpha:]]");
		outputCmd="";
		bytesRead = process.readCmd(outputCmd);
		Log.i("Cloud:(BytesRead)", bytesRead);
		fileList = bytesRead.split("\r\n");
		for(int i=1;i<fileList.length-1;i++){
			mapping.put(new String("/data/app/"+fileList[i]), new Integer(0));
		}
		process.executeCmd("rsync  rsync://pub@192.168.2.106/pub/backup/system/app/ | cut -c 44- | grep [[:alpha:]]");
		outputCmd="";
		bytesRead = process.readCmd(outputCmd);
		fileList = bytesRead.split("\r\n");
		Log.i("Cloud:(BytesRead)", bytesRead);
		for(int i=1;i<fileList.length-1;i++){
			mapping.put(new String("/system/app/"+fileList[i]), new Integer(0));
		}
		process.executeCmd("rsync  rsync://pub@192.168.2.106/pub/backup/mnt/asec/ | cut -c 44- | grep [[:alpha:]]");
		outputCmd="";
		bytesRead = process.readCmd(outputCmd);
		Log.i("Cloud:(BytesRead)", bytesRead);
		fileList = bytesRead.split("\r\n");
		for(int i=1;i<fileList.length-1;i++){
			mapping.put(new String("/mnt/asec/"+fileList[i]+"/pkg.apk"), new Integer(0));
		}
		
		PackageManager  pm = getPackageManager();
		List<PackageInfo> pkginfo_list;
		pkginfo_list = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
		
		for(int i=0;i<pkginfo_list.size();i++){
			String tDir = pkginfo_list.get(i).applicationInfo.sourceDir;
			Log.i("Cloud",tDir);
			if(mapping.containsKey(tDir)){
				mapping.put(tDir, new Integer(i));
			}
		}
		
		ll = new AbsoluteLayout(this);
		scroll = new ScrollView(this);
		Set start =mapping.entrySet();
		Iterator iter=start.iterator();
		//appsHelper = new AppsHelper();
		//installedPackages = appsHelper.getAllPackages();
		//packagesOnCloud =appsHelper.appsOnCloud("sindhu@192.168.2.103:/home/sindhu/Desktop/");
		//packagesOnCloud =appsHelper.appsOnCloud("rsync://pub@192.168.2.106/pub/backup/");
		//appsHelper.appsOnCloud("sindhu@192.168.2.103:/home/sindhu/Desktop/a/");
		d=new AlertDialog.Builder(this);
		//if(packagesOnCloud==null)
		//{
			
			//d.setTitle("COULD NOT RSYNC");
			//d.show();
			
			
		//}
		//else
		//{
			//d.setTitle("WORKS FINE");
			//d.show();
			//Log.i("Before Size",installedPackages.size()+"");
			
			//Log.i("After Size",installedPackages.size()+"");
		
			//for(int i=0;i<packagesOnCloud.size();i++)
				//Log.i("Cloud", packagesOnCloud.get(i));
		
			//for(int i=0;i<installedPackages.size();i++)//working fine
				//Log.i("Local", installedPackages.get(i));
			
			//if(packagesOnCloud.removeAll(installedPackages))
			//{
				int tv_y=10,btn_y=10,i=0;
				while(iter.hasNext())
				{
		            Map.Entry m =(Map.Entry)iter.next();

					//Log.i("TO BE INSTALLED APPS",packagesOnCloud.get(i));
					Button btn=new Button(this);
		        	btn.setId(i);
		        	btn.setText("Install");//	Log.i("TO BE INSTALLED APPS",packagesOnCloud.get(i));
		        	
		        	btn.setLayoutParams(new LayoutParams(50,30,180,btn_y));
		        	btn.setOnClickListener(this);
		        	TextView tv=new TextView(this);
		        	tv.setId(i);
		        	
		        	tv.setLayoutParams(new LayoutParams(200,50,10,tv_y));
		        	tv.setTextSize(30);
		        	String tDir = (String) m.getKey();
		        	String dirComp[] = tDir.split("/");
		        	//String app_name=packagesOnCloud.get(i);
		        	Log.i("Cloud","extComp:"+tDir.substring(tDir.length()-3, tDir.length()));
		        	if(tDir.length()>4)
		        	{
		        	if((Integer)m.getValue() == 0 && tDir.substring(tDir.length()-3, tDir.length()).equals("apk") ) {
		        		
		        		
		        		
		        		if(dirComp[dirComp.length-1].equals("pkg.apk") && dirComp[dirComp.length-2].contains("-")){
		        			dirComp[dirComp.length-2]=dirComp[dirComp.length-2].substring(0, dirComp[dirComp.length-2].indexOf("-"));
			        		tv.setText(dirComp[dirComp.length-2].substring(dirComp[dirComp.length-2].lastIndexOf(".")+1));
			        		packagesOnCloud.add(tDir);
				        	ll.addView(tv);
				        	ll.addView(btn);
				        	btn_y+=40;
				        	tv_y+=40;
				        	
			        		i++;
		        		}
		        		else if(dirComp[dirComp.length-1].contains("-"))
			        	{
			        		dirComp[dirComp.length-1]=dirComp[dirComp.length-1].substring(0, dirComp[dirComp.length-1].indexOf("-"));
			        		tv.setText(dirComp[dirComp.length-1].substring(dirComp[dirComp.length-1].lastIndexOf(".")+1));
			        		packagesOnCloud.add(tDir);
				        	ll.addView(tv);
				        	ll.addView(btn);
				        	btn_y+=40;
				        	tv_y+=40;
				        	
			        		i++;
			        	}
		        		
		        		
		        	
	        		
		        	//tv.setText(extract[extract.length-1]);
		        	

		        	
		        	}

					
				
				}
				}
			//}
		
		//}
		scroll.addView(ll);
        setContentView(scroll);   
        
	}
	@Override
	public void onClick(View v) 
	{
		/*
		Process p = null;
		DataOutputStream stdin = null;
		InputStream stdout = null;
		byte[] buffer = null;
		int read = 0;
		String out = "";
		try {
			
			
			p = Runtime.getRuntime().exec(new String[]{"su", "-c", "system/bin/sh"});
			stdin = new DataOutputStream(p.getOutputStream());
			stdin.writeBytes("rsync -rpogtu  rsync://pub@192.168.2.106/pub/backup/phone-apk/"+packagesOnCloud.get(v.getId())+" /mnt/sdcard/tmp/ | xargs echo\n"); 
			stdout = p.getInputStream();
			buffer = new byte[BUFF_LEN];
			
			
			while(true){
		 
			read = stdout.read(buffer);
		    out += new String(buffer, 0, read);
		    if(read<BUFF_LEN){
		        //we have read everything
		        break;
		    }
			}
				//do something with the output
			
		//Toast.makeText(NewAppsOnCloud.this, "To be implemented \n"+packagesOnCloud.get(v.getId()), Toast.LENGTH_SHORT).show();
	}catch(Exception e) {
		try {
		stdin.writeBytes("rsync -rpogtu  rsync://pub@192.168.2.106/pub/backup/sdcard-apk/packagesOnCloud.get(v.getId()) /mnt/sdcard/tmp/ | xargs echo\n");
		out = "";
		while(true){
			   
			read = stdout.read(buffer);
		    out += new String(buffer, 0, read);
		    if(read<BUFF_LEN){
		        //we have read everything
		        break;
		    }
		}
		}catch(Exception e1) {
			e1.printStackTrace();
		}
	}*/
	int id = v.getId();
	String tmpDir = packagesOnCloud.get(id);
	String dirComp[] = tmpDir.split("/");
	process.executeCmd("rsync -progu rsync://pub@192.168.2.106/pub/backup"+tmpDir+" /mnt/sdcard/temp/");
	String outputCmd="";
	outputCmd = process.readCmd(outputCmd);
	if(dirComp[1].equals("data") || dirComp[1].equals("system"))
		process.executeCmd("pm install -f /mnt/sdcard/temp/"+dirComp[dirComp.length-1]);
	else
		process.executeCmd("pm install -s /mnt/sdcard/temp/"+dirComp[dirComp.length-1]);
	
	outputCmd="";
	outputCmd = process.readCmd(outputCmd);
	
	Log.i("Cloud:",id+"");
	}
	
}

package com.widget;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.widget.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.*;

public class ListAppsActivity extends Activity {
	
	
	
	TextView tv;
	AppsHelper appsHelper;
	ArrayList<String> appNames,packageNames;
	List<PackageInfo> pkginfo_list;
    List<ApplicationInfo> appinfo_list;
    
	ListView lv;
	NewQAAdapter adapter;
	public ImageView mMoreIv = null;
	public int mSelectedRow=0;
	String [] appNamesArray;
	LinearLayout ll;
	ManageProcess process;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lv=new ListView(this);
		ll=new LinearLayout(this);
        ll.addView(lv);
        process = new ManageProcess();
        process.acquireRoot();
        mMoreIv=new ImageView(this);
        /*
		appsHelper = new AppsHelper();
		
		appNames = appsHelper.getAllApps();
		
		packageNames = appsHelper.getAllPackages();
		
		adapter=new NewQAAdapter(this);
		
		appNamesArray =new String[appNames.size()];
		for(int i=0;i<appNames.size();i++)
		{
			appNamesArray[i]=appNames.get(i);
		}
		*/
        PackageManager  pm = getPackageManager();
		pkginfo_list = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
		appinfo_list = pm.getInstalledApplications(0);
		
        appNamesArray =new String[pkginfo_list.size()];
        for(int i=0;i<pkginfo_list.size();i++){
        	
        	
        		appNamesArray[i] = pkginfo_list.get(i).packageName;
        	
        }
        
        adapter=new NewQAAdapter(this);
		adapter.setData(appNamesArray);
		
		lv.setAdapter(adapter);
		
		
		ActionItem push =new ActionItem();
		push.setTitle("Push");
		
		ActionItem pull =new ActionItem();
		pull.setTitle("Pull");
		/*
		ActionItem sync =new ActionItem();
		sync.setTitle("Sync");
		*/
		final QuickAction mQuickAction 	= new QuickAction(this);
		
		mQuickAction.addActionItem(push);
		mQuickAction.addActionItem(pull);
		/*mQuickAction.addActionItem(sync);*/
		
		
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(int pos)
			{
				//Log.i("PACKAGE NAME",packagename);
				if (pos == 0)
				{ 	
					/*
						String x=appsHelper.getPackageAbsolutePath(packagename);
						String path=null;
						if(x.contains("@d@@@"))
						{
							path="data/";
							
						}
						else
						{
							path="mnt/";
						}
						//appsHelper.pushPackage(x.substring(6, x.length()), "sindhu@192.168.2.103:/home/sindhu/Desktop/a/datafiles/"+path);
						appsHelper.pushPackage(x.substring(6, x.length()), "rsync://pub@192.168.2.106/pub/backup/"+path);
					*/
					String bytesRead = "";
					String dataDirectory;
					dataDirectory= pkginfo_list.get(mSelectedRow).applicationInfo.dataDir;
					process.executeCmd("rsync -progu "+dataDirectory+" rsync://pub@192.168.2.106/pub/backup/data/data/");
					String outputCmd="";
					bytesRead = process.readCmd(outputCmd);
					
				} 
				else if (pos == 1)
				{
					/*int ret=0;
					String path=null;
					ret=appsHelper.toPullPackage(packagename);
					if(ret==1)
					{
						path="data/";
					}
					else
					{
						path="mnt/";
					}
					//appsHelper.pullPackage(packagename,"/sdcard/newnew/datafiles/"+path,"sindhu@192.168.2.103:/home/sindhu/Desktop/a/datafiles/"+path);
					appsHelper.pullPackage(packagename,"/sdcard/newnew/datafiles/"+path,"rsync://pub@192.168.2.106/pub/backup/"+path);
					*/
					String bytesRead = "";
					String dataDirectory;
					String components[];
					dataDirectory= pkginfo_list.get(mSelectedRow).applicationInfo.dataDir;
					components = dataDirectory.split("/");
					process.executeCmd("rsync -progu rsync://pub@192.168.2.106/pub/backup/data/data/"+components[components.length-1]+" /data/data/");
					String outputCmd="";
					bytesRead = process.readCmd(outputCmd);
					process.executeCmd("ls -l -R /data/data/"+components[components.length-1]);
					outputCmd="";
					bytesRead = process.readCmd(outputCmd);
					String []lsOutput;
					lsOutput = bytesRead.split("\r\n");
					Pattern root = Pattern.compile("[0-9a-zA-Z.//_-]+:$");
					Pattern child = Pattern.compile("([0-9a-zA-Z.//_-]+)$");
					ArrayList <String> dataFiles = new ArrayList <String>();
					String root_string="";
					
					for(int i=1;i<lsOutput.length-1;i++)
					{
						Matcher r = root.matcher(lsOutput[i]);
						Matcher c = child.matcher(lsOutput[i]);
						String child_string = "";
						if(r.find()){
							root_string = r.group(0);
							root_string = root_string.substring(0,root_string.length()-1);
							dataFiles.add(root_string);
							}
						else if(c.find()){
							child_string = c.group(0);
							child_string = root_string+"/"+child_string;
							dataFiles.add(child_string);
						}
					}
					
					int uid = pkginfo_list.get(mSelectedRow).applicationInfo.uid;
				
					String fileList="";
					for(int i=0;i<dataFiles.size();i++){
						fileList += dataFiles.get(i) + " ";
						if(i%30==0)
						{
							process.executeCmd("chown "+uid+"."+uid+" "+fileList);
							Log.i("Cloud","chown "+uid+"."+uid+" "+fileList);
							fileList = "";
						}
						//outputCmd="";
						//bytesRead = process.readCmd(outputCmd);
					}
					if(!fileList.equals("")){
						process.executeCmd("chown "+uid+"."+uid+" "+fileList);
						Log.i("Cloud","chown "+uid+"."+uid+" "+fileList);
						fileList = "";
					}
					
					
				
				}
				
				/*else if (pos == 2)
				{ 
					// julies will do;
				}*/	
			}
		});
		
		/*mQuickAction.setOnDismissListener(new PopupWindow.OnDismissListener() {			
			@Override
			public void onDismiss() {
				mMoreIv.setImageResource(R.drawable.ic_list_more);
			}
		});*/
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mSelectedRow = position; //set the selected row
				
				mQuickAction.show(view);
				//mMoreIv.setImageResource(R.drawable.ic_list_more_selected);
				
				
				
			}
		});
		
		
        setContentView(ll);  
		//getAllApps();*/
	}

	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*
		int id = v.getId();
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		int packageId = id/3;
		String packagename = packageNames.get(packageId);
		switch(id%3)
		{
			case 0:
				
				appsHelper.pushPackage(packagename, appsHelper.getPackageAbsolutePath(packagename), "nikhil@192.168.2.106:/home/nikhil/a");
				break;
			case 1:
				appsHelper.pullPackage(packagename,"/sdcard/newnew/","nikhil@192.168.2.106:/home/nikhil/a/");
				break;
			case 2:
				alertDialog.setMessage("Clicked sync button of packageId:"+packageId);
				alertDialog.show();
		}
	*/	
	}
	
}

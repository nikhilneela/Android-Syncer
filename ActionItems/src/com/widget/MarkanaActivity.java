package com.widget;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.google.ase.Exec;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MarkanaActivity extends Activity implements OnClickListener{
    
	/** Called when the activity is first created. */
	ManageProcess process;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
        process = new ManageProcess();
        process.acquireRoot();
        setContentView(R.layout.main);
    }

	@Override
	public void onClick(View v) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		String bytesRead="";
		switch(v.getId()){
			
			case R.id.apkSync:
				
				process.executeCmd("rsync -progu /data/app/*.apk rsync://pub@192.168.2.106/pub/backup/data/app/");
				String outputCmd="";
				bytesRead = process.readCmd(outputCmd);
				process.executeCmd("rsync -progu /system/app/*.apk rsync://pub@192.168.2.106/pub/backup/system/app/");
				outputCmd = "";
				bytesRead = process.readCmd(outputCmd);
				process.executeCmd("rsync -progu /mnt/asec/ rsync://pub@192.168.2.106/pub/backup/mnt/asec/");
				outputCmd = "";
				bytesRead = process.readCmd(outputCmd);
				alertDialog.setMessage("All applications are synchronized");
				//This synchronizes all apks
				//AppsHelper appsHelper;
				//appsHelper = new AppsHelper();
				//appsHelper.pushPackage("/data/app/", "rsync://pub@192.168.2.106/pub/backup/phone-apk/");
				//appsHelper.pushPackage("/mnt/asec/", "rsync://pub@192.168.2.106/pub/backup/sdcard-apk/");
				//
				
				//alertDialog.show();
			break;
			
			case R.id.dataSync:
				
			try {
				Class testClass = Class.forName("com.widget.ListAppsActivity");
				Intent testIntent = new Intent(MarkanaActivity.this,testClass);
				//process.close();
				startActivity(testIntent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
			case R.id.appsOnCloud:
				try {
					Class testClass = Class.forName("com.widget.NewAppsOnCloud");
					Intent testIntent = new Intent(MarkanaActivity.this,testClass);
					//process.close();
					startActivity(testIntent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
			
			case R.id.installedApps:
				process.executeCmd("rsync -proguP /data/data/ rsync://pub@192.168.2.106/pub/backup/data/data/");
				outputCmd="";
				bytesRead = process.readCmd(outputCmd);

				alertDialog.setMessage("All data Synced!!!");
				alertDialog.show();
			break;
		}
	}
	
}
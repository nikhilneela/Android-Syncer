package com.widget;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.Log;
import android.widget.*;

public class AppsHelper {

	private static final int BUFF_LEN = 1024;
	private ArrayList<String> appNames,packageNames;
	private Process process;
	private DataOutputStream os;
	private FileInputStream fstream;
	private DataInputStream in;
	private BufferedReader br;
	String packages[];
	
	AppsHelper() {
		appNames = new ArrayList<String>();
		packageNames = new  ArrayList<String>();
		get();
	}
	
	public ArrayList<String> getAllApps() {
		return appNames;
	}
	
	public ArrayList<String> getAllPackages(){
		//get();
		return packageNames;
	}
	public int toPullPackage(String packageName)
	{
		int flag = 0;
		try
		{
			fstream=new FileInputStream("/sdcard/temp/filelist1.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			
			while((line=br.readLine())!=null)
			{
				
				int index = line.indexOf("-");
				line = line.substring(0, index);
				if(line.contains(packageName)) {
					
					flag = 1;
					break;
				}
			}
			
			
			br.close();
			in.close();
			fstream.close();
			Log.i("$$$ FLAG : :",flag+" ");
			return flag;
			
		}
		catch(Exception ex)
		{
			ex.getStackTrace();
			return flag;
		}
	}
	
	public String getPackageAbsolutePath(String packageName) {
		
		String absolutePath = null;
		try{
			Log.i("HELLLLLOOO","HELLOO");
			fstream = new FileInputStream("/sdcard/temp/filelist1.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			
			int flag = 0;
			
			while((line=br.readLine())!=null)
			{
				
				int index = line.indexOf("-");
				line = line.substring(0, index);
				if(line.equals(packageName)) {
					flag = 1;
					break;
				}
			}
			
			br.close();
			in.close();
			fstream.close();
			
			if(flag==1)
			{
				absolutePath = "@d@@@@/data/data/"+packageName.trim();
			}
			else
			{
				//Log.i("APPSHELPER PACKAGE NAME",packageName.trim());
				fstream=new FileInputStream("/sdcard/temp/filelist2.txt");
				in=new DataInputStream(fstream);
				BufferedReader sdbr=new BufferedReader(new InputStreamReader(in));
				String line2;
				
				while((line2=sdbr.readLine())!=null)
				{
					Log.i("STARTED COMPARISION",line2);
					Log.i("STARTED COMPARISION",packageName);
					if(line2.contains(packageName))
					{
						absolutePath = "@m@@@/mnt/asec/"+line2.trim();
						Log.i("GET ABSOLUTE PATH",absolutePath);
						break;
					}
				
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return absolutePath;
	}
	
	public void pushPackage(String absolutePath,String dest){
		try{
			process = Runtime.getRuntime().exec("/system/bin/su");
			os = new DataOutputStream(process.getOutputStream());
			Log.i("ABSOLUTE PATH IN PUSH PACKAGE -APPS HELPER", absolutePath);
			//String temp = "rsync -rltD --no-p -e \" ssh -i /sdcard/newnew/sindhu/dropbear_rsa_host_key \"" + " " + absolutePath+" " + dest + "\n";
			String temp = "rsync -rpogtu" + " " + absolutePath+" " + dest + "\n";
			os.writeBytes(temp);
			Log.i("RSYNC COMMAND IN PUSH",temp);
			os.flush();
			os.close();
			process.waitFor();
			process.destroy();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void pullPackage(String packageName,String absolutePath,String dest){
		try{
			process = Runtime.getRuntime().exec("/system/bin/su");
			os = new DataOutputStream(process.getOutputStream());
			
			//String temp = "rsync -rltD --no-p -e \" ssh -i /sdcard/newnew/sindhu/dropbear_rsa_host_key \"" + " " + dest + packageName.trim()+"*"+" " + absolutePath + "\n";
			String temp = "rsync -rpogtu" + " " + dest + packageName.trim()+"*"+" " + absolutePath + "\n";
			Log.i("RSYNC COMMAND PULL",temp);
			
			os.writeBytes(temp);
			os.flush();
			os.close();
			
			process.waitFor();
			process.destroy();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> appsOnCloud(String dest){
		/*
		ArrayList<String> appsOnCloud = new ArrayList<String>();
		try{
			process = Runtime.getRuntime().exec("/system/bin/su");
			os = new DataOutputStream(process.getOutputStream());
			
				
			String temp;
			//temp="rsync -rltD --no-p -e \" ssh -i /sdcard/newnew/sindhu/dropbear_rsa_host_key \" sindhu@192.168.2.103:/home/sindhu/Desktop/a/list.txt /sdcard/temp/"+"\n";
			temp="rsync -rpogtu" + " " + dest.trim() +"list.txt"+" "+"/sdcard/temp/"+"\n";
			Log.i("testString",temp);
			os.writeBytes(temp);
			os.flush();
			os.close();
			process.waitFor();
			process.destroy();
			Log.i("I WANT TO CHECK THIS TODAY",process.exitValue()+"");
	        //if(process.exitValue()==0)
	        //{
	        	fstream = new FileInputStream("/sdcard/temp/list.txt");
	        	in = new DataInputStream(fstream);
	        	br = new BufferedReader(new InputStreamReader(in));
		
	        	String line;			
	        	while((line=br.readLine())!=null)
	        	{
	        		appsOnCloud.add(line);
	        		//appsOnCloud.add(line.substring(line.lastIndexOf(".")+1));
	        	}
	        	
	        //}
	        //else
	        //{
	        	//appsOnCloud=null;
	        
	        //	return null;
	        //}
		}// end of try
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			return appsOnCloud;
		}
		*/
		ArrayList<String> appsOnCloud = new ArrayList<String>();
		try {
			
			Process p = null;
			p = Runtime.getRuntime().exec(new String[]{"su", "-c", "system/bin/sh"});
			DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
			stdin.writeBytes("rsync  rsync://pub@192.168.2.106/pub/backup/phone-apk/ | cut -c 44- | grep [[:alpha:]]\n");
			stdin.flush();
			InputStream stdout = p.getInputStream();
			byte[] buffer = new byte[BUFF_LEN];
			int read = 0;
			String out = new String();
			String packages[];
			while(true){
				if(stdout.available()>0) {
					read = stdout.read(buffer);
					out += new String(buffer, 0, read);
				}
				if(read<BUFF_LEN){
		        
					break;
				}
			}
			
			stdin.writeBytes("rsync  rsync://pub@192.168.2.106/pub/backup/sdcard-apk/ | cut -c 44- | grep [[:alpha:]]\n");
			stdin.flush();
			while(true){
				for(int i=0;i<1000 && stdout.available() <= 0;i++){
				if(stdout.available()>0){
				read = stdout.read(buffer);
				out += new String(buffer, 0, read);
				}
				}
				if(read<BUFF_LEN){
		        
					break;
				}
				}
			
			packages = out.split("\n");
			for(int i=0;i<packages.length;i++){
				appsOnCloud.add(packages[i]);
			}
			stdin.flush();
			stdin.close();
			stdout.close();
			p.destroy();
			}catch(Exception e){
				e.printStackTrace();
			}
		return appsOnCloud;
}
	
	
	
	
	private void get(){
		/*try{
			
			process = Runtime.getRuntime().exec("/system/bin/su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("/system/bin/ls /data/app | grep .apk  > /sdcard/temp/filelist1.txt \n");
			os.writeBytes("/system/bin/ls /mnt/asec > /sdcard/temp/filelist2.txt \n");
			os.writeBytes("/system/bin/cat /sdcard/temp/filelist1.txt /sdcard/temp/filelist2.txt > /sdcard/temp/filelist.txt\n");
			os.flush();
			
			
			fstream = new FileInputStream("/sdcard/temp/filelist.txt");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			
			String line;			
			while((line=br.readLine())!=null)
			{
				int index = line.indexOf("-");
				line = line.substring(0, index);
				Log.i("**** PN****",line);
				packageNames.add(line);
				appNames.add(line.substring(line.lastIndexOf(".")+1));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}
	*/
	Process p = null;
	DataOutputStream stdin;
	InputStream stdout;
		try {
			
			p = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh /system/bin/ls /mnt/asec | grep [[:alpha:]]"});
			stdin = new DataOutputStream(p.getOutputStream());
			//stdin.writeBytes("ls /mnt/asec | grep [[:alpha:]]\n");
			stdin.flush();
			stdout = p.getInputStream();
			byte[] buffer = new byte[BUFF_LEN];
			int read = 0;
			String out = new String();
			while(true){
				//for(int i=0;i<1000 && stdout.available() <= 0;i++){
				//if(stdout.available() > 0) {
					
				
					read = stdout.read(buffer);
					out += new String(buffer, 0, read);
				//}
				//}
					if(read<BUFF_LEN){
		        
						break;
					}
				
			}
			
			stdin.writeBytes("ls /data/app | grep [[:alpha:]]\n");
			stdin.flush();
			//stdout.wait(1000);
			while(true){
				for(int i=0;i<1000 && stdout.available()<=0 ;i++)
				{
				if(stdout.available()>0)
				{
					read = stdout.read(buffer);
					out += new String(buffer, 0, read);
				}
				}
				if(read<BUFF_LEN){
		        
					break;
				}
			}
			packages = out.split("\n");
			for(int i=0;i<packages.length;i++){
				appNames.add(packages[i]);
				packageNames.add(packages[i]);
			}
			stdin.flush();
			stdin.close();
			stdout.close();
			p.destroy();
			}catch(Exception e){
				e.printStackTrace();
				p.destroy();
			}
		
		
		
		}		
}

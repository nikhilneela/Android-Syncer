package com.widget;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.ase.Exec;

public class ManageProcess {

	private FileDescriptor shellFd;
	private final static int BUFF_LEN=1024;
	private DataInputStream stdin;
	private DataOutputStream stdout;
	private byte []buffer;
	ManageProcess() {
		int[] pids = new int[1];
		try {
			shellFd = Exec.createSubprocess("/system/bin/sh", "-", null, pids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final int shellPid = pids[0];	
		FileInputStream is = new FileInputStream(shellFd);
		stdin = new DataInputStream(is);
		FileOutputStream os = new FileOutputStream(shellFd);
		stdout = new DataOutputStream(os);
		buffer = new byte[BUFF_LEN];
	}
	
	void acquireRoot() {
		executeCmd("su");
	}
	void close() {
		try {
			stdout.close();
			stdin.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	String readCmd(String out) {
		int bytesRead = 0;
		while(true){
				
			
				int read;
				try {
					read = stdin.read(buffer);
				
				bytesRead +=read;
				out += new String(buffer, 0, read);
				if(read<BUFF_LEN){
	        
					break;
				}
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			
		}
		return out;
	}
	
	void executeCmd(String buffer) {
		try {
			stdout.writeBytes(buffer+"\n");
			synchronized (stdout) {
				stdout.wait(2000);
			}
			
			stdout.flush();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
		
	

}

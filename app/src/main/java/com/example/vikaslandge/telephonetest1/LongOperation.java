package com.example.vikaslandge.telephonetest1;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

public class LongOperation extends AsyncTask<Void, Void, String> {
	
	String to,sub,msg ;
	
	
	public LongOperation(String to, String sub, String msg) {
	
						this.to=to;
						this.sub=sub;
						this.msg=msg;	
	
	}




	@Override
	protected String doInBackground(Void... params) {
		try{
		GMailSender sender = new GMailSender("vikasslandge@gmail.com","XXXXXXXXX");
        sender.sendMail(sub,msg,to,to);
		} 
		catch(Exception e){Log.e("error",e.getMessage(),e);return "Email Not Sent";}
		return "Email Sent"; 
	} 
	
	
	

	@Override
	protected void onPostExecute(String result) 
	{
	}
	@Override
	protected void onPreExecute() 
	{
	}
	
	@Override
	protected void onProgressUpdate(Void... values) 
	{
	}
}
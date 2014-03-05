package zappos;



import static java.lang.System.err;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
public class ZapposHTTPrequest {
	
	public static int NO_OF_PRODUCTS;
	public static int GIVEN_PRICE;
	File file;
	BufferedReader br;
	public ZapposHTTPrequest()
	{
	 file = new File("data.txt");
	 try {
		 file.createNewFile();
		br = new BufferedReader(new FileReader("data.txt"));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	 
	private final String USER_AGENT = "Mozilla/5.0";
 
	public static void main(String[] args) throws Exception {
		
		if (args.length < 2)
		{
			err.println("Not enough arguments.");
			return;
		}
		
		NO_OF_PRODUCTS = Integer.parseInt(args[0]);
		GIVEN_PRICE = Integer.parseInt(args[1]);
		
	
 
		ZapposHTTPrequest zappo = new ZapposHTTPrequest();
		if(zappo.file.exists())
		{
			zappo.file.delete();
		}
 
		System.out.println("Testing 1 - Send Http GET request");
		zappo.sendGet();
		float temp = 0;
		int a[] = new int[NO_OF_PRODUCTS];
		String temp1[] = new String[NO_OF_PRODUCTS];
		zappo.getresult (a,0, temp,temp1);
		
 
	}
	
	
	public void getresult (int i[], int stage, float sum, String url1[])
	{
		List<String> list = new ArrayList<String>();

		String sCurrentLine;
		try {
			
			   if (stage == NO_OF_PRODUCTS)
			   {
				   if(Math.ceil(sum) == GIVEN_PRICE)
				   {	
					   for (int t = 0; t < NO_OF_PRODUCTS; t++){
						   System.out.println(url1[t]);
						  
					   }
					   		System.out.println("************");
				   }
					 return;
					 
				 }
			   String[] arr;
			while ((sCurrentLine = br.readLine()) != null) 
			   {
				float sum1 =0;
				String delims = "[ ]+";
				arr = sCurrentLine.split(delims);
					
				 sum1 =sum+ Float.parseFloat(arr[0].substring(1)) ;
				 i[stage] = (int) Math.ceil( Float.parseFloat(arr[0].substring(1)));
				 url1[stage] = arr[1];
				 getresult (i,stage+1, sum1, url1);
						
			   }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
 
	// HTTP GET request
	public void sendGet() throws Exception {
		int j = 1;
		boolean flag = true;
		while(flag)
		{
		String url = "http://api.zappos.com/Search?term=&sort={%22price%22:%22asc%22}" + "&page=" + j + "&limit=100&key=b05dcd698e5ca2eab4a0cd1eee4117e7db2a10c4";
 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) 
		{
			response.append(inputLine);
			
		}
		in.close();
 
		//print result
		
		//System.out.println(response.toString());
		
		//creating file 1
		//File file = new File("data.txt");
		 
		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		
				
		JSONObject obj1 = new JSONObject(response.toString());

		List<String> list = new ArrayList<String>();
		JSONArray array = obj1.getJSONArray("results");
		
		
		
		for(int i = 0 ; i < array.length() ; i++)
		{
		   //System.out.println(array.getJSONObject(i));
		   list.add(array.getJSONObject(i).getString("price"));
		   if(Float.parseFloat((array.getJSONObject(i).getString("price")).substring(1, array.getJSONObject(i).getString("price").length())) > GIVEN_PRICE)
		   {
			   flag = false;
			   break;
		   }
		   
		   bw.append(array.getJSONObject(i).getString("price") + "  " + array.getJSONObject(i).getString("productUrl") + "\r\n");
		   
		}
		bw.close();
		
	j++;
	
	}
		
		
	}

 
}
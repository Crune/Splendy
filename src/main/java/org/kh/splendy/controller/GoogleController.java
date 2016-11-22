package org.kh.splendy.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class GoogleController {
	@RequestMapping("/main/google")
	public String helloGoogle(HttpServletRequest request) {
		String appKey        = "768530434374-au3vrrllnhr3a96h3i6utec28filmqcn.apps.googleusercontent.com";
		String appSecret     = "VR7RGj1x7ET3dG8eMUlMn_jj";
		String redirect_uri = "http://localhost/main/google2";
		
		String code = request.getParameter("code");
		
		URL AccessTokenURL = null;
		try {
			AccessTokenURL = new URL("https://accounts.google.com/o/oauth2/token");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection http = null;
		try {
			http = (HttpURLConnection)AccessTokenURL.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		http.setDoOutput(true);
		http.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("grant_type=authorization_code&code="+code+"&client_id="+appKey+"&client_secret="+appSecret+"&redirect_uri="+redirect_uri);

		OutputStreamWriter outStream = null;
		try {
			outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		PrintWriter writer = new PrintWriter(outStream);
		writer.write(buffer.toString());
		writer.flush();
		InputStreamReader inputStream = null;
		try {
			inputStream = new InputStreamReader(http.getInputStream(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		BufferedReader bufferReader = new BufferedReader(inputStream);
		StringBuilder builder = new StringBuilder();
		String str;
		try {
			while((str = bufferReader.readLine()) != null){
			 builder.append(str+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String json_result = builder.toString();
		System.out.println(json_result);
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject)jsonParser.parse(json_result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		String id_token = (String)jsonObject.get("id_token");
		System.out.println("id_token = "+id_token);
		
		String[] spilted = id_token.split("\\.");
		
		System.out.println(spilted[1]);
		
		///////////////////////////////////////////////
		
		/*GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
		GoogleSignInAccount acct = result.getSignInAccount();
		String personName = acct.getDisplayName();
		String personGivenName = acct.getGivenName();
		String personFamilyName = acct.getFamilyName();
		String personEmail = acct.getEmail();
		String personId = acct.getId();
		Uri personPhoto = acct.getPhotoUrl();*/
		
		return "user/hello";
	}
	
}

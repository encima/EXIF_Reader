package com.encima;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class EXIFReader {

	String[] ignoreStrings  = new String[] {"ExifTool", "Maker Note"};
	
	public EXIFReader() {
		
	}
	
	public Map<String, String> readTags(String path) {
		try {
			Process p  = Runtime.getRuntime().exec("exiftool " + path);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			Map<String, String> exif = new HashMap<String, String>();
			while((line = br.readLine()) != null) {
				String[] split = line.split(":");
				//We want to make sure that the output is only a key/value pair, could also append anything after index 0 to the value
				if(split.length == 2) {
					exif.put(formatString(split[0]), split[1].trim());
				}
			}
			return exif;
		} catch (IOException e1) {
			System.err.println("Error: Likely that system does not contain the ExifTool Perl Library, check it is on your PATH");
			return null;
		}
	}
	
	public String formatString(String exif) {
		exif = exif.trim().toLowerCase();
		for (String s : ignoreStrings) {
			if(exif.contains(s.trim().toLowerCase())) exif = exif.replace(s.toLowerCase(), "").trim();
		}
		return exif;
	}
	
	public static void main(String[] args) {
		EXIFReader er = new EXIFReader();
		Map<String, String> tags = er.readTags("/Users/encima/Dropbox/Projects/PhD/Java/GSN_DGFC/webapp/images/DGL6/27/17.05.11/100RECNX/IMG_0076.JPG");
		System.out.println(tags.entrySet());
	}
}

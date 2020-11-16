package hspt.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Util {
	
    private static final char DEFAULT_SEPARATOR = ';';
    private static final char DEFAULT_QUOTE = '"';

    
    public static List<String[]> readCSV(InputStream is) throws IOException{

        BufferedReader br = null;
        String line = "";
        ArrayList<String[]> result = new ArrayList<String[]> ();

        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                List<String> fields = parseLine(line, DEFAULT_SEPARATOR, DEFAULT_QUOTE); // line.split(delimiter);
                
                
                result.add(fields.toArray(new String[0]));
            }

        } finally {
            if (br != null) {
                    br.close();
            }
        }
        
        return result;

    }
   
	public static void writeCSV(String fileName, List<String[]> lines) throws IOException {
		FileWriter fw = null;

		try {
			fw = new FileWriter(new File(fileName));

			for (String[] line : lines) {
				int count = 0;
				for (String field : line) {
					if (0 != count)
						fw.write(";");
					fw.write(field);
					count++;
				}
				fw.write("\n");
			}

		} finally {
			if (fw != null) {
				fw.close();
			}
		}

	}   
    
    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null || cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }
    
    
    public static String objToJson(Object obj) throws Exception {
    	 ObjectMapper mapper = new ObjectMapper();
         mapper.enable(SerializationFeature.INDENT_OUTPUT);
         String json = mapper.writeValueAsString(obj);
         return json;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object jsonToObject(String json, Class clazz) throws Exception {
    	ObjectMapper mapper = new ObjectMapper();
    	Object obj = mapper.readValue(json, clazz);
    	
    	return obj;
    	
    }
    
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object readJson(String fileName, Class clazz) throws IOException{

    	ObjectMapper mapper = new ObjectMapper();

        // Read JSON file and convert to java object
        FileInputStream fileInputStream = new FileInputStream(fileName);
        Object obj = mapper.readValue(fileInputStream, clazz);
        fileInputStream.close();
        
        return obj;
    }
    
    public static void writeJson(String fileName, Object obj) throws IOException{
    	ObjectMapper mapper = new ObjectMapper();
    	
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        mapper.writerWithDefaultPrettyPrinter().writeValue(fileOutputStream, obj);
        fileOutputStream.close();    	
    }
   



}

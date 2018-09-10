package SheetPackageTest;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
 
public class SheetsQuickstart {
    
	private static final String APPLICATION_NAME = "CSC131";
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials//sheets.googleapis.com-java-quickstart.json");
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT;
    private static final List<String> SCOPES = Arrays.asList( SheetsScopes.SPREADSHEETS );
    private static String spreadsheetId = "1HEkPX-wEowUAOSH3rAzwLOndnAMZ_WsCkxR_aonbyu8";
   
 static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    

    public static Credential authorize() throws IOException {
    	String respath = "/client_secret.json";
    	InputStream in = SheetsQuickstart.class.getResourceAsStream(respath);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    } 

    //returns today's date
    public static String getDate() throws IOException{
    	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
        Date date = new Date();  
        return formatter.format(date);
    }
    
    //Random number generator
    public static String randomKey() throws IOException{
    	Random rand = new Random();
    	int randomKey = rand.nextInt(10000-1000) + 1000;
    	return "" + randomKey;
    }
    
    /*
     *returns a time x minutes in the future (HH:mm:ss)
     */
    public static String timeLimit(int x) throws IOException{
    	//returns a time m minutes in the future
    	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
    	Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, x);
        return formatter.format(calendar.getTime());
    }
    
    /*
     *Initializes date/key/timer or gets timer and key in a String array
     *array size: 2 (for teacher only)
     *array[0] is timer, array[1] is key
     */
    public static String[] getTkey(String sheetName, int sheetID)throws IOException{
    	Sheets service = getSheetsService();
    	//targeted range for date and key
    	final String range = "'" + sheetName + "'!F:P";
        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
        //puts in a 2d list
        List<List<Object>> element = response.getValues();
        
        String time = "";
        String key = "";
        String[]arr = new String[2]; //array size 2
        int col = 5;
        
        //if no data is found in the element from the range
        if (element == null) {
        	//make a new key and update today's date and time limit
        	key = randomKey();
        	time = timeLimit(15);
        	List<Request> r = new ArrayList<>();
        	updateSheet(r, getDate(),sheetID,0,col);
        	updateSheet(r, key,sheetID,31,col);
        	updateSheet(r, time,sheetID,32,col);
        	updateSheet(r, service);
        	System.out.println("no data was there");
        }
        //some data was found in the element from the range
        else {
        	//check if the first date slot is empty
        	if (element.get(0).isEmpty()) {
        		//make a new key and update today's date and time limit
        		key = randomKey();
        		time = timeLimit(15);
        		List<Request> r = new ArrayList<>();
        		updateSheet(r, getDate(),sheetID,0,col);
            	updateSheet(r, key,sheetID,31,col);
            	updateSheet(r, time,sheetID,32,col);
            	updateSheet(r, service);
            	System.out.println("first date slot empty");
        	}
        	//first date slot not empty
        	else {
        		//find out how many dates are there
        		int i = element.get(0).size();
        		
        		//check if the last slot is today's date
        		if(element.get(0).get(i-1).equals(getDate())) {
        			try {
        				key = (String)element.get(31).get(i-1);
        				time = (String)element.get(32).get(i-1);
        				System.out.println("It's the same day");
        			}
        			catch(Exception e){ //when key and timer is missing
        				System.out.println("key is missing, making new key and timer");
        				key = randomKey();
        				time = timeLimit(15);
            			List<Request> r = new ArrayList<>();
    	        		updateSheet(r, getDate(), sheetID,0, col+i-1);
    	        		updateSheet(r, key, sheetID,31, col+i-1);
    	        		updateSheet(r, time, sheetID,32,col+i-1);
    	        		updateSheet(r, service);
        			}
        		}
        		//when a different date found
        		else {
        			//set today's date, time limit, and key in the next slot
        			key = randomKey();
        			time = timeLimit(15);
        			List<Request> r = new ArrayList<>();
	        		updateSheet(r, getDate(),sheetID,0, col+i);
	        		updateSheet(r, key,sheetID,31, col+i);
	        		updateSheet(r, time,sheetID,32,col+i);
	        		updateSheet(r, service);
	        		System.out.println("different date, making new key");
        		}
        	}
        }
        //returns timer and key for today
        arr[0] = time;
        arr[1] = key;
        return arr;
    }
    
    /*
     *Resets the time limit and the Key for today's date in google sheets
     *array size: 2 (for teacher only)
     *array[0] is timer, array[1] is key
     */
    public static String[] resetTkey(String sheetName, int sheetID)throws IOException{
    	Sheets service = getSheetsService();
    	//targeted range for date and key
    	final String range = "'" + sheetName + "'!F:P";
        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
        //2d list
        List<List<Object>> element = response.getValues();
        
        String time = "";
        String key = "";
        String[]arr = new String[2];
        int col = 5;
        //if no data is found in the element from the range
        if (element == null) {
        	//make a new key and update today's date and time limit
        	key = randomKey();
        	time = timeLimit(15);
        	List<Request> r = new ArrayList<>();
        	updateSheet(r, getDate(),sheetID,0,col);
        	updateSheet(r, key,sheetID,31,col);
        	updateSheet(r, time,sheetID,32,col);
        	updateSheet(r, service);
        	System.out.println("no data was there");
        }
        //some data was found in the element from the range
        else {
        	//check if the first date slot is empty
        	if (element.get(0).isEmpty()) {
        		//make a new key and update today's date and time limit
        		key = randomKey();
        		time = timeLimit(15);
        		List<Request> r = new ArrayList<>();
        		updateSheet(r, getDate(),sheetID,0,col);
            	updateSheet(r, key,sheetID,31,col);
            	updateSheet(r, time,sheetID,32,col);
            	updateSheet(r, service);
            	System.out.println("first date slot empty");
        	}
        	//first date slot not empty
        	else {
        		//find out how many dates are there
        		int i = element.get(0).size();
        		//resets timer and key for the last column
        		key = randomKey();
    			time = timeLimit(15);
    			List<Request> r = new ArrayList<>();
        		updateSheet(r, getDate(),sheetID,0, col+i-1);
        		updateSheet(r, key,sheetID,31, col+i-1);
        		updateSheet(r, time,sheetID,32,col+i-1);
        		updateSheet(r, service);
        		System.out.println("resetting new key");
        	}
        }
        //returns timer and key for today
        arr[0] = time;
        arr[1] = key;
        return arr;
    }
    
    /*
     *student check-in method
     *returns 
     *String "t" or "f" (true/false) at array[0]
     *String message at array[1]
     *String name of student at array[2]
     */
    public static String[] checkIn(String sheetName, int sheetID, String keyInput, String studentID, String studentNote)throws IOException {
    	String[]arr = new String[3];
    	arr[0]=""; //valid or invalid. {t} or {f}
    	arr[1]=""; //message
    	arr[2]=""; //gets student's name
    	Sheets service = getSheetsService();
    	final String range = "'" + sheetName + "'!A:Z";
        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
        List<List<Object>> element = response.getValues();
        
        if(element == null) { //entire sheet is empty
        	arr[0] = "f"; arr[1] = "ERROR: empty sheet.";
        }
        else {
        	if(element.get(0).isEmpty()) { //first row empty
        		arr[0] = "f"; arr[1] = "ERROR: empty field.";
        	}
        	else {
        		int col = element.get(0).size();
        		
        		if(col < 6) { //first date slot empty
        			arr[0] = "f"; arr[1] = "ERROR: check-in unavailable";
        		}
        		else {
        			if(element.get(0).get(col-1).equals(getDate())) { //date slot is today
        				try { //checking the key and timer
            				String key = (String)element.get(31).get(col-1);
    	        			String timer = (String)element.get(32).get(col-1);
    	        			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    	        			Date timeNow = formatter.parse(timeLimit(0));
    	        			Date timeLimit = formatter.parse(timer);
    	        			if( (timeNow.compareTo(timeLimit) < 0) && keyInput.equals(key)) { //valid time and key
    	        				//check for valid SID
    	        				try {
    	        					int k = element.size();
	    	        				for(int i = 0; i < k; i++){
	    	        					//element.get(row).get(column)
	    	        					if(element.get(i).get(2).equals(studentID)) { //finds SID
	    	        						arr[0] = "t"; arr[1] = "checked in successfully";
	    	        						arr[2] = (String)element.get(i).get(0); //get name
	    	        						List<Request> r = new ArrayList<>();
	    	        						if(studentNote.equals(null) || studentNote.equals("")){
	    	        							updateSheet(r, "X", sheetID, i, col-1);
	    	        							updateSheet(r, service);
	    	        						}
	    	        						else {
	    	        							updateSheet(r, studentNote, sheetID, i, col-1);
	    	        							updateSheet(r, service);
	    	        						}
	    	        						i=k; //exit loop
	    	        					}
	    	        					
	    	        				}
    	        				}catch(IndexOutOfBoundsException e){
	        						arr[0] = "f"; arr[1] = "invalid SID";
	        					}
    	        				
    	        			}
    	        			else {
    	        				arr[0] = "f"; arr[1] = "invalid key";
    	        			}
            			}
            			catch (Exception e) {
            				arr[0] = "f"; arr[1] = "missing timer or key";
            			}
        			}
        			else {
        				arr[0] = "f"; arr[1] = "too early, not yet";
        			}
        		}
        	}
        }
    	
        if(arr[0].equals("")) {
        	arr[0] = "f"; arr[1] = "ERROR";
        }

    	return arr;
    }
    
    /*
     * Updates a single cell data (1 API call per use)
     */
    public static void updateSheet(String stringValue, int sheetId, int rowIndex, int columIndex)throws IOException{
    	Sheets service = getSheetsService();
        List<Request> requests = new ArrayList<>();
        //Adding DATE to row and column
        List<CellData> values = new ArrayList<>();
        values.add(new CellData().setUserEnteredValue(new ExtendedValue().setStringValue((stringValue))));
        requests.add(new Request()
        		.setUpdateCells(new UpdateCellsRequest()
        		.setStart(new GridCoordinate().setSheetId(sheetId).setRowIndex(rowIndex).setColumnIndex(columIndex))
        		.setRows(Arrays.asList(new RowData().setValues(values)))
        		.setFields("userEnteredValue,userEnteredFormat.backgroundColor")));     
        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);
     	service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
    }
    
    /*
     * updates multiple values in the google sheet with one API call (part1)
     */
    public static void updateSheet(List<Request> request, String stringValue, int sheetId, int rowIndex, int columIndex)throws IOException {
    	//for multiple cell updates
        List<CellData> values = new ArrayList<>();
        values.add(new CellData().setUserEnteredValue(new ExtendedValue().setStringValue((stringValue))));
        request.add(new Request()
        		.setUpdateCells(new UpdateCellsRequest()
        		.setStart(new GridCoordinate().setSheetId(sheetId).setRowIndex(rowIndex).setColumnIndex(columIndex))
        		.setRows(Arrays.asList(new RowData().setValues(values)))
        		.setFields("userEnteredValue,userEnteredFormat.backgroundColor")));
    }
    
    /*
     * updates multiple values in the google sheet with one API call (part2)
     */
    public static void updateSheet(List<Request> request, Sheets service)throws IOException{
    	//executes the update
    	BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest().setRequests(request);
     	service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
    }
    
    /*
     * Checks the login for the teacher
     */
    public static boolean checkLogin(String uname, String pword)throws IOException{
    	String dbName = "";
    	String dbWord = "";
    	String sheetName = "SHA";
    	Sheets service = getSheetsService();
    	final String range = "'" + sheetName + "'!A3:B3";
        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
        //2d list
        List<List<Object>> element = response.getValues();
    	try {
    		dbName = (String)element.get(0).get(0);
    		dbWord = (String)element.get(0).get(1);
    	}
    	catch(Exception e) {
    		System.out.println("missing username or password, register first");
    		return false;
    	}
    	
    	if(uname.equals(dbName) && makeHash256(pword).equals(dbWord)){
    		return true;
    	}
    	else {
    		return false;
    	}
    	
    }
    
    /*
     * Sets up username and password on the SHA page
     */
    public static void userSetup(String uname, String pword)throws IOException {
    	Sheets service = getSheetsService();
        List<Request> req = new ArrayList<>();
    	updateSheet(req, uname, 681712685, 2, 0);
    	updateSheet(req, makeHash256(pword), 681712685, 2, 1);
    	updateSheet(req, service);
    	
    }
    
    /*
     * Makes a hash from a string and returns hash in string
     */
    public static String makeHash256(String str) {
    	//Hash the string to bytes
    	MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    	byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
    	
    	//Byte to hex converter to get the hashed value in hexadecimal
    	StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xff & hash[i]);
        if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    public static void main(String[] args) throws IOException {
    	userSetup("DNguyen","123!@#");
    }
}


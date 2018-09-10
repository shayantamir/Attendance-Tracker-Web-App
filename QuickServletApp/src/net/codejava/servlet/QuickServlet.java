package net.codejava.servlet;
 
import java.io.IOException;
import java.io.PrintWriter;
import SheetPackageTest.SheetsQuickstart;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class QuickServlet extends HttpServlet {
    /**
     * this life-cycle method is invoked when this servlet is first accessed
     * by the client
     */
    
	//tracks the last course the teacher picked
	private String preCourse = "";

    public void init(ServletConfig config) {
        System.out.println("Servlet is being initialized");
        preCourse = "CSC131";
        
    }
 
    /**
     * handles HTTP GET request
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        PrintWriter writer = response.getWriter();
        writer.println("<html>Welcome to Attendance Tracker</html>");
    	writer.flush();  		
    }
    
    /**
     * handles HTTP POST request
     * @throws ServletException 
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws IOException, ServletException, NullPointerException {
    	
    	PrintWriter writer = response.getWriter();
    	String formName = request.getParameter("FormName");
    	RequestDispatcher rd;
        
    	/*
    	 * Get student data. Code is performed after generating key from the professor. Using the if condition
    	 * we get the form name "StudentEntry" to verify the jsp page has been activated. Using the request function
    	 * we get the parameters in the jsp text box so the student ID and student key and use those data to validate
    	 * in the back end. We also check if the comment box is empty. 
    	 * 
    	 */
    	if (formName.equals("StudentEntry")) {	
    		HttpSession session = request.getSession();
    		String paramID = request.getParameter("Student ID");
    	    String paramKey = request.getParameter("Key");
    	    String paramComment = request.getParameter("Comment");
    	    String[]arr = null;
            //course info can be found from action
    	    String action = request.getParameter("action");
    	    String[] classId = action.split(",");
    	    String course = classId[0];
    	    int sheetId = Integer.parseInt(classId[1]);
    	    if(session.getAttribute("student") == null) { // student first time checking in
    	    	arr = SheetsQuickstart.checkIn(course ,sheetId ,paramKey ,paramID ,paramComment);
    	    	if (arr[0].equals("t")) { // student input is valid
                    session.setAttribute("student", paramID);
                    request.setAttribute("stuID", paramID);
                    request.setAttribute("key", paramKey);
                    rd = request.getRequestDispatcher("Receipt.jsp");
                    rd.forward(request, response);
        	    }
        	    else {
        	    	rd = request.getRequestDispatcher("Invalid.html");
                    rd.forward(request, response);
        	    }
    	    }
    	    else { // not first time checking in
    	    	if(session.getAttribute("student").equals(paramID)) { // if student is entering the same SID
    	    		// will allow for check-in attempt
    	    		arr = SheetsQuickstart.checkIn(course ,sheetId ,paramKey ,paramID ,paramComment);
        	    	if (arr[0].equals("t")) {
        	    		//session.setAttribute("student", paramID);
                        request.setAttribute("stuID", paramID);
                        request.setAttribute("key", paramKey);
                        rd = request.getRequestDispatcher("Receipt.jsp");
                        rd.forward(request, response);
            	    }
            	    else {
            	    	rd = request.getRequestDispatcher("Invalid.html");
                        rd.forward(request, response);
            	    }
    	    	}
    	    	else {
    	    		if(paramID.equals("") || paramKey.equals("")) { // one param Key or ID is empty
    	    			rd = request.getRequestDispatcher("Invalid.html");
                        rd.forward(request, response);
    	    		}
    	    		else {
    	    			rd = request.getRequestDispatcher("AttendanceError.html");
        	    		rd.forward(request, response);
    	    		}
    	    	}
    	    }
    	
    	}
    	/*
    	 * Using request we get the teacher username and password and validate it in the back end. 
    	 */
    	else if(formName.equals("TeacherLogin")){ // Teacher.jsp switch for formName
    	    String teacher = request.getParameter("userTeacher");
    	    String password = request.getParameter("passTeacher");
            
    	    if(SheetsQuickstart.checkLogin(teacher, password)) { // login successfully
                HttpSession session = request.getSession();
                session.setAttribute("teacher", teacher);
    	    	rd = request.getRequestDispatcher("Course.jsp");
                rd.forward(request, response); 
            }
    	    else {
            	rd = request.getRequestDispatcher("Invalid.html");
                rd.forward(request, response);
	    	} 
    	}
    	/*
    	 * This is the course select page where the professor selects the section he/she would like to 
    	 * get the attendance from. Upon selection the sheetID is passed for the tabs in the google sheet
    	 * and the name of the google sheet tabs which becomes the range. We also forward the page to the
    	 * display page called RandKey where the timer, key, and manual entry button are located. 
    	 */
    	else if(formName.equals("CourseSelect")) { //Course.jsp switch for formName
    		// list parameter
    		String course = request.getParameter("myList");    	    	

    		if(course.equals("CSC20")) {
    			//arr[0] has value for the timer
    			//arr size is 2
    			String[]arr = SheetsQuickstart.getTkey("CSC20",1996570317);
    			request.setAttribute("randomKey", arr[1] );
    			request.setAttribute("timeLimit", "Key is valid till "+arr[0]);
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
	    	    preCourse = "CSC20";
    		}
    		else if(course.equals("CSC130")) {
    			String[]arr = SheetsQuickstart.getTkey("CSC130",1472870202);
    			request.setAttribute("randomKey", arr[1] );
    			request.setAttribute("timeLimit", "Key is valid till "+arr[0]);
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
	    	    preCourse = "CSC130";
    		}
    		else if(course.equals("CSC131")) {
    			String[]arr = SheetsQuickstart.getTkey("CSC131",0);
    			request.setAttribute("randomKey", arr[1] );
    			request.setAttribute("timeLimit", "Key is valid till "+arr[0]);
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
	    	    preCourse = "CSC131";
    		}
    		else if(course.equals("CSC133")) {
    			String[]arr = SheetsQuickstart.getTkey("CSC133",756897706);
    			request.setAttribute("randomKey", arr[1] );
    			request.setAttribute("timeLimit", "Key is valid till "+arr[0]);
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
	    	    preCourse = "CSC133";
    		}
    		else if(course.equals("CSC135")) {
    			String[]arr = SheetsQuickstart.getTkey("CSC135",543682871);
    			request.setAttribute("randomKey", arr[1] );
    			request.setAttribute("timeLimit", "Key is valid till "+arr[0]);
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
	    	    preCourse = "CSC135";
    		} 
    	}
    	else if(formName.equals("KeyReset")) { //RandKey.jsp switch for formName
    		if(preCourse.equals("CSC20")) {
    			String[]arr = SheetsQuickstart.resetTkey("CSC20",1996570317);
    			request.setAttribute("randomKey", arr[1] );
    			request.setAttribute("timeLimit", "Key is valid till "+arr[0]);
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
	    	    preCourse = "CSC20";
    		}
    		else if(preCourse.equals("CSC130")) {
    			String[]arr = SheetsQuickstart.resetTkey("CSC130",1472870202);
    			request.setAttribute("randomKey", arr[1] );
    			request.setAttribute("timeLimit", "Key is valid till "+arr[0]);
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
	    	    preCourse = "CSC130";
    		}
    		else if(preCourse.equals("CSC131")) {
    			String[]arr = SheetsQuickstart.resetTkey("CSC131",0);
    			request.setAttribute("randomKey", arr[1] );
    			request.setAttribute("timeLimit", "Key is valid till "+arr[0]);
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
	    	    preCourse = "CSC131";
    		}
    		else if(preCourse.equals("CSC133")) {
    			String[]arr = SheetsQuickstart.resetTkey("CSC133",756897706);
    			request.setAttribute("randomKey", arr[1] );
    			request.setAttribute("timeLimit", "Key is valid till "+arr[0]);
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
	    	    preCourse = "CSC133";
    		}
    		else if(preCourse.equals("CSC135")) {
    			String[]arr = SheetsQuickstart.resetTkey("CSC135",543682871);
    			request.setAttribute("randomKey", arr[1] );
    			request.setAttribute("timeLimit", "Key is valid till "+arr[0]);
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
	    	    preCourse = "CSC135";
    		}
    	}
    	else {
    		writer.println("Key has not been created"); // this is for extra safety just in case there
    											//was something wrong with the web page for student or professor
        	writer.flush();
    	}
    }

    /**
     * this life-cycle method is invoked when the application or the server
     * is shutting down
     */
    public void destroy() {
        System.out.println("Servlet is being destroyed");
    }
}
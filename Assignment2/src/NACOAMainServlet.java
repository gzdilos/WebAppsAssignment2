


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/***
 * This program if for the s2/2015 COMP9234 Web Applications Assignment 1
 * 
 * It is a website for search of the DBLP database
 * 
 * This class refers to XML with the DBLPHandler using the DBLPBean class (for each entry)
 * 
 * The class also updates an modifies the model through the DBLPHandler
 * 
 * 
 */

@WebServlet(name="NACOAMainServlet",urlPatterns="/start")
public class NACOAMainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static int NUM_ENTRIES_NEW_FILE = 20; 

	final static String RESULTS_FILE_LOCATION = "./Assignment2/WebContent/WEB-INF/results.xml";
	final static String MAIN_FILE_LOCATION = "./Assignment2/WebContent/WEB-INF/DBLPSmallGen.xml";
	//final static String MAIN_FILE_LOCATION = "./workspace/DBLP/WebContent/WEB-INF/smallDBLP.xml";
	final static String CART_FILE_LOCATION = "./Assignment2/WebContent/WEB-INF/cart.xml";
	/* Files for the given session */
	private ArrayList<NACOABean> resultBeans; 
	private ArrayList<NACOABean> cartBeans;
	
	/* XML Handler: abstract class for dealing with XML */
	private NACOAHandler handler;
	
	/* Data Handler: class for dealing with databases */
	private NACOADataHandler dHandler;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NACOAMainServlet() {
        super();

        resultBeans = new ArrayList<NACOABean>();
        cartBeans = new ArrayList<NACOABean>();
        handler = new NACOAHandler();
        dHandler = new NACOADataHandler();
    }
    
    /**
     * Call this function if you want to generate a number if randomly selected entries
     */
    private ArrayList<NACOABean> generateRandomBeans(int num){
    	//we dont need this right?
    	//yeah... just returns an array of beans
    	//ok this is redundant
    	//TODO: Remove
    	//generate num number of entries into an xml
    	
    	Random rand = new Random();
    	int totalEntries = handler.getNumMain();
    	
    	ArrayList<Integer> listOfNum = new ArrayList<Integer>();
    	ArrayList<NACOABean> randomEntriesList = new ArrayList<NACOABean>();
    	
    	for(int i = 0;  i < num*5; i++ ){
    		
    		int n = rand.nextInt(totalEntries-1) + 1;
    	
    		listOfNum.add(n);
    	}
    	
    	int counter = 0;
    	for (int n : listOfNum){
    		counter++;
    		
    		if(counter > 10){
    			break;
    		}
    		
    		NACOABean newBean = handler.getEntryMain(n);
    		if(newBean == null){
    			counter--;
    		} else {
    			randomEntriesList.add(newBean);
    		}
    	}
    	
    	//set to be used by main for display!
    	return randomEntriesList;
    	
    }
    

    private void performAdvancedSearch(HttpServletRequest req, HttpServletResponse res){
    	//extract variables
		String author = (String) req.getParameter("search_author");
		String title = (String) req.getParameter("search_title");
		String venue = (String) req.getParameter("search_venue");
		String year = (String) req.getParameter("search_year");
		String pubType = (String) req.getParameter("search_pubtype");
		
		try {
			//we should only do this if we want to load the main xml
			if(handler.fileExists(MAIN_FILE_LOCATION)){
				handler.loadMainXML(MAIN_FILE_LOCATION);
			}
			
			if(handler.fileExists(RESULTS_FILE_LOCATION)){
				handler.loadResultsXML(RESULTS_FILE_LOCATION);
			}
			
			//this performs the search and puts the results into a result doc
			handler.searchAdvanced(author, title, venue, year, pubType);
			
			//write results to the given file location
			handler.updateResultStoreXML(RESULTS_FILE_LOCATION);
			
		} catch (Exception e){
			System.out.println("error loading main xml:" + e );
		}
		
			
		//this then just retrieves the results from the result doc (into bean format)
		resultBeans = handler.getBeanFromResultDoc(0);
		
		//set up our bean to be displayed
		ResultPageBean viewBean = new ResultPageBean();
		viewBean.setResultBeans(resultBeans);
		
		int totalResults = handler.getNumResults();
		viewBean.setTotalResults(totalResults);
		
		
		if(totalResults > 10){
			viewBean.setMore(true);
		}
		viewBean.setCurr_page_num(1);
		viewBean.setNext_page_num(2);
		
		req.setAttribute("viewBean",viewBean);
    	
		//i guess we still need this
		//but we might change it to include a search for people?
		//as Admin can find people and ban them?
		//um that might be good too
		//that sounds like a good idea
		//um same database but different query and table
		

    }
    
    private void performBasicSearch(HttpServletRequest req, HttpServletResponse res){
    	//extract variables
    	//same thing except basic search
		String query = (String) req.getParameter("search_query");
		String searchType = (String) req.getParameter("search_category");
		
		try {
			//we should only do this if we want to load the main xml
			if(handler.fileExists(MAIN_FILE_LOCATION)){
				handler.loadMainXML(MAIN_FILE_LOCATION);
    		}
			
			//read results from the given file location
    		if(handler.fileExists(RESULTS_FILE_LOCATION)){
				handler.loadResultsXML(RESULTS_FILE_LOCATION);
    		}
    		
    		
    		if(searchType.matches(".*Author.*")){
    			handler.searchBasicAuthor(query);
    		} else if (searchType.matches(".*Publication.Title.*")){
    			handler.searchBasicTitle(query);
    		} else if (searchType.matches(".*Venue.*")){
    			handler.searchBasicVenue(query);
    		} else if (searchType.matches(".*Year.*")){
    			handler.searchBasicYear(query);
    		} else {
    			System.out.println("search type unknown");
    		}
    		//write results to the given file location
    		handler.updateResultStoreXML(RESULTS_FILE_LOCATION);
			
		} catch (Exception e){
			System.out.println("error loading main xml:" + e );
		}
		
		//Do a search and display the content (from results instead of main!)

		resultBeans = handler.getBeanFromResultDoc(0);	
		
	  	//set up our bean to be displayed
		ResultPageBean viewBean = new ResultPageBean();
		viewBean.setResultBeans(resultBeans);
		
		int totalResults = handler.getNumResults();
		viewBean.setTotalResults(totalResults);
		
		
		if(totalResults > 10){
			viewBean.setMore(true);
		}
		viewBean.setCurr_page_num(1);
		viewBean.setNext_page_num(2);
		
		req.setAttribute("viewBean",viewBean);
    	

    	//set up our bean to be displayed
		//yeah um.. ill ask on the forum but i think searching will be easy
		//SQL makes searches pretty easy I think

    }
    
    
  //we can read the URI for the specific request, and then redirect appropriately
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException	{

    	System.out.println(System.getProperty("user.dir"));
    	doPost(req,res);
    	
    	//This is the get method call... it just redirects to post
    	
	
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	RequestDispatcher requestDispatcher; 
		//where did the user want to go?
		String uri = req.getRequestURI();
		
		//create database if not created
		if(!dHandler.databaseExists("nacoadatabase")){
			dHandler.setUpDatabase();
		}
		
		//if we are logged in retrieve user id
		if(req.getSession().getAttribute("logged_in") != null){
			//write the code you want
			
		}
		

		//so as you can see it lol its just a series of if statements based on what page 
		//if we are logged in retrieve user id
		if (uri.contains("logout")){
			//write the code you want
			logoutUser(req, res);
			
			requestDispatcher = req.getRequestDispatcher("/Logout.jsp");
	    	requestDispatcher.forward(req, res);
			
		} else if (uri.contains("register")){ //USER REGISTRATION PAGE
			//if user has submitted form
			
			if(req.getParameter("registering") != null){
				//register the user
				int user_id = registerUser(req, res);
				
				//send a email to the user... TODO:
				
				//login the user... but they are not yet registered without email confirm (do later)
				loginUser(req,res,user_id);
			}
						
			requestDispatcher = req.getRequestDispatcher("/Register.jsp");
	    	requestDispatcher.forward(req, res);
			System.out.println(uri);
		} else if (uri.contains("login")){ //USER LOGIN PAGE
			requestDispatcher = req.getRequestDispatcher("/Login.jsp");
	    	requestDispatcher.forward(req, res);
		} else if (uri.contains("submitcred")){ //SUBMIT LOGIN DETAILS
			String username = req.getParameter("username");
			System.out.println(username);
			String password = req.getParameter("password");
			int id = dHandler.getId(username);
			if (authUser(id, password)) {
				loginUser(req, res, id);
				requestDispatcher = req.getRequestDispatcher("/Search.jsp");
		    	requestDispatcher.forward(req, res);
			}else {
				requestDispatcher = req.getRequestDispatcher("/Login.jsp");
		    	requestDispatcher.forward(req, res);
			}
		} else if (uri.contains("cart")){ //CART PAGE
		//for cart it only processes the cart page or remove action
		try {
			//just set up the cart to be read 
			setUpCart(req, res);
		} catch (Exception e){
			System.out.println("setting pu cart fail: " + e );
		}
		
		if (req.getParameter("remove_cart") != null) {
			removeFromCart(req,res);
		}	
		
    	requestDispatcher = req.getRequestDispatcher("/Cart.jsp");
    	requestDispatcher.forward(req, res);
			
		} else if (uri.contains("results")){  //RESULT PAGE (changing page No. OR extra detail for an entry)
			//for result it does searches (basic/advanced) or add to cart for (result view or expanded detail view)
			//know what i mean?
			
			String searchType = (String) req.getParameter("search_type");

			//case of search
			//we will redirect to results
			if (searchType != null){

				if (searchType.matches(".*advanced.*")){
					performAdvancedSearch(req,res);

				} else if (searchType.matches(".*basic.*")){
					performBasicSearch(req,res);
					
				} //uh yeah its kinda complicated...  yeah
				
			} else if(req.getParameter("add_to_cart_view") != null){
				//add to cart from extend view
				
				appendToCartPage(req,res);	

			} else if(req.getParameter("add_to_cart") != null){
				//add to cart from results
				
				appendToCartPage(req,res);
				
				processResults(req,res); //this function lets have a look at it

			} else {
				//just looking at results 
				
				loadResultsXML(); //this stuff will probably be redundant as we are using sql
				
				processResults(req,res);
			}
			
	    	requestDispatcher = req.getRequestDispatcher("/Results.jsp");
	    	requestDispatcher.forward(req, res);
		} else {//MAIN PAGE (this is /search or welcome
			//generate random list

			loadMainXML();

			req.setAttribute("randomBeans",generateRandomBeans(10));
			
	    	requestDispatcher = req.getRequestDispatcher("/Search.jsp");
	    	requestDispatcher.forward(req, res);
		}

	}
	
	public void appendToCartPage(HttpServletRequest req, HttpServletResponse res){
		int pubId = Integer.parseInt(req.getParameter("publication_id"));
		
		try {
			//i think we will have to replace this with SQL functions or we replace the 
			//implementations of these functiosn to use SQL
			setUpCart(req, res);
			
			//we append to the loaded cart
			handler.appendToCart(pubId);	
			
			handler.updateCartXML(CART_FILE_LOCATION); //this will be useless 
			
			cartBeans = handler.getBeanFromCartDoc(0);

    		req.getSession().setAttribute("shoppingCart", cartBeans);
    		handler.setCartToSession("shoppingCartDoc", req.getSession());
			

    		
    		
		} catch (Exception e){
			System.out.println("error adding to cart:" + e );
		}
		
	}
	
	public void removeFromCart(HttpServletRequest req, HttpServletResponse res){
		//we need to load the "current" cart doc, either from session variable, or from xml
		try {
			setUpCart(req, res);
			
			//need to remove from the document from last to first
			///we need to start by looking at the last object 		
			int totalEntries =  Integer.parseInt(req.getParameter("num_items"));

			//to remove items i removed from last to first to avoid changing the indexes
			for(int n = totalEntries-1; n >= 0; n--){
				if(req.getParameter("entry"+n) != null){
					
					//remove from the document
					handler.removeFromCart(n);
					
				}
				
			}

			handler.updateCartXML(CART_FILE_LOCATION);
			
			cartBeans = handler.getBeanFromCartDoc(0);

    		req.getSession().setAttribute("shoppingCart", cartBeans);
    		handler.setCartToSession("shoppingCartDoc", req.getSession());
			
		} catch (Exception e){
			System.out.println("error adding to cart:" + e );
		}
	}

	public int registerUser(HttpServletRequest req, HttpServletResponse res){
		String username = (String) req.getParameter("username");
		String password = (String) req.getParameter("password");
		String email = (String) req.getParameter("email");
		String nickname = (String) req.getParameter("nickname");
		String firstname = (String) req.getParameter("firstname");
		String lastname = (String) req.getParameter("lastname");
		String dob = (String) req.getParameter("dob");
		String address = (String) req.getParameter("address");
		String creditinfo = (String) req.getParameter("creditinfo");
		System.out.println(":" + username );
		System.out.println(":" + password );
		System.out.println(":" + email );
		System.out.println(":" + nickname );
		System.out.println(":" + firstname );
		System.out.println(":" + lastname );
		System.out.println(":" + dob );
		System.out.println(":" + address );
		System.out.println(":" + creditinfo );
		
		System.out.println("Creating User in MySQL Database");
		int user_id = dHandler.createUser(username, password, email, nickname, firstname, lastname, dob, address, creditinfo);
		
		return user_id;

	}
	
	public Boolean authUser(int id, String pw) {
		Boolean result = false;
		result = dHandler.checkPassword(id, pw);
		return result;
	}
	
	public void loginUser(HttpServletRequest req, HttpServletResponse res, int id){
		//set the session to login
		req.getSession().setAttribute("logged_in", true);
		req.getSession().setAttribute("user_id", id);
		req.getSession().setAttribute("username", dHandler.getUserName(id));
		//Add more stuff that you need for a login
		
	}
	
	public void logoutUser(HttpServletRequest req, HttpServletResponse res){
		//set the session to logged out
		req.getSession().setAttribute("logged_in", false);
		
	}
	
	/**
	 * @TODO: change this function to work with SQL
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void setUpCart(HttpServletRequest req, HttpServletResponse res) throws Exception{
		//This function just does dumb things to get xml working
		//we wont need it either
		//oh yeah ok
		//need to load result xml
		loadResultsXML();
		
		//we should only do this for the first time or when we update the xml
		//we should reuse the session attribute
		
		//check session variable exists
		if (req.getSession().getAttribute("shoppingCartDoc") == null) {
			//we check the file now 
			if(handler.fileExists(CART_FILE_LOCATION)){
				//we load
	    		handler.loadCartXML(CART_FILE_LOCATION);
			} else {
				//we must create the cart file
				
				handler.createCartXML(CART_FILE_LOCATION);
			}
		} else {
			//we can retrieve the file from the attribute
			handler.setSessionCartToDoc("shoppingCartDoc", req.getSession());
			
		}
	}
	
	public void loadResultsXML(){
		try {
			if(handler.fileExists(RESULTS_FILE_LOCATION)){
				handler.loadResultsXML(RESULTS_FILE_LOCATION);
			}
		} catch (Exception e){
			System.out.println("Failed to load results xml: " + e);
		}
			
	}
	
	public void loadMainXML(){
		
		try {
			//if(handler.fileExists(MAIN_FILE_LOCATION)){
				handler.loadMainXML(MAIN_FILE_LOCATION);
			//}
		} catch (Exception e){
			System.out.println("Failed to load main xml: " + e);
		}
			
	}
	
	/**
	 * Processes the result page 
	 * @param req
	 * @param res
	 */
	public void processResults(HttpServletRequest req, HttpServletResponse res){
		//just set up the cart to be read 

		String entryToview = req.getParameter("entryMoreView");//im trying to remember what my code does lol
		
		//oh yeah so this viewBean is just a bean to display stuff, is a series of boolean values
		//like do we show more results?
		//do we show less results (like a back button?)
		
		if(entryToview != null){ //EXPANDING VIEW TO READ MORE
			int entryToViewNum = Integer.parseInt(entryToview);
			
			resultBeans = handler.getBeanFromResultDoc(entryToViewNum);
			NACOABean entry = resultBeans.get(0);
			
			//that is what a ResultPageBean does
			ResultPageBean viewBean = new ResultPageBean();
			
			viewBean.setReadMore(true);
			viewBean.setReadEntry(entry);
			
			req.setAttribute("viewBean",viewBean);
			
		} else { //CHANGING PAGE NUMBER
			ResultPageBean viewBean = new ResultPageBean();
			
			//get the current page number (default = 1)
			int totalResults = handler.getNumResults();
			viewBean.setTotalResults(totalResults);
			
			String pageNo = req.getParameter("page");
			int currPageNo = Integer.parseInt(pageNo);
			viewBean.setCurr_page_num(currPageNo);
			
			//so the logic for that is here
			//if the current page number is > 1 we can go backwards etc
			if(pageNo == null){
				//default is first page
				resultBeans = handler.getBeanFromResultDoc(0);
				
				if(currPageNo > 1){
					viewBean.setLess(true);
				}
				
			} else {
				//generate the appropriate
				int startEntry = 10 * (currPageNo-1);
				
				if(startEntry > totalResults){
					//error
					return;
				} 
				
				//retrieve results
				resultBeans = handler.getBeanFromResultDoc(startEntry);
				//set up our bean to be displayed
				viewBean.setResultBeans(resultBeans); //we keep two copies
				
				//set up entry
				if(currPageNo > 1){
					viewBean.setLess(true);
					viewBean.setPrev_page_num(currPageNo-1);
				}
				if(totalResults > startEntry + 10){
					viewBean.setMore(true);
					viewBean.setNext_page_num(currPageNo+1);
				}

				//and we pass these Beans to our JSPs through attributes
				req.setAttribute("viewBean",viewBean);
				//yeah, it reads it and has if statements to display data
				//um yeah it uses JSTL for ifs

			}
		}

		
	}

}


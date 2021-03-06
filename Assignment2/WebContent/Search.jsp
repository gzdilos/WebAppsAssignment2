<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    
<%@page import="java.util.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">

  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
   
    <!-- - This is default -->
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

   	<title>Main Page</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" type="text/css" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">

    <!-- Custom styles for this template -->
    <link href="${pageContext.request.contextPath}/customBootstrap.css" rel="stylesheet">

    
    <script type="text/JavaScript" src="/js/sha512.js"></script> 
    <script type="text/JavaScript" src="/js/forms.js"></script>

    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.1/jquery.min.js"></script> 
    <script type="text/JavaScript" src="/js/popup.js"></script>

    <script src="http://code.jquery.com/jquery.js"></script>
    <script type="text/javascript" src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>

  
  </head>

  <body>
    <div class="container">
    
    <jsp:include page="/topBanner.jsp" /> 
    
      <div class="jumbotron">
        <h1>Welcome to NACOA</h1>
         
         <!--  Need to place search form here -->
         <!-- Default we show basic, if we have a request to have advanced we switch -->
         
          <c:choose>
            <c:when test="${param.search=='Advanced Search'}">
              <h2>Advanced Search</h2>
              <form action="" method="GET">
                <input class="btn btn-ss btn-info" type="submit" value="Basic Search">
              </form>
                <br>
                
              <form action="${pageContext.request.contextPath}/results" method="POST"> 
                <input type="hidden" name="search_type" value="advanced">
                <div class="input-group">
                  <input type="text" class="form-control" aria-label="Text input with dropdown button" placeholder="Author" name="search_author">
                  <input type="text" class="form-control" aria-label="Text input with dropdown button" placeholder="Title" name="search_title">                         
                  <input type="text" class="form-control" aria-label="Text input with dropdown button" placeholder="Venue" name="search_venue">              
                  <input type="text" class="form-control" aria-label="Text input with dropdown button" placeholder="Year" name="search_year">
                </div><!-- /input-group -->
                <div class="input-group">
                  <div class="input-group-btn">
                    
                    <select class="form-control" style="width: 200px" name="search_pubtype">
                        <option value="" disabled selected>Select Publication Type</option>
                        <option>Journal</option>
                        <option>Conference</option>
                        <option>Book/Collections</option>
                        <option>Editorship/Proceedings</option>
                    </select> 
                    
                  </div><!-- /btn-group -->
                  
                  <div class="input-group-btn">
                    <button class="btn btn-default" type="submit"><b>Search</b></button>
                  </div><!-- /btn-group -->
                    
                </div><!-- /input-group -->
              </form>
              <br>

            </c:when>    
            <c:otherwise>
            
              <h2>Basic Search </h2>    
              <form action="" method="GET">
                <input class="btn btn-ss btn-info" type="submit" name="search" value="Advanced Search">
              </form>
              <br> 
             
              <form action="${pageContext.request.contextPath}/results" method="POST">  
                <input type="hidden" name="search_type" value="basic">
                <div class="input-group">
                  <input type="text" class="form-control" aria-label="Text input with dropdown button" placeholder="Enter your search here and select a category" name="search_query">
                  <div class="input-group-btn">
                    <select class="form-control" style="width: 159px" name="search_category">
                        <option>Publication Title</option>
                        <option>Author</option>                        
                        <option>Venue</option>
                        <option>Year</option>
                    </select> 
                  </div><!-- /btn-group -->
                    <span class="input-group-btn">
                      <button class="btn btn-default" type="submit"><b>Search</b></button>
                    </span>
                </div><!-- /input-group -->
              </form>
            <br>
        
            </c:otherwise>
          </c:choose>
         
        <!-- 
        <p><a class="btn btn-lg btn-success" href="${pageContext.request.contextPath}/Results.jsp" role="button">Sign up today</a></p>
        -->
      </div>
  
      <h3>Randomised</h3>
      <div class="content">
      <table class="table table-bordered"> 
        <tr class="active">
          <td scope="col"><b>TITLE</b></td>
          <td scope="col"><b>AUTHOR</b></td>
          <td scope="col"><b>DATE PUBLISHED</b></td>
          <td scope="col"><b>ISBN</b></td>
        </tr>
        
        <c:forEach var="entry" items="${requestScope.randomBeans}" varStatus="varStatus" >
        
          <form name="articles_option" action="" method="POST">
          <!--  set for each of these entries some way of id to add to cart later -->
          <input type="hidden" name="publication_id" value="${entry.xmlID}">
          
          <tr class="active">
            <td class="active">${entry.title}</td>
            <td class="success">${entry.authors}</td>
            <td class="warning">${entry.year}</td>
            <td class="danger"><b>${entry.isbn}</b></td>
          </tr>
          </form>
        </c:forEach>
      </table>
      </div>

      <footer class="footer">
        <p>&copy; Felix Yuen Dao Phu 2015</p>
      </footer>

    </div> <!-- /container -->


    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script>
  </body>
</html>

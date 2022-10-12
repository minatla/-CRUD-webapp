<!doctype html>
 
<!--     
 /*  Name: Mina Lam
     Course: CNT 4714 – Spring 2022 – Project Four 
     Assignment title:  A Three-Tier Distributed Web-Based Application 
     Date:  April 24, 2022 
*/  -->

<%
    String textBox = (String) session.getAttribute("textBox");
    if(textBox == null){
        textBox = " ";
    }

    String result = (String) session.getAttribute("result");
    if(result == null){
        result = " ";
   }
%>
    
        

<html lang="en">
<head>
    <title>Project 4</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script src="buttons.js"></script>  
</head>

<body style="background-color:rgb(195, 222, 198)">

    <div class="container-fluid" style="margin-top: 80px;">
        <row class="row justify-content-center">
            <!-- Title and description -->
            <h1 class="text-center col-sm-12 col-md-12 col-lg-12" style="color: rgb(40, 112, 175)">Welcome to the Spring 2022 Project 4 Enterprise Database System </h1>
            <h3 class="text-center col-sm-12 col-md-12 col-lg-12"> A Servlet/JSP-based Multi-tiered Enterprise Applciation Using A Tomcat Container</h3>
            <div class="text-center col-sm-12 col-md-12 col-lg-12"> You are connected to the Project 4 Enterprise System database as a <span style="color: red;">client-level</span> user.</div>
            <div class="text-center col-sm-12 col-md-12 col-lg-12"> Please enter any valid SQL query or update command in the box below.</div>
            <!-- where user eners SQL -->
            <form action = "/Project4/ClientUserAppServlet" method = "post" style="margin-top: 15px;" class="text-center">
                <div class="form-group row">
                    <div class=" col-sm-12 col-md-12 col-lg-12">
                        <textarea name="textBox" class="form-control" id="textBox" rows="8" cols="50"> <%= textBox %> </textarea>
                    </div>
                </div>
                <button style="margin-bottom: 15px;" type="submit" class="btn btn-dark">Execute Command</button>
                <button onClick="resetForm();" style="margin-bottom: 15px;" type="reset" class="btn btn-dark">Reset Form</button>
                <button onClick="clearResults();" style="margin-bottom: 15px;" type="reset" class="btn btn-dark">Clear Results</button>
            </form>
        </row>
    </div>


    <div class="text-center" style="margin-top: 30px;">
        <p> All execution results will appear below</p>
        <hr>
        <h4>Database Results:</h4>
        <%= result %>
    </div>


    </div>

    <!-- optional javaScript -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>

</html>

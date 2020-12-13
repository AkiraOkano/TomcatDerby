// runs on liberty and derby on pleiades
//https://www.ibm.com/support/knowledgecenter/ja/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_dep_jdbc.html
package okano;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet {

  @Resource(name = "jdbc/exampleDS")
  private DataSource ds1;
  private Connection con = null;
  private static final long serialVersionUID = 1L;

  public HelloWorld() {
    super();
  }
  public void doGet(HttpServletRequest request, HttpServletResponse response)
                                       throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<H1>Hello World Liberty and Derby</H1>\n");
    try {
       con = ds1.getConnection();
       Statement stmt = null;
       stmt = con.createStatement();
       // create a table
       stmt.executeUpdate("create table cities (name varchar(50) not null primary key, population int, county varchar(30))");
       // insert a test record
       stmt.executeUpdate("insert into cities values ('myHomeCity', 106769, 'myHomeCounty')");
       // select a record
       ResultSet result = stmt.executeQuery("select county from cities where name='myHomeCity'");
       result.next();
       // display the county information for the city.
       out.println("The county for myHomeCity is " + result.getString(1));
       // drop the table to clean up and to be able to rerun the test.
       stmt.executeUpdate("drop table cities");
       }
    catch (SQLException e) {
       e.printStackTrace();
       }
    finally {
       if (con != null){
           try{
              con.close();
              }
           catch (SQLException e) {
             e.printStackTrace();
             }
           }
       }
    }
}
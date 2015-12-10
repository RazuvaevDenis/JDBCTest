package netcracker;

import com.mysql.jdbc.PreparedStatement;
import com.sun.org.apache.bcel.internal.generic.Type;
import org.apache.log4j.Level;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class Test {

	public static void SimpleStatement(Connection connection, Statement st, Logger log) throws SQLException {
		ResultSet rs=st.executeQuery("select * from students");
		while (rs.next()) {
			log.log(Level.INFO,rs.getString("student_id")+" "+rs.getString("name")+" "+
					rs.getString("surname")+" "+rs.getString("age")+" "+rs.getString("faculty"));
		}
	}

	public static void SimplePreparedStatement(Connection connection, java.sql.PreparedStatement pst, Logger log) throws SQLException {
		ResultSet pst_rs=pst.executeQuery();
		while(pst_rs.next()){
			log.log(Level.INFO,pst_rs.getString("lecturer_id")+" "+pst_rs.getString("name")+" "+
					pst_rs.getString("surname")+" "+pst_rs.getString("age")+" "+pst_rs.getString("science"));
		}
	}

	public static void SimpleCallableStatement(Connection connection, CallableStatement cst, Logger log) throws SQLException {
		cst.registerOutParameter("count_stud",Types.INTEGER);
		cst.execute();
		log.log(Level.INFO,"Count of student = " + cst.getInt("count_stud"));
	}

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/jdbc";
		Properties property = new Properties();
		DOMConfigurator.configure("src/main/resources/log4j.xml");
		Logger log=Logger.getLogger(Test.class);
		try (FileInputStream fis = new FileInputStream("src/main/resources/jdbc.properties")){
			property.load(fis);
		} catch (IOException e) {
			log.log(Level.ERROR,"Error");
		}
		try (Connection connection = DriverManager.getConnection(url, property);
			 Statement st=connection.createStatement();
			 CallableStatement cst=connection.prepareCall("{call p3(?)}");
			 java.sql.PreparedStatement pst=connection.prepareStatement("select * from lecturers where lecturer_id = ?")) {
			Class.forName("com.mysql.jdbc.Driver");
			pst.setInt(1,1);
			SimpleStatement(connection,st,log);
			SimplePreparedStatement(connection,pst,log);
			SimpleCallableStatement(connection,cst,log);
		} catch (SQLException e) {
			log.log(Level.ERROR,"Error");
		} catch (ClassNotFoundException e) {
			log.log(Level.ERROR,"Error");
		}
	}
}

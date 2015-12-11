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

	private static final Logger log=Logger.getLogger(Test.class.getName());

	public static void SimpleStatement(Statement st, Logger log){
		try(ResultSet rs=st.executeQuery("select * from students")) {
			while (rs.next()) {
				log.log(Level.INFO, rs.getString("student_id") + " " + rs.getString("name") + " " +
						rs.getString("surname") + " " + rs.getString("age") + " " + rs.getString("faculty"));
			}
		}
		catch(SQLException e){
			log.log(Level.ERROR,"Error with error code " +  e.getErrorCode()+"." + e.getMessage(),e);
		}
	}

	public static void SimplePreparedStatement(java.sql.PreparedStatement pst, Logger log){
		try(ResultSet pst_rs=pst.executeQuery()) {
			while (pst_rs.next()) {
				log.log(Level.INFO, pst_rs.getString("lecturer_id") + " " + pst_rs.getString("name") + " " +
						pst_rs.getString("surname") + " " + pst_rs.getString("age") + " " + pst_rs.getString("science"));
			}
		}
		catch(SQLException e){
			log.log(Level.ERROR,"Error with error code " +  e.getErrorCode()+"." + e.getMessage(),e);
		}
	}

	public static void SimpleCallableStatement(CallableStatement cst, Logger log) throws SQLException {
		cst.registerOutParameter("count_stud",Types.INTEGER);
		cst.execute();
		log.log(Level.INFO,"Count of student = " + cst.getInt("count_stud"));
	}

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/jdbc";
		Properties property = new Properties();
		try (FileInputStream fis = new FileInputStream("src/main/resources/jdbc.properties")){
			property.load(fis);
		} catch (IOException e) {
			log.log(Level.ERROR,"Error." + e.getMessage(), e);
		}
		try (Connection connection = DriverManager.getConnection(url, property);
			 Statement st=connection.createStatement();
			 CallableStatement cst=connection.prepareCall("{call p3(?)}");
			 java.sql.PreparedStatement pst=connection.prepareStatement("select * from lecturers where lecturer_id = ?")) {
			Class.forName("com.mysql.jdbc.Driver");
			pst.setInt(1,1);
			SimpleStatement(st,log);
			SimplePreparedStatement(pst,log);
			SimpleCallableStatement(cst,log);
		} catch (SQLException e) {
			log.log(Level.ERROR,"Error with error code " +  e.getErrorCode()+"." + e.getMessage(),e);
		} catch (ClassNotFoundException e) {
			log.log(Level.ERROR,"Error." + e.getMessage(),e);
		}
	}
}

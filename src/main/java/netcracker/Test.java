package netcracker;

import com.mysql.jdbc.PreparedStatement;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class Test {
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/jdbc";
		FileInputStream fis;
		Properties property = new Properties();
		Connection connection = null;
		try {
			fis = new FileInputStream("src/main/resources/jdbc.properties");
			property.load(fis);
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, property);
			Statement st=connection.createStatement();
			java.sql.PreparedStatement pst=connection.prepareStatement("select * from lecturers where lecturer_id = ?");
			pst.setInt(1,1);
			CallableStatement cst=connection.prepareCall("{call p2}");
			ResultSet pst_rs=pst.executeQuery();
			ResultSet rs=st.executeQuery("select * from students");
			ResultSet c_rs=cst.executeQuery();
			while (rs.next()) {
                System.out.println(rs.getString("student_id")+" "+rs.getString("name")+" "+
                		rs.getString("surname")+" "+rs.getString("age")+" "+rs.getString("faculty"));
            }
			System.out.println("---------------------------------");
			while(pst_rs.next()){
				System.out.println(pst_rs.getString("lecturer_id")+" "+pst_rs.getString("name")+" "+
						pst_rs.getString("surname")+" "+pst_rs.getString("age")+" "+pst_rs.getString("science"));
			}
			System.out.println("---------------Callable Statement---------------------");
			while(c_rs.next()){
				System.out.println(c_rs.getString("student_id")+" "+c_rs.getString("name")+" "+
						c_rs.getString("surname")+" "+c_rs.getString("age")+" "+c_rs.getString("faculty"));
			}
			connection.close();
		} catch (IOException e) {
			System.err.println("Error!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

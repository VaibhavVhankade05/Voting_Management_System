package VMS;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;



public class VotingSystem 
{
	static String c, c1;
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception
	{
		VotingSystem vs = new VotingSystem();
		
		
		do
		{
			System.out.println("********** Add Voter Details **********");
			vs.Voter();
			
			System.out.println("*** Do You Want Add Voter Details(Y/N) ***");
			c = sc.next().toUpperCase();
			
			  if(c.equals("Y"))
			  {
				  continue;
			  }
			  else
			  {
				  System.out.println("********** Add Candidate Details **********");
				  vs.Candidate();
				  
				  System.out.println("*** Do You Want Add Candidate Details(Y/N) ***");
				  c1 = sc.next();
				  
				  if(c1.equals("Y"))
				  {
					  continue;
				  }
				  else
				  {
					  System.out.println("******* Start Election *******");
					  vs.StartElection();
					  System.out.println("*** Do You Want To Terminate(Y/N) ***");
					  String c2 = sc.next().toUpperCase();
					  
					  if(c2.equals("N"))
					  {
						  continue;
					  }
					  else
					  {
						  vs.Result();
						  break;
					  }
				  }
			  }
			
		}while(true);
		
	}

	
	//*******Voter Details*******
	
	void Voter() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VotingSystem" , "root" , "Vaibhav@27");
		Statement stmt = con.createStatement();
		
		try
		{
			System.out.println("Enter Voter ID:");
			int id = sc.nextInt();
			System.out.println("Enter Voter Name:");
			String name = sc.next();
			System.out.println("Enter Voter Addrees:");
			String add = sc.next();
			System.out.println("Enter Voter Password");
			String pw = sc.next();
			
			stmt.execute("insert into VoterDetails( ID, Name, Address , Status, Password ) VALUES("+id+" , ' "+name+" ' , ' "+add+" ' , "+0+" , ' "+pw+" ')");
			System.out.println(" ");
			System.out.println("Successfully added voter's deatils...");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		con.close();
			
	}
	
	
	//*******Candidate Details*******
	
	void Candidate() throws ClassNotFoundException, SQLException 
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VotingSystem" , "root" , "Vaibhav@27");
		Statement stmt = con.createStatement();
		
		try
		{
			System.out.println("Enter Candidate ID:");
			int id = sc.nextInt();
			System.out.println("Enter Candidate Name:");
			String name = sc.next();
			System.out.println("Enter Candidate Addrees:");
			String add = sc.next();
			
			stmt.execute(" insert into CandidateDetails(ID, Name, Address , Votes ) VALUES("+id+" , ' "+name+" ' , ' "+add+" ' , "+0+")");
			System.out.println(" ");
			System.out.println("Successfully added candidate's deatils...");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		con.close();
	}
	
	
	//*******Start Election*******
	
	void StartElection() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VotingSystem" , "root" , "Vaibhav@27");
		Statement stmt = con.createStatement();
		
		try
		{
			System.out.println("*****WELCOME TO ELECTION*****");
			System.out.println(" ");
			System.out.println("Enter your VoterId:");
			int vid = sc.nextInt();
			System.out.println("Enter your Password:");
			String pass = sc.next();
			
			ResultSet rs = stmt.executeQuery("select * from VoterDetails");
			
			while(rs.next())
			{
//				System.out.println("Voter ID: "+rs.getInt("ID") +"   " + "Voter Password: "+rs.getString("Password") );
				
				if(rs.next())
				{
					System.out.println("Valid ID AND Password");
					System.out.println(" ");
					ResultSet candidate = stmt.executeQuery("SELECT ID, Name FROM CandidateDetails");
					System.out.println("**********Available Candidate**********");
					System.out.println("   ************ VOTE FOR ***********   ");
					
					while(candidate.next())
					{
						System.out.println("Candidate ID: "+candidate.getInt("ID") +"   " + "Candidate Name: "+candidate.getString("Name") );	
						System.out.println(" ");
						
					}
						try
						{
							System.out.println("Enter Candidate ID:");
							int cid = sc.nextInt();
							PreparedStatement ps,vs = null;
							ps = con.prepareStatement("UPDATE CandidateDetails SET Votes=Votes+1 WHERE ID=?");
							ps.setInt(1, cid);
							ps.executeUpdate();
							System.out.println("Your vote is updated successfully... ");
							vs = con.prepareStatement("Update VoterDetails SET Status=1 WHERE ID=?");
							vs.setInt(1, vid);
							vs.executeUpdate();
						}
						catch(Exception e)
						{
							System.out.println(e);
						}
				}
				else
				{
					System.out.println("Invalid ID AND Password");
				}
			}
			rs.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	void Result() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VotingSystem" , "root" , "Vaibhav@27");
		Statement stmt = con.createStatement();
				
		ResultSet r = stmt.executeQuery("SELECT ID, Name, Votes FROM CandidateDetails ORDER BY Votes DESC");
		while(r.next())
		{
		System.out.println("Candidate ID: "+r.getInt("ID") +"   " + "Candidate Name: "+r.getString("Name") + " " + "Candidate Votes: "+r.getInt("Votes") );	
		System.out.println(" ");	
		}
		r = stmt.executeQuery("SELECT ID, Name, Votes FROM CandidateDetails ORDER BY Votes DESC");
		r.next();
		System.out.println("  ");
		System.out.println("******************** WINNER ********************");
		System.out.println("Candidate ID: "+r.getInt("ID") +"   " + "Candidate Name: "+r.getString("Name") + " " + "Candidate Votes: "+r.getInt("Votes"));
		
		
	}
	
	
	
}
 
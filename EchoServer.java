// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
	
 //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the server.
   */
  ChatIF serverUI; 
  
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI;
    
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
	  String message = msg.toString();
	  String[] messageSplit = message.split(" ");
	  if(messageSplit[0].equals("#login")) {
		  
	  }
	  
	  System.out.println("Message received: " + msg + " from " + client);
	  this.sendToAllClients(msg);
  }
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromServerUI(String message)
  {
    try
    {
    	// if client entered a command
    	if(message.charAt(0) == '#') {
    		String [] command = message.split(" ");
    		switch(command[0]) {
    			case "#quit": //not sure if this is right 
    				close();
    				break;
    			case "#stop": //not sure if this is right 
    				stopListening();
    				break;
    			case "#close": //not sure if this is right
    				close();
    				break;
    			case "#setport":
    				setPort(Integer.parseInt(command[1])); //only allowed if server is closed; displays error message otherwise
    				break;
    			case "#login":
    				if (isListening()) serverUI.display("Already logged in");
    				else listen();
    				break;
    			case "#getport":
    				serverUI.display(Integer.toString(getPort()));
    				break;
    		}
    	} else {
    		String serverMessage = "SERVER MSG>" + message;
    		serverUI.display(serverMessage);
    		this.sendToAllClients(serverMessage);
    	}
    }
    catch(IOException e)
    {
      serverUI.display("Could not send message to clients.  Terminating server.");
      //close();
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  /**
   * Hook method called each time a new client connection is
   * accepted.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("Client has connected");
  }
  
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client has disconnected");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class

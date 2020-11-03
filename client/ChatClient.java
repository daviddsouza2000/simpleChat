// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  /**
   * The login id of this chat client instance
   */
  int loginID;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(int loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	// if client entered a command
    	if(message.charAt(0) == '#') {
    		String [] command = message.split(" ");
    		switch(command[0]) {
    			case "#quit":
    				quit();
    				break;
    			case "#logoff":
    				closeConnection();
    				break;
    			case "#sethost":
    				if (isConnected()) {
    					clientUI.display("Must first log off");
    				}
    				else {
    					try {
    						setHost(command[1]);
    						clientUI.display("Host set to" + command[1]);
    					} catch(ArrayIndexOutOfBoundsException e) {
    						clientUI.display("Host not specified");
    					}
    				}
    				break;
    			case "#setport":
    				if (isConnected()) {
    					clientUI.display("Must first log off");
    				}
    				else {
    					try {
    						setPort(Integer.parseInt(command[1]));
    						clientUI.display("Port set to" + Integer.parseInt(command[1]));
    					} catch(ArrayIndexOutOfBoundsException e) {
    						clientUI.display("Port not specified");
    					}
    				}
    				break;
    			case "#login":
    				if (isConnected()) clientUI.display("Already logged in");
    				else openConnection();
    				sendToServer(message);
    				break;
    			case "#gethost":
    				clientUI.display(getHost());
    				break;
    			case "#getport":
    				clientUI.display(Integer.toString(getPort()));
    				break;
    		}
    	} else {
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {
    	System.exit(0);
    }
    System.exit(0);
  }
  
  /**
   * Hook method called after a connection has been established.
   */
  @Override
  protected void connectionEstablished() {
	  try {
		sendToServer("#login " + loginID);
	} catch (IOException e) {
		clientUI.display("Could not send message to server.  Terminating client.");
		quit();
	}
  }
  
  /**
   * Hook method called after the connection has been closed.
   */
  @Override
  protected void connectionClosed() {
	  clientUI.display("The connection has been closed");
  }
	
  /**
   * Hook method called each time an exception is thrown by the client's
   * thread that is waiting for messages from the server. 
   * @param exception
   *            the exception raised.
   */
  @Override
  protected void connectionException(Exception exception) {
	  clientUI.display("WARNING - The server has stopped listening for connection \n SERVER SHUTTING DOWN! DISCONNECTING! \n "
	  		+ "Abnormal termination of connection");
  }
}


//End of ChatClient class

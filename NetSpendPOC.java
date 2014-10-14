import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.Socket;


public class NetSpendPOC {
	public static void main(String [] args)
	{
		String host = "66.205.191.185";
		int port = 3914;
		String userId= "checkngo-socketpc";    
		String password="passw0rd1"; 
		String  transRefNum= "1279";
		try {
			System.out.println("SOCKET");
			Socket clientSocket = new Socket(host, port);
			System.out.println("AFTER SOCKET");
			 /** STEP1 Authentication **/
            String signOnRequest = getNetspendSignOnRequest(transRefNum,userId,password);
            System.out.println(signOnRequest);
            String signOnResponse= getNetspendResponse(signOnRequest,clientSocket);
            System.out.println(signOnResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
public static String getNetspendSignOnRequest(String transRefNum,String userId,String password) throws Exception {
        
        String methodName = "getNetspendSignOnRequest";
        String Partner="checkngo";
        String signOnReq = "<" + "signOn" + "!" +transRefNum + "!" + Partner + "!" + userId +"!" + password + ">";
        
        
        return signOnReq;
        
    }
    
    /** Added to get SignOn **/
    
    public static String getNetspendResponse(String request,Socket clientSocket) throws Exception {
        	
        	String methodName = "getNetspendResponse";
            String modifiedSentence=null;
            try{
            		byte[] payload = request.getBytes("UTF-8");// Compute the length of the message, including terminating characters
    				int len = payload.length;
    				// Create the header bytes
    				byte[] header = new byte[2];
    				header[0] = (byte)((len >> 8) & 0xff);
    				header[1] = (byte)(len & 0xff);
    				System.out.println("XXXXXX");
    				System.out.print(header[0]);
    				System.out.print(header[1]);
    				System.out.print(new String(payload));
    				System.out.println("XXXXXX");
    				//OutputStream output =clientSocket.getOutputStream();
    				BufferedOutputStream  output =new BufferedOutputStream(clientSocket.getOutputStream());
    				
    	            output.write(header);// Write the payload bytes
    				output.write(payload);// Write terminating characters
    						
    				output.flush();
    				clientSocket.setSoTimeout(45000);
    				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    				modifiedSentence = inFromServer.readLine();
    				System.out.println(modifiedSentence);
            } catch (InterruptedIOException io) {
io.printStackTrace();
            	throw(io);

            }
            catch (Exception e) {

            	e.printStackTrace();
            	throw(e);

            }
    				
    	 return modifiedSentence;
        }
    
}

package ftp;
import java.io.*;
import java.net.*;
import java.lang.Math;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {


    public static void main(String[] args) {

		//parsing commands from the command line and calculating file size,no. of segments and size of each segment
        String serverIp = args[1]; 
        int portNo = Integer.parseInt(args[2]);
        String fileName = args[4];
        String filePath = "Transmit\\";
        File file = new File(filePath+fileName);
        long fileSize = file.length();
        int segmentSize = (int)Math.ceil((double)fileSize/10);
       
        //print information about the file to be transferred
        System.out.println("We are transferring a file with name \"" + fileName
                + "\" of size " + fileSize + " B. We divide it into "
                + "10 segments of size " + segmentSize + " Bytes each.");

        String controlMessage = fileName + ";" + fileSize + ";10;" + segmentSize;
        
        
        Socket clientSocket = null;
        PrintStream ps = null;
        DataInputStream dis = null;
        //byte[] b;    //

        try {
            //creating socket connection for sending the control message to the server
        	System.out.println(" Creating control connection with IP address " + serverIp + " on " +portNo);
            clientSocket = new Socket(serverIp, portNo);
            ps = new PrintStream(clientSocket.getOutputStream());
            dis = new DataInputStream(clientSocket.getInputStream());
            System.out.println("Connection successful");
            System.out.println("Sending file information");
            
            //sending the control message to server
            ps.println(controlMessage);
            
            System.out.println("file information sent!!! Initiating data transfer!!! ");
            
            //converting the file into a byte stream for transfer
            
            FileInputStream fileInputStream = new FileInputStream(file);
              //read the byte stream into a file input stream
            
            System.out.println("1");
            //create byte arrays to store each file segment
            byte[] b1 = new byte[segmentSize];
            byte[] b2 = new byte[segmentSize];
            byte[] b3 = new byte[segmentSize];
            byte[] b4 = new byte[segmentSize];
            byte[] b5 = new byte[segmentSize];
            byte[] b6 = new byte[segmentSize];
            byte[] b7 = new byte[segmentSize];
            byte[] b8 = new byte[segmentSize];
            byte[] b9 = new byte[segmentSize];
            byte[] b10 = new byte[segmentSize];
            System.out.println("2");
            //int i;
            
            fileInputStream.read(b1);
            fileInputStream.read(b2);
            fileInputStream.read(b3);
            fileInputStream.read(b4);
            fileInputStream.read(b5);
            fileInputStream.read(b6);
            fileInputStream.read(b7);
            fileInputStream.read(b8);
            fileInputStream.read(b9);
            fileInputStream.read(b10);
           /* while (true) {
                i = fileInputStream.read(b1);
                if (i == -1)
                  break;
            }
            System.out.println(b1);
            System.out.println("3");
            
            while (true) {
                i = fileInputStream.read(b2);
                if (i == -1)
                  break;
            }
            System.out.println(b2);
            
            while (true) {
                i = fileInputStream.read(b3);
                if (i == -1)
                  break;
            }
            
            while (true) {
                i = fileInputStream.read(b4);
                if (i == -1)
                  break;
            } 
            
            while (true) {
                i = fileInputStream.read(b5);
                if (i == -1)
                  break;
            }
            
            while (true) {
                i = fileInputStream.read(b6);
                if (i == -1)
                  break;
            }
            
            while (true) {
                i = fileInputStream.read(b7);
                if (i == -1)
                  break;
            }
            
            while (true) {
                i = fileInputStream.read(b8);
                if (i == -1)
                  break;
            }
            
            while (true) {
                i = fileInputStream.read(b9);
                if (i == -1)
                  break;
            } 
            
            while (true) {
                i = fileInputStream.read(b10);
                if (i == -1)
                  break;
            }*/
                
            System.out.println("4");  
            
            //create threads to transmit each of the segments in parallel over different ports
            Thread thread1 = new FileTransmit(serverIp, portNo + 1, b1,1);
            Thread thread2 = new FileTransmit(serverIp, portNo + 2, b2,2);
            Thread thread3 = new FileTransmit(serverIp, portNo + 3, b3,3);
            Thread thread4 = new FileTransmit(serverIp, portNo + 4, b4,4);
            Thread thread5 = new FileTransmit(serverIp, portNo + 5, b5,5);
            Thread thread6 = new FileTransmit(serverIp, portNo + 6, b6,6);
            Thread thread7 = new FileTransmit(serverIp, portNo + 7, b7,7);
            Thread thread8 = new FileTransmit(serverIp, portNo + 8, b8,8);
            Thread thread9 = new FileTransmit(serverIp, portNo + 9, b9,9);
            Thread thread10 = new FileTransmit(serverIp, portNo + 10, b10,10);
            System.out.println("5");  
            //start all the threads simultaneously
            thread1.start();
            thread2.start();
            thread3.start();
            thread4.start();
            thread5.start();
            thread6.start();
            thread7.start();
            thread8.start();
            thread9.start();
            thread10.start();
            System.out.println("6");   
            while (true) {
				if (!thread1.isAlive() && !thread2.isAlive()
						&& !thread3.isAlive() && !thread4.isAlive()
						&& !thread5.isAlive() && !thread6.isAlive()
						&& !thread7.isAlive() && !thread8.isAlive()
						&& !thread9.isAlive() && !thread10.isAlive()) {
					break;
				}
			}
            //Once all the threads have completed execution and if response is acquired from server about reception of all segments then print transfer completed
            System.out.println("Transfer Completed");
            
            //close the socket connection
            ps.close();
            dis.close();
            clientSocket.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
    }
}

//FileTransmit thread declaration for data transfer
class FileTransmit extends Thread {

    int portNo;
    byte[] b;
    int segmentNo;
    String serverIp;
    Socket clientSocket = null;
    PrintStream ps = null;
    DataInputStream dis = null;
  //Each instance of the FileTransmit thread creates data variables for the port no., segment no., serverIp and byte stream 
    FileTransmit(String serverIp, int portNo, byte[] b, int segmentNo) {
        this.portNo = portNo;
        this.b = b;
        this.serverIp = serverIp;
        this.segmentNo = segmentNo;
    }

  //Code for the FileTRansmit thread where data transfer takes place
    public void run() {
        try {
        	//Each instance of FileTransmit creates a unique socket connections for data transfer
            clientSocket = new Socket(serverIp, portNo);
            ps = new PrintStream(clientSocket.getOutputStream());
            dis = new DataInputStream(clientSocket.getInputStream());
            System.out.println("Segment " + segmentNo + " is being transferred on port " + portNo);

            if (clientSocket != null && ps != null && dis != null) {
                ps.write(b);
                //Transfer for each segment completed once written to temp files
                System.out.println("Segment " + segmentNo + " tranferred");               
            }
            //close the respective interfaces once the respective segment has been transferred
            ps.close();
            dis.close();
            clientSocket.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(FileTransmit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileTransmit.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
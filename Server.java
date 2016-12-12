package ftp;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

	//Creating socket for control connection. Function takes in port no. as parameter from the command line
	public static ServerSocket createserverSocket(int portNo) {
		ServerSocket serverSocket = null;

		try {
			//create a new socket to receive the control message
			serverSocket = new ServerSocket(portNo);
		} catch (Exception e) {
			System.out.println(e);
		}

		return serverSocket;
	}

	@SuppressWarnings("deprecation")
	public static void main(String args[]) {

		ServerSocket controlSocket = null;
		String line;
		DataInputStream is;
		PrintStream os;
		Socket socket = null;
		int port = Integer.parseInt(args[0].trim());
		String[] message;

		controlSocket = Server.createserverSocket(port);

		try {

			while (true) {
				//accept the request from the client	
				socket = controlSocket.accept();

				is = new DataInputStream(socket.getInputStream());

				os = new PrintStream(socket.getOutputStream());
				
				//Read the control message
				line = is.readLine();

				if (line != null) {
					os.println("Ok");
					//semicolons are used as the message delimiter
					message = line.split(";");

					String fileName = message[0];
					String filePath = "Receive\\";
					File file = new File(filePath + fileName);
					long fileSize = Integer.parseInt(message[1]);
					@SuppressWarnings("unused")
					int segmentNo = Integer.parseInt(message[2]);
					int segmentSize = Integer.parseInt(message[3]);
					
					//Instance the thread FileReceive with different ports to receive the different segments
					Thread thread1 = new FileReceive(port + 1,
							"Temp\\file1.bin", segmentSize, 1);
					Thread thread2 = new FileReceive(port + 2,
							"Temp\\file2.bin", segmentSize, 2);
					Thread thread3 = new FileReceive(port + 3,
							"Temp\\file3.bin", segmentSize, 3);
					Thread thread4 = new FileReceive(port + 4,
							"Temp\\file4.bin", segmentSize, 4);
					Thread thread5 = new FileReceive(port + 5,
							"Temp\\file5.bin", segmentSize, 5);
					Thread thread6 = new FileReceive(port + 6,
							"Temp\\file6.bin", segmentSize, 6);
					Thread thread7 = new FileReceive(port + 7,
							"Temp\\file7.bin", segmentSize, 7);
					Thread thread8 = new FileReceive(port + 8,
							"Temp\\file8.bin", segmentSize, 8);
					Thread thread9 = new FileReceive(port + 9,
							"Temp\\file9.bin", segmentSize, 9);
					Thread thread10 = new FileReceive(port + 10,
							"Temp\\file10.bin", segmentSize, 10);
					
					
					//Start all the four interfaces
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
					
					while (true) {
						if (!thread1.isAlive() && !thread2.isAlive()
								&& !thread3.isAlive() && !thread4.isAlive()
								&& !thread5.isAlive() && !thread6.isAlive()
								&& !thread7.isAlive() && !thread8.isAlive()
								&& !thread9.isAlive() && !thread10.isAlive()) {
							break;
						}
					}

					//Create temporary files and write each of the segments into the four temporary files
					File fileTemp = new File("Temp\\file1.bin");
					FileInputStream fileInputStream = new FileInputStream(fileTemp);
					byte[] buf = new byte[segmentSize];
					fileInputStream.read(buf); 
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(buf);  //write the received file segment into the temporary file created
					
					//Repeating the same steps for writing the subsequent three file segments into three temporary files 
					fileTemp = new File("Temp\\file2.bin");
					fileInputStream = new FileInputStream(fileTemp);
					fileInputStream.read(buf);
					fos.write(buf);
					
					fileTemp = new File("Temp\\file3.bin");
					fileInputStream = new FileInputStream(fileTemp);
					fileInputStream.read(buf);
					fos.write(buf);
					
					fileTemp = new File("Temp\\file4.bin");
					fileInputStream = new FileInputStream(fileTemp);
					fileInputStream.read(buf);
					fos.write(buf);
					
					fileTemp = new File("Temp\\file5.bin");
					fileInputStream = new FileInputStream(fileTemp);
					fileInputStream.read(buf);
					fos.write(buf);
					
					fileTemp = new File("Temp\\file6.bin");
					fileInputStream = new FileInputStream(fileTemp);
					fileInputStream.read(buf);
					fos.write(buf);
					
					fileTemp = new File("Temp\\file7.bin");
					fileInputStream = new FileInputStream(fileTemp);
					fileInputStream.read(buf);
					fos.write(buf);
					
					fileTemp = new File("Temp\\file8.bin");
					fileInputStream = new FileInputStream(fileTemp);
					fileInputStream.read(buf);
					fos.write(buf);
					
					fileTemp = new File("Temp\\file9.bin");
					fileInputStream = new FileInputStream(fileTemp);
					fileInputStream.read(buf);
					fos.write(buf);
					
					fileTemp = new File("Temp\\file10.bin");
					fileInputStream = new FileInputStream(fileTemp);
					buf = new byte[(int)(fileSize - (9 * (long)segmentSize))];
					fileInputStream.read(buf);
					fos.write(buf);
					
					//close the files
					fos.close();
					
					line = null;
				}
				//close the 4 socket connections 
				os.close();
				is.close();
				socket.close();
				
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

//Declaring the FileReceive thread to receive all the four segments in parallel
class FileReceive extends Thread {

	int portNo;
	int segmentSize;
	int segmentNo;
	byte[] b;
	String fileName;
	ServerSocket serverSocket = null;
	Socket socket;
	DataInputStream is;
	PrintStream os;
	 //Each instance of the FileReceive thread creates data variables for the port no., segment no., serverIp and byte stream
	FileReceive(int portNo, String fileName, int segmentSize, int segmentNo) {
		this.portNo = portNo;
		this.fileName = fileName;
		this.segmentSize = segmentSize;
		this.segmentNo = segmentNo;
	}

	//FileReceive thread begins execution
	public void run() {
		try {
           //Create socket connection to receive the file segment
			serverSocket = Server.createserverSocket(portNo);
			socket = serverSocket.accept();

			is = new DataInputStream(socket.getInputStream());
			os = new PrintStream(socket.getOutputStream());
            
			//create the byte array to receive the respective file segment
			b = new byte[segmentSize];
			if (socket != null && is != null && os != null) {
				is.readFully(b);//read the byte stream from the input stream
				os.println(segmentNo);  //once segment received send the segment no. received back to the client	
			}
			
			//create objects for the new consolidated file to which all files are to be written to 
			File file = new File(fileName);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(b);// write the buffer to the new file
			is.close();
			os.close();
			socket.close();
			serverSocket.close();
			//close the current segment socket connection
		} catch (UnknownHostException ex) {
			Logger.getLogger(FileReceive.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException ex) {
			Logger.getLogger(FileReceive.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}

}

	import java.io.*;
	import java.net.*;
/**
 * @author Kristof Van Cappellen
 * @author Emiel Vandeloo
 *
 */

	import java.io.*;
	import java.net.*;

	public class Client {

		public static void main(String[] args) throws Exception {
			String command = args[0];
			URI uri = new URI(args[1]);
			String host = uri.getHost();
			String path = uri.getPath();
			int port = new Integer(args[2]);
			Socket clientSocket = new Socket(host, port);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			switch (command) {
			case "GET":
				// Generate command to server
				outToServer.writeBytes("GET " + path + " HTTP/1.1" + "\r\n");
				outToServer.writeBytes("Host: " + host + "\r\n");
				outToServer.writeBytes("\r\n");
				outToServer.flush();
				// Create file for results
				File GETFile = new File("C:/Users/Emiel/Documents/Jaar 3/Computer Networks/"
						+ "Assignment 2/localgetfile.txt");
				FileOutputStream outToGETFile = new FileOutputStream(GETFile);
				// Read response from server
				while (inFromServer.readLine() != null) {
					byte[] data = inFromServer.readLine().getBytes();
					String dataString = new String(data);
					System.out.println(dataString);
					outToGETFile.write(data);
					outToGETFile.write("\r\n".getBytes());
				}
				// Handle possible images
				Document doc = Jsoup.parse(GETFile, "UTF-8", uri.toString());
				Elements images = doc.select("img");
				for (Element imag : images) {
					URI imageURI = new URI(imag.baseUri());
					String imageHost = imageURI.getHost();
					String imagePath = imageURI.getPath();
					outToServer.writeBytes("GET " + imagePath + " HTTP/1.1" + "\r\n");
					outToServer.writeBytes("Host: " + imageHost + "\r\n");
					outToServer.writeBytes("\r\n");
					outToServer.flush();
					File imageFile = new File("C:/Users/Emiel/Documents/Jaar 3/Computer Networks/"
							+ "Assignment 2/localimagefile.txt");
					FileOutputStream outToImageFile = new FileOutputStream(imageFile);
					while (inFromServer.readLine() != null) {
						byte[] data = inFromServer.readLine().getBytes();
						String dataString = new String(data);
						System.out.println(dataString);
						outToImageFile.write(data);
						outToImageFile.write("\r\n".getBytes());
					}
					outToImageFile.close();
				}
				// Close streams
				outToGETFile.close();
				outToServer.close();
				inFromServer.close();
				break;
			case "HEAD":
				outToServer.writeBytes("HEAD " + path + " HTTP/1.1" + "\r\n");
				outToServer.writeBytes("Host: " + host + "\r\n");
				outToServer.writeBytes("\r\n");
				outToServer.flush();
				File HEADFile = new File("C:/Users/Emiel/Documents/Jaar 3/Computer Networks/"
						+ "Assignment 2/localheadfile.txt");
				FileOutputStream outToHEADFile = new FileOutputStream(HEADFile);
				while (inFromServer.readLine() != null) {
					byte[] data = inFromServer.readLine().getBytes();
					String dataString = new String(data);
					System.out.println(dataString);
					outToHEADFile.write(data);
					outToHEADFile.write("\r\n".getBytes());
				}
				outToHEADFile.close();
				outToServer.close();
				inFromServer.close();
				break;
			case "PUT":
				
			case "POST":
				
			}
			clientSocket.close();
		}

	}

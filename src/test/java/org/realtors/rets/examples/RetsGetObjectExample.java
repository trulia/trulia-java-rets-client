package org.realtors.rets.examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.realtors.rets.client.CommonsHttpClient;
import org.realtors.rets.client.GetObjectRequest;
import org.realtors.rets.client.RetsException;
import org.realtors.rets.client.RetsHttpClient;
import org.realtors.rets.client.RetsSession;
import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.client.SingleObjectResponse;


/**
 * Simple Example performing a GetObject and iterating the results
 *
 */
public class RetsGetObjectExample {


	public static void main(String[] args) throws MalformedURLException {

		//Create a RetsHttpClient (other constructors provide configuration i.e. timeout, gzip capability)
		RetsHttpClient httpClient = new CommonsHttpClient();
		RetsVersion retsVersion = RetsVersion.RETS_1_7_2;
		String loginUrl = "http://theurloftheretsserver.com";

		//Create a RetesSession with RetsHttpClient
		RetsSession	session = new RetsSession(loginUrl, httpClient, retsVersion);

		String username = "username";
		String password = "password";
		try {
			//Login
			session.login(username, password);
		} catch (RetsException e) {
			e.printStackTrace();
		}

		String sResource = "Property";
		String objType   = "Photo";
		String seqNum 	= "*"; // * denotes get all pictures associated with id (from Rets Spec)
		List<String> idsList = Arrays.asList("331988","152305","243374");
		try {
			//Create a GetObjectRequeset
			GetObjectRequest req = new GetObjectRequest(sResource, objType);

			//Add the list of ids to request on (ids can be determined from records)
			Iterator<String> idsIter = idsList.iterator();
			while(idsIter.hasNext()) {
				req.addObject(idsIter.next(), seqNum);
			}

			//Execute the retrieval of objects 
			Iterator<SingleObjectResponse> singleObjectResponseIter = session.getObject(req).iterator();

			//Iterate over each Object 
			while (singleObjectResponseIter.hasNext()) {
				SingleObjectResponse sor = (SingleObjectResponse)singleObjectResponseIter.next();

				//Retrieve in info and print
				String type =			sor.getType();
				String contentID = 		sor.getContentID();
				String objectID = 		sor.getObjectID();
				String description = 	sor.getDescription();
				String location = 		sor.getLocation();
				InputStream is = 		sor.getInputStream();

				System.out.print("type:" + type);
				System.out.print(" ,contentID:" + contentID);
				System.out.print(" ,objectID:" + objectID);
				System.out.println(" ,description:" + description);
				System.out.println("location:" + location); 

				//Download object
				try {
					String dest			= "/path/of/dowload/loaction";
					int size = is.available();
					String filename = dest + contentID +"-" + objectID + "." + type;
					OutputStream out = new FileOutputStream(new File(filename)); 
					int read = 0;
					byte[] bytes = new byte[1024];

					while ((read = is.read(bytes)) != -1) {

						out.write(bytes, 0, read);
					}

					is.close();
					out.flush();
					out.close();

					System.out.println("New file with size " + size + " created: " + filename);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}

			}

		} catch (RetsException e) {
			e.printStackTrace();
		}
		finally {
			if(session != null) {
				try {
					session.logout();
				}
				catch (RetsException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

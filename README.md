trulia-java-rets-client
=================

The Trulia Java RETS Client is a library used to access data on RETS compliant servers.
This is a fork of the [jrets](https://github.com/jpfielding/jrets) master branch, which it self is a fork of the [CART RETS client](http://cart.sourceforge.net/).


Simple example of a search:


	public static void main(String[] args) throws MalformedURLException {

		//Create a RetsHttpClient (other constructors provide configuration i.e. timeout, gzip capability)
		RetsHttpClient httpClient = new CommonsHttpClient();
		RetsVersion retsVersion = RetsVersion.RETS_1_7_2;
		String loginUrl = "http://theurloftheretsserver.com";

		//Create a RetesSession with RetsHttpClient
		RetsSession session = new RetsSession(loginUrl, httpClient, retsVersion);    

		String username = "username";
		String password = "password";

		//Set method as GET or POST
		session.setMethod("POST");
		try {
			//Login
			session.login(username, password);
		} catch (RetsException e) {
			e.printStackTrace();
		}

		String sQuery = "(Member_num=.ANY.)";
		String sResource = "Property";
		String sClass = "Residential";

		//Create a SearchRequest
		SearchRequest request = new SearchRequest(sResource, sClass, sQuery);

		//Select only available fields
		String select ="field1,field2,field3,field4,field5";
		request.setSelect(select);

		//Set request to retrive count if desired
		request.setCountFirst();

		SearchResultImpl response;
		try {
			//Execute the search
			response= (SearchResultImpl) session.search(request);

			//Print out count and columns
			int count = response.getCount();
			System.out.println("COUNT: " + count);
			System.out.println("COLUMNS: " + StringUtils.join(response.getColumns(), "\t"));

			//Iterate over, print records
			for (int row = 0; row < response.getRowCount(); row++){
				System.out.println("ROW"+ row +": " + StringUtils.join(response.getRow(row), "\t"));
			}
		} catch (RetsException e) {
			e.printStackTrace();
		} 
		finally {
			if(session != null) { 
				try {
					session.logout(); 
				} 
				catch(RetsException e) {
					e.printStackTrace();
				}
			}
		}

Simple example of a GetObject Example:

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
					String filename = dest + contentID +"-" + objectID + ".jpeg";
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

## License
[Trulia Java RETS Client is licensed under the MIT License](https://github.com/trulia/trulia-java-rets-client/blob/master/LICENSE)

*************************************
*    Mirth Archive - 22/10/2010     *
*************************************

Successfully running this code requires:

Connection with the Database

1. Remote server with the lastest version of PostgreSQL installed (currently, 9.0.1). See http://www.postgresql.org/ for more information.
2. Latest version of the PostgreSQL JDBC driver (currently 9.0), in the form of an external jar file. See http://jdbc.postgresql.org/ for more information.
3. VPN connection to MirthCorp to access their servers

Connection with Solr*

1. addition of a number of jar files (including files for the CommonsHttpClient), 
	see http://wiki.apache.org/solr/Solrj for the full list.

*can only be done once the connection with database is established

=============
= Changelog =
=============

2010-10-07 Duncan Tsai <duncant@uci.edu>

	* DBConnection.java: class created
	* UserRetriever.java: class created
	* FileRetriever.java: class created

2010-10-14 Duncan Tsai <duncant@uci.edu>

	*UserRetreiver.java (retrieveUserInformation()) : combined with functionality from processUserRetrieveQuery()
	*UserRetreiver.java (processUserRetrieveQuery()) : function no long exists
	*FileRetreiver.java (retrieveFile()): combined with functionality from processQuery()
	*FileRetreiver.java (processQuery()) : function no long exists
	*UserRetreiver.java (addUser()) : function created

2010-10-15 Duncan Tsai <duncan@uci.edu>

	*UserRetreiver.java (editUser()) : function created
	*UserRetreiver.java (userExist()): function created

2010-10-17 Duncan Tsai <duncant@uci.edu>

	*UserRetreiver.java (addUser()): added conditional statements relating to minimum username and password length
		when creating a new user
	*DefaultUserSettingsGenerator.java: class created
	*UserRetreiver.java (addUser()) : call to DefaultUserSettingGenerator is added

2010-10-19 Duncan Tsai <duncant@uci.edu>
	
	*UserManager.java : renamed UserRetreiver.java
	*UserRetreiver.java: class no longer exists 
	*UserManager.java (editUser()): function created
	*UserManager.java (updateGlobalSettings()): function created

2010-10-21 Duncan Tsai <duncant@uci.edu>

	*PersonalSettingEditor.java: class created

2010-10-22 Duncan Tsai <duncant@uci.edu>

	*DateUtil.java: class created
	*DefaultUserSettingsGenerator.java: class created
	*BrowsingHistoryTableGenerator.java: class created
	*UserRetreiver.java (addUser()) : call to DefaultUserSettingsGenerator.generate() is added
	*UserRetreiver.java (addUser()) : call to BrowsingHistoryTableGenerator.generate() is added
	*BrowsingHistoryLogger.java: class created
	*FileRetreiver.java (retrieveFile()): calls to BrowsingHistoryLogger.log() are added
	
	*All java files reorganized into appropriate package structure.

2010-10-25 Duncan Tsai <duncant@uci.edu>

	*BrowsingHistoryTableGenerator.java (generate()) : stopped using a PreparedStatement to submit as SQL query. 
		Instead used regular Statement object with predefined string. This format is necessary for the creation
		of unique tables.
		
2010-11-04 Duncan Tsai/Ed Gim <duncant@uci.edu, egmin@uci.edu>

	*SolrConnection.java: class created
	*SolrDataRetriever.java: class created
	*DBErrorHandler.java: class created
	*replaced all system.out.print errors with calls to DBErrorHandler.handle() 
	
2010-11-07 Duncan Tsai <duncant@uci.edu>

	*SolrDataRetriever.java (retriever()) : added basic code skeleton for processing queries and parsing results
	
2010-11-09 Duncan Tsai/Danielle Song <duncant@uci.edu, d.song31@gmail.com>

	*SolrDataRetriever.java (retriever()): completed full functionality. able to pull specified fields of data out of sorted documents 
		and store them into maps, which are then stored into arraylists.
	*SolrDataViewer.java: class created (simple test class that prints out retrieved values, will not be used once UI is implemented)

2010-11-11 Danielle Song <d.song31@gmail.com>

	* ConsoleUI.java: class created, simple console interface for testing purposes

2010-11-16 Danielle Song/Duncan Tsai <d.song31@gmail.com, duncant@uci.edu>

	*SolrDataAdder.java: class created. Is seperate from search functionality. Processes XML based CCD documents into SolrDocuments. (not yet fully 		functionality)

2010-11-18 Danielle Song/Duncan Tsai <d.song31@gmail.com, duncant@uci.edu>

	*ConsoleUI.java (initialize()): updated to interpret advanced search queries.
	*SolrDataRetriever.java (retriever()): updated to process advanced search queries

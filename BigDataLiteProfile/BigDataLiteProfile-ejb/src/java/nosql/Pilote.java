package nosql;

import oracle.kv.KVStore;
import java.util.List;
import java.util.Iterator;
import oracle.kv.KVStoreConfig;
import oracle.kv.KVStoreFactory;
import oracle.kv.FaultException;
import oracle.kv.StatementResult;
import oracle.kv.table.TableAPI;
import oracle.kv.table.Table;
import oracle.kv.table.Row;
import oracle.kv.table.PrimaryKey;

import oracle.kv.ConsistencyException;
import oracle.kv.RequestTimeoutException;
import java.lang.Integer;
import oracle.kv.table.TableIterator;
import oracle.kv.table.FieldRange;
import oracle.kv.table.MultiRowOptions;
import java.util.Arrays;
import oracle.kv.table.Index; 
import oracle.kv.table.IndexKey; 



/**
 * Cette classe fournit les fonctions nécessaires pour gérer les pilotes. Il s'agit des fonctions suivantes:
 * createTablePilote : créé la table pilote
 * alterTablePiloteAddColumnAge : ajouter une colonne dans la table pilote
 * dropColumnAgeFromPiloteTable: suppression de la colonne age
 * dropTablePilote : suprime la table pilote
 * createIndexOnPilote : crée un index sur la colonne plnom de la table pilote
 * dropIndexOnPilte : supprime l'index créé précédemment
 * insertPiloteRows : Insère de nouvelles lignes dans la table pilote
 * deletePiloteRow : supprime le pilote inséré connaissant le clé primaire
 * multiDeletePiloteRows : supprime les pilotes partageant une même partie de la clé primaire

 
 */
public class Pilote {

    private final KVStore store;

    /**
     * Runs the Pilote command line program.
     */
    public static void main(String args[]) {
        try {
            Pilote unPilote= new Pilote(args);
	    Pilote.testDdlCommandOnPilote(unPilote);
	    Pilote.testMajPilote(unPilote);
	    Pilote.testLectureLignesPilote(unPilote, "Baron",1,"Cap, Haiti");
	    Pilote.testLectureLignesCheckup(unPilote,  "Baron",1,"Cap, Haiti", 1);
	    Pilote.testLectureLignesViaIndex(unPilote,  "Baron");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses command line args and opens the KVStore.
     */
	Pilote(String[] argv) {

		String storeName = "kvstore";
		String hostName = "localhost";
		String hostPort = "5000";

		final int nArgs = argv.length;
		int argc = 0;

		store = KVStoreFactory.getStore
		    (new KVStoreConfig(storeName, hostName + ":" + hostPort));
	}

/**
* Affichage du résultat
*/

	private void displayResult(StatementResult result, String statement) {
		System.out.println("===========================");
		if (result.isSuccessful()) {
			System.out.println("Statement was successful:\n\t" +
			statement);
			System.out.println("Results:\n\t" + result.getInfo());
		} else if (result.isCancelled()) {
			System.out.println("Statement was cancelled:\n\t" +
			statement);
		} else {
			/*
			* statement was not successful: may be in error, or may still
			* be in progress.
			*/
			if (result.isDone()) {
				System.out.println("Statement failed:\n\t" + statement);
				System.out.println("Problem:\n\t" +
				result.getErrorMessage());
			}
			else {

				System.out.println("Statement in progress:\n\t" +
				statement);
				System.out.println("Status:\n\t" + result.getInfo());
			}
		}
	}

	/**
	* Affichage d’une ligne de la table PILOTE
	*/

	void displayPiloteRow (Row piloteRow) {
  		Integer plnum1 = 	piloteRow.get("plnum").asInteger().get();
		String plnom1 = piloteRow.get("plnom").asString().get();
		String dnaiss1 = piloteRow.get("dnaiss").asString().get();
		String adr1 = piloteRow.get("adr").asString().get();
		String tel1= piloteRow.get("tel").asString().get();
		Float sal1= piloteRow.get("sal").asFloat().get();

		System.out.println("Pilote  row : { plnum=" + plnum1 + 
		" plnom="+plnom1 +" dnaiss="+dnaiss1+" adr="+adr1+
		" tel="+tel1+ " salaire="+sal1+"}");
	}


/**
* Affichage d’une ligne de la table CHECKUP
*/
	void displayCheckupRow (Row checkupRow) {
		// Now retrieve the individaul fields from the row.
		String plnom1 = checkupRow.get("plnom").asString().get();
		String adr1 = checkupRow.get("adr").asString().get();
		Integer plnum1 = checkupRow.get("plnum").asInteger().get();
		Integer cunum1= checkupRow.get("cunum").asInteger().get();
		String cudate1= checkupRow.get("cudate").asString().get();
		String  curesultat1= checkupRow.get("curesultat").asString().get();
		System.out.println("Checkup row : { plnum=" + plnum1 + " plnom="+plnom1 +" adr="+adr1+" cunum ="+ cunum1 +" cudate1="
			+ cudate1+" curesultat="+curesultat1+"}");

}



	/**
	* Ce programme test les fonctions suivantes :
	* unPilote.createPilote();
	* unPilote.alterTablePiloteAddColumnAge();
	* unPilote.dropColumnAgeFromPiloteTable();
	* unPilote.createIndexOnPilote();
	* unPilote.dropIndexOnPilte();
	* unPilote.dropTablePilote();
	*/
	public static void testDdlCommandOnPilote(Pilote unPilote){
		unPilote.dropTablePilote();
		unPilote.createPilote();
		unPilote.alterTablePiloteAddColumnAge();
		unPilote.dropColumnAgeFromPiloteTable();
		unPilote.createIndexOnPilote();
		unPilote.dropIndexOnPilte();
		unPilote.dropTablePilote();

	}

	/**
	* Ce programme test les fonctions suivantes :
	* unPilote.createPilote();
	* unPilote.createIndexOnPilote();
	* unPilote.insertPiloteRows();
	* unPilote.deletePiloteRow();
	* multiDeletePiloteRows()
	*/
	public static void testMajPilote(Pilote unPilote){
		unPilote.createPilote();
		unPilote.createCheckup();
		unPilote.createIndexOnPilote();
		unPilote.insertPiloteRows();
		unPilote.deletePiloteRow("Icare", 1, "Athène, Grèce");
		unPilote.multiDeletePiloteRows("Icare", "Athène, Grèce");
	}
																						
	/**
	* Ce programme test les fonctions suivantes :
	* unPilote.getPiloteByKey(plnom, plnum);
	*/
	public static void testLectureLignesPilote(Pilote unPilote,  String plnom, int plnum, String adr){
		unPilote.getPiloteByKey(plnom, plnum, adr);
		unPilote.multiGetPiloteByPartialKey(plnom, adr);
		unPilote.multiGetTableIteratorOnPiloteRows(plnom, adr);

	}


	/**
	* Ce programme test les fonctions suivantes :
	* unPilote.getCheckupByKey;
	*/
	public static void testLectureLignesCheckup(Pilote unPilote,  String plnom, int plnum, String adr, int cunum){
		unPilote.getCheckupByKey(plnom,  adr, plnum, cunum);
		unPilote.multiGetCheckupAncestorsOrChild(plnom, adr);
		unPilote.multiGetCheckupChildAndAncestors(plnom, adr);


	}

	public static void testLectureLignesViaIndex(Pilote unPilote,  String plnom){
			unPilote.multiGetOverIndexIdxPilotePlnom(plnom);


	}

	public void createPilote() {
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : createPilote *********************************" );

		try {
			/*
			* Add a table to the database.
			* Execute this statement asynchronously.
			*/
			statement =
			"create table pilote("+
			"plnum    INTEGER,"+  
			"plnom  STRING,"+
			"dnaiss STRING,"+ 
			"adr    STRING,"+ 
			"tel    STRING,"+ 
			"sal    FLOAT,"+ 
			"PRIMARY KEY (shard(plnom, adr), plnum))";

			result = store.executeSync(statement);
			displayResult(result, statement);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
	}

	public void createCheckup() {
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : createPilote *********************************" );

		try {
			/*
			* Add a table to the database.
			* Execute this statement asynchronously.
			*/
			statement =
			"create table pilote.checkup("+
			"cunum    INTEGER,"+  
			"cudate STRING,"+ 
			"curesultat    STRING,"+ 
			"PRIMARY KEY (cunum))";

			result = store.executeSync(statement);
			displayResult(result, statement);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
	}

	/**
 	* alterTablePiloteAddColumnAge : ajouter une colonne dans la table pilote
	*/

	public void alterTablePiloteAddColumnAge() {
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : alterTablePiloteAddColumnAge *********************************" );

		try {
			/*
			* Ajout d'une colonne à la table pilote.
			* Execute this statement asynchronously.
			*/
			statement ="alter table pilote (add age INTEGER)";

			result = store.executeSync(statement);
			displayResult(result, statement);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
	}

	/**
	 * dropColumnAgeFromPiloteTable: suppression de la colonne age
	*/

	public void dropColumnAgeFromPiloteTable() {
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : dropColumnAgeFromPiloteTable *********************************" );

		try {
			/*
			* Suppression d'une colonne à la table pilote.
			* Execute this statement asynchronously.
			*/
			statement ="alter table pilote (drop age)";

			result = store.executeSync(statement);
			displayResult(result, statement);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
	}



	/**
 	* createIndexOnPilote : crée un index sur la colonne plnom de la table pilote
 	*/

	public void createIndexOnPilote() {
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : createIndexOnPilote *********************************" );

		try {
			/*
			* Création d'un index sur la colonne PLNOM de la table pilote.
			* Execute this statement asynchronously.
			*/
			statement ="create index idx_pilote_plnom on pilote(plnom)";

			result = store.executeSync(statement);
			displayResult(result, statement);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
	}

	/**
	* dropIndexOnPilte : supprime l'index créé précédemment
	*/
	public void dropIndexOnPilte() {
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : dropIndexOnPilte *********************************" );

		try {
			/*
			* Suppression d'un index sur la colonne PLNOM de la table pilote.
			* Execute this statement asynchronously.
			*/
			statement ="drop index idx_pilote_plnom on pilote";

			result = store.executeSync(statement);
			displayResult(result, statement);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
	}

	/**
 	* dropTablePilote : suprime la table pilote
 	*/

	public void dropTablePilote() {
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : dropTablePilote *********************************" );

		try {
			/*
			* Suppression de la table pilote.
			*/
			statement ="drop table pilote";

			result = store.executeSync(statement);
			displayResult(result, statement);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
	}

	/**
	* insertAPiloteRow : Insère une nouvelle ligne dans la table pilote
	*/

	private void insertAPiloteRow(int plnum, String plnom, String dnaiss, String adr, String tel, float sal){
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : insertAPiloteRow *********************************" );

		try {

			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tablePilote = tableH.getTable("pilote");
			// Get a Row instance
			Row piloteRow = tablePilote.createRow();
			// Now put all of the cells in the row.
			// This does NOT actually write the data to
			// the store.


			// Create one row
			piloteRow.put("plnum", plnum);
			piloteRow.put("plnom", plnom);
			piloteRow.put("dnaiss", dnaiss);
			piloteRow.put("adr", adr);
			piloteRow.put("tel", tel);
			piloteRow.put("sal", sal);


			// Now write the table to the store.
			// "item" is the row's primary key. If we had not set that value,
			// this operation will throw an IllegalArgumentException.
			tableH.put(piloteRow, null, null);

		} 
/*
		catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} 
		catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
*/
		catch (ConsistencyException ce) {
			System.out.println(" Consistency exception:\n" + ce.getMessage());
		} 
		catch (RequestTimeoutException re) {
			System.out.println(" Request timeout exception:\n" + re.getMessage());
		}

		catch (Exception e) {
			System.out.println(" Toutes Exceptions:\n" + e.getMessage());
		}

	}


	/**
	* insertAPiloteRow : Insère une nouvelle ligne dans la table pilote
	*/

	private void insertACheckupRow(int plnum, String plnom,  String adr, int cunum, String cudate, String curesultat){
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : insertAPiloteRow *********************************" );

		try {

			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tableCheckup= tableH.getTable("pilote.checkup");
			// Get a Row instance
			Row checkupRow = tableCheckup.createRow();
			// Now put all of the cells in the row.
			// This does NOT actually write the data to
			// the store.


			// Create one row
			checkupRow.put("plnom", plnom);
			checkupRow.put("adr", adr);
			checkupRow.put("plnum", plnum);
			checkupRow.put("cunum", cunum);
			checkupRow.put("cudate", cudate);
			checkupRow.put("curesultat", curesultat);


			// Now write the table to the store.
			// "item" is the row's primary key. If we had not set that value,
			// this operation will throw an IllegalArgumentException.
			tableH.put(checkupRow, null, null);

		} 
		catch (ConsistencyException ce) {
			System.out.println(" Consistency exception:\n" + ce.getMessage());
		} 
		catch (RequestTimeoutException re) {
			System.out.println(" Request timeout exception:\n" + re.getMessage());
		}

		catch (Exception e) {
			System.out.println(" Toutes Exceptions:\n" + e.getMessage());
		}

	}
 
	/**
	* insertPiloteRows : Insère de nouvelles lignes dans la table pilote en appelant la fonction insertAPiloteRow
	*/
 
	public void insertPiloteRows() {
		System.out.println("\n********************************** Dans : insertPiloteRows *********************************" );

		try {
			this.insertAPiloteRow(1, "Gagarin", "09/03/1934", "Klouchino, Russie", "0071122334455", 10000.75F);
			this.insertAPiloteRow(1, "Jähn Sigmund", "13/02/1937", "Morgenröthe-Rautenkranz, Allemagne", "0049199999999", 12000.5F);
			this.insertAPiloteRow(1, "Icare", "13/02/1947", "Athène, Grèce", "00300623344556", 8000.5F);
			this.insertAPiloteRow(1, "Icare", "13/02/1000", "Olympe, Grèce", "00300623344577", 8500.5F);
			this.insertAPiloteRow(1, "Icare", "14/02/1900", "Le Pirée, Grèce", "00300623344588", 8600.5F);
			this.insertAPiloteRow(1, "Zorro", "14/02/1880", "Los Angeles, USA", "0010623344588", 8600.5F);
			this.insertAPiloteRow(1, "Pagnol", "14/02/1980", "Marseille, France", "00330623344589", 8700.5F);
			this.insertAPiloteRow(1, "Bleck", "14/02/2010", "Marseille, France", "00330623344589", 8700.5F);
			this.insertAPiloteRow(1, "Erzulie", "13/02/1804", "Port-au-Prince, Haiti", "005090623344557", 13000.5F);
			this.insertAPiloteRow(2, "Erzulie", "14/04/1804", "Port-au-Prince, Haiti", "0050906233445607", 14000.5F);
			this.insertAPiloteRow(1, "Baron", "15/01/1809", "Cap, Haiti", "0050906233445607", 14000.5F);
			this.insertAPiloteRow(2, "Baron", "16/04/1812", "Cap, Haiti", "0050906233445607", 14000.5F);
			this.insertAPiloteRow(3, "Baron", "14/04/1890", "Cap, Haiti", "0050906233445607", 14000.5F);

			// Insert Pilote Checkup

			this.insertACheckupRow(1, "Baron",  "Cap, Haiti", 1, "12/12/1820", "RAS");
			this.insertACheckupRow(1, "Baron",  "Cap, Haiti", 2, "12/12/1821", "MALADE");
			this.insertACheckupRow(4, "Baron",  "Cap, Haiti", 1, "11/11/1823", "RAS");


		} 
/*
		catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
*/
		catch (ConsistencyException ce) {
			System.out.println(" Consistency exception:\n" + ce.getMessage());
		} 
		catch (RequestTimeoutException re) {
			System.out.println(" Request timeout exception:\n" + re.getMessage());
		}

		catch (Exception e) {
			System.out.println(" Toutes Exceptions:\n" + e.getMessage());
		}

	}

	/**
	* deletePiloteRow : supprime un pilote connaissant la clé entièrement renseignée
	*/
 
	public void deletePiloteRow(String plnom, int plnum, String adr) {
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : deletePiloteRow *********************************" );

		try {

			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tablePilote = tableH.getTable("pilote");

			// Get the primary key for the row that we want to delete
			PrimaryKey pilotePrimaryKey = tablePilote.createPrimaryKey();
			pilotePrimaryKey.put("plnom", plnom);
			pilotePrimaryKey.put("plnum", plnum);
			pilotePrimaryKey.put("adr", adr);


			// Now delete rows with the same name.
			// this operation will throw an IllegalArgumentException.
			tableH.multiDelete(pilotePrimaryKey, null, null);

		} catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
		catch (Exception e) {
			System.out.println(" Toutes Exceptions:\n" + e.getMessage());
		}

	}

	/**
	* multiDeletePiloteRows : supprime les pilotes ayant un même nom. PK partiellement renseignée
	*/
 
	public void multiDeletePiloteRows(String plnom, String  adr) {
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : multiDeletePiloteRows *********************************" );

		try {

			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tablePilote = tableH.getTable("pilote");

			// Get the primary key for the row that we want to delete
			PrimaryKey pilotePrimaryKey = tablePilote.createPrimaryKey();
			// Supprimer tous les pilotes ayant le nom Icare
			// La PK composé (plnom, plnum, adr) sera renseignée partiellement(plnome et plnum uniquement).
			pilotePrimaryKey.put("plnom", plnom);
			pilotePrimaryKey.put("adr", adr);


			// Now delete the table rows in the store.
			// "item" is the row's primary key. If we had not set that value,
			// this operation will throw an IllegalArgumentException.
			tableH.multiDelete(pilotePrimaryKey, null, null);

		}
/* catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
*/
		catch (ConsistencyException ce) {
			System.out.println(" Consistency exception:\n" + ce.getMessage());
		} 
		catch (RequestTimeoutException re) {
			System.out.println(" Request timeout exception:\n" + re.getMessage());
		}
		catch (Exception e) {
			System.out.println(" Toutes Exceptions:\n" + e.getMessage());
		}

	}



	/**
	* getPiloteByKey : Lecture d'un pilote connaissant sa clé primaire entièrement renseignée
	*/
 
	private void getPiloteByKey(String plnom,  int plnum, String adr){
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : getPiloteByKey *********************************" );
		try {

			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tablePilote = tableH.getTable("pilote");
			

			PrimaryKey key = tablePilote.createPrimaryKey();
			key.put("plnom", plnom);
			key.put("plnum", plnum);
			key.put("adr", adr);

			// Retrieve the row. This performs a store read operation.
			// Exception handling is skipped for this trivial example.
			Row row = tableH.get(key, null);

			// Now retrieve the individual fields from the row.
			// Now retrieve the individual fields from the row.
			displayPiloteRow(row);
		

		} 
/*
		catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} 
		catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
*/
		catch (ConsistencyException ce) {
			System.out.println(" Consistency exception:\n" + ce.getMessage());
		} 
		catch (RequestTimeoutException re) {
			System.out.println(" Request timeout exception:\n" + re.getMessage());
		}
		catch (Exception e) {
			System.out.println(" Toutes Exceptions:\n"); 
			e.printStackTrace();
		}

	}
 

	/**
	* multiGetPiloteByPartialKey : Lecture des pilotes ayant une même shard key(même nom et même numéro). La PK partiellement renseignée
	*/
 

	private void multiGetPiloteByPartialKey(String plnom, String adr){
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : multiGetPiloteByPartialKey *********************************" );

		try {

			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tablePilote = tableH.getTable("pilote");
			

			PrimaryKey key = tablePilote.createPrimaryKey();
			key.put("plnom", plnom);
			key.put("adr", adr);

			// Retrieve the rows. This performs a store read operation.
			// Exception handling is skipped for this trivial example.
			List<Row> myPiloteRows = null;
			myPiloteRows = tableH.multiGet(key, null, null);
	
			for (Row piloteRow: myPiloteRows) {
				// Now retrieve the individual fields from the row.
				displayPiloteRow(piloteRow);
			}
		

		}
/* 
		catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} 
		catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
*/
		catch (ConsistencyException ce) {
			System.out.println(" Consistency exception:\n" + ce.getMessage());
		} 
		catch (RequestTimeoutException re) {
			System.out.println(" Request timeout exception:\n" + re.getMessage());
		}
		catch (Exception e) {
			System.out.println(" Toutes Exceptions:\n" + e.getMessage());
		}


	}

	/**
	* multiGetIteratorOnPiloteRows : Lecture des pilotes ayant une même shard key(même nom et même numéro). La PK partiellement renseignée
	*/
 

	private void multiGetTableIteratorOnPiloteRows(String plnom, String adr){
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : multiGetPiloteByPartialKey *********************************" );

		try {

			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tablePilote = tableH.getTable("pilote");
			

			PrimaryKey key = tablePilote.createPrimaryKey();
			key.put("plnom", plnom);
			key.put("adr", adr);

			// Exception handling is omitted, but in production code
			// ConsistencyException, RequestTimeException, and FaultException
			// would have to be handled.
			TableIterator<Row> iter = tableH.tableIterator(key, null, null);
			while (iter.hasNext()) {
				Row piloteRow = iter.next();
				// Examine your row's fields here
				// Now retrieve the individual fields from the row.
				displayPiloteRow(piloteRow);
			}
	

		} 
/*
		catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} 
		catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
*/
		catch (ConsistencyException ce) {
			System.out.println(" Consistency exception:\n" + ce.getMessage());
		} 
		catch (RequestTimeoutException re) {
			System.out.println(" Request timeout exception:\n" + re.getMessage());
		}

		catch (Exception e) {
			System.out.println(" Toutes Exceptions:\n" + e.getMessage());
		}

	}


	/**
	* getCheckupByKey : Lecture du checkup d’un pilote connaissant sa clé primaire 	   entièrement renseignée
	*/
	private void getCheckupByKey(String plnom,   String adr, int plnum,	int cunum){
		System.out.println("\n********************************** Dans : getCheckupByKey *********************************" );

		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		try {
			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tableCheckup = tableH.getTable("pilote.checkup");
			
			PrimaryKey key=tableCheckup.createPrimaryKey();
			key.put("plnom", plnom);
			key.put("adr", adr);
			key.put("plnum", plnum);
			key.put("cunum", cunum);
			
			// Retrieve the row. This performs a store read operation.
			// Exception handling is skipped for this trivial example.
			Row row = tableH.get(key, null);
			// Now retrieve the individual fields from the row.
			 displayCheckupRow (row);
		} 
		catch (ConsistencyException ce) {
			System.out.println(" Consistency exception:\n" + ce.getMessage());
		} 
		catch (RequestTimeoutException re) {
			System.out.println(" Request timeout exception:\n" + re.getMessage());
		}

		catch (Exception e) {
			System.out.println(" Toutes Exceptions:\n" + e.getMessage());
		}

	}



	/**
	* multiGetCheckupAncestorsOrChild: Lecture des pilotes ou des checkup de pilotes ayant 
	* une même shard key(même nom et même adresse). La PK partiellement renseignée
	*/
	private void multiGetCheckupAncestorsOrChild(String plnom, String adr){
		System.out.println("\n********************************** Dans : multiGetCheckupAncestorsOrChild*****************************" );

		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		try { 
			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tablePilote = tableH.getTable("pilote");
			Table tableCheckup = tableH.getTable("pilote.checkup");

			PrimaryKey key = tablePilote.createPrimaryKey();
			key.put("plnom", plnom);
			key.put("adr", adr);

			// Get a MultiRowOptions and tell it to look at both the child 
			// tables 
			MultiRowOptions mro = new MultiRowOptions(null, null, Arrays.asList(tableCheckup )); 

			// Retrieve the rows. 
			TableIterator<Row> iter = tableH.tableIterator(key, mro, null); 

			while (iter.hasNext()) { 
				Row row = iter.next(); 
				// display parent rows
				if (row.getTable().equals(tablePilote)) 
					displayPiloteRow (row);
				// display children rows
				else if (row.getTable().equals(tableCheckup))
					displayCheckupRow (row);
			 } 

		} 
		catch (ConsistencyException ce) {
			System.out.println(" Consistency exception:\n" + 	ce.getMessage());
		} 
		catch (RequestTimeoutException re) {
			System.out.println(" Request timeout exception:\n" + re.getMessage());
		}
		catch (Exception e) {
			System.out.println(" Toutes Exceptions:\n" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	* multiGetCheckupChildAndAncestors: Lecture des checkup de pilotes et des ancêtres liés aux checkup ayant 
	* une même shard key(même nom et même adresse). La PK partiellement renseignée
	*/
	private void multiGetCheckupChildAndAncestors(String plnom, String adr){
		System.out.println("\n********************************** Dans : multiGetCheckupChildAndAncestors *****************************" );

		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		try { 
			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tablePilote = tableH.getTable("pilote");
			Table tableCheckup = tableH.getTable("pilote.checkup");

			PrimaryKey key = tableCheckup.createPrimaryKey();
			key.put("plnom", plnom);
			key.put("adr", adr);

			// Get a MultiRowOptions and tell it to look at both the child 
			// tables 
			MultiRowOptions mro = new MultiRowOptions(null, Arrays.asList(tablePilote ), null); 

			// Retrieve the rows. 
			TableIterator<Row> iter = tableH.tableIterator(key, mro, null); 

			while (iter.hasNext()) { 
				Row row = iter.next(); 
				// display parent rows
				if (row.getTable().equals(tablePilote)) 
					displayPiloteRow (row);
				// display children rows
				else if (row.getTable().equals(tableCheckup))
					displayCheckupRow (row);
			 } 

		} 
		catch (ConsistencyException ce) {
			System.out.println(" Consistency exception:\n" + 	ce.getMessage());
		} 
		catch (RequestTimeoutException re) {
			System.out.println(" Request timeout exception:\n" + re.getMessage());
		}
		catch (Exception e) {
			System.out.println(" Toutes Exceptions:\n" + e.getMessage());
			e.printStackTrace();
		}

	}


	/**
	* multiGetOverIndex: Lecture via index
	*/
	private void multiGetOverIndexIdxPilotePlnom(String plnom){
		System.out.println("\n********************************** Dans : multiGetOverIndexIdxPilotePlnom *****************************" );

		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		try { 
			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tablePilote = tableH.getTable("pilote");
	
			// Construct the IndexKey. The name we gave our index when 			
			// we created it was ‘idx_pilote_plnom'. 
			Index idxPilotePlnom = tablePilote.getIndex("idx_pilote_plnom"); 
			IndexKey idxPilotePlnomKey = idxPilotePlnom.createIndexKey(); 
			idxPilotePlnomKey.put("plnom", plnom);

			// Retrieve the rows. 
			TableIterator<Row> iter = tableH.tableIterator(idxPilotePlnomKey , null, null); 
			int cpt=0;
			while (iter.hasNext()) { 
				cpt++;
				Row row = iter.next(); 
				// display pilote rows
				displayPiloteRow (row);	
			 } 
			System.out.println("\nnb ligne: cpt ="+cpt);


		} catch (ConsistencyException ce) {
			System.out.println("Consistency exception:\n" + ce.getMessage());
		} catch (RequestTimeoutException re) {
			System.out.println("Request timeout exception:\n" + re.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}

	}



};


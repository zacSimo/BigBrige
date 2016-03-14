/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosql;

import java.util.Random;
import oracle.kv.ConsistencyException;
import oracle.kv.FaultException;
import oracle.kv.KVStore;
import oracle.kv.KVStoreConfig;
import oracle.kv.KVStoreFactory;
import oracle.kv.RequestTimeoutException;
import oracle.kv.StatementResult;
import oracle.kv.table.Row;
import oracle.kv.table.Table;
import oracle.kv.table.TableAPI;

/**
 *
 * @author oracle
 */
public class Profil {
    private final KVStore store;
    private static String driverHive = "org.apache.hadoop.hive.jdbc.HiveDriver";
   //private final oracleDWH;
    
    public static void main(String[] args){
        try{
            Profil u = new Profil(args);
            u.dropTableProfil();
            u.createProfil();
            Random r = new Random();
            for(int i = 1;i<=5;i++){
                u.insertAProfilRow(i, "photo_"+i, r.nextInt(4)+1);
            }
            
            
        }catch(RuntimeException r){r.getMessage();}
    }
    
    
    public Profil(String argv[]){
        String storeName = "kvstore";
        String hostName = "localhost";
	String hostPort = "5000";

	final int nArgs = argv.length;
	int argc = 0;

	store = KVStoreFactory.getStore(new KVStoreConfig(storeName, hostName + ":" + hostPort));
        
        
    }
    
    public void createProfil() {
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : createProfil *********************************" );

		try {
			/*
			* Add a table to the database.
			* Execute this statement asynchronously.
			*/
			statement =
			"create table Profil("+
			"idProfil  INTEGER,"+  
			"photo  STRING,"+
			"idUser INTEGER,"+  
			"PRIMARY KEY (idProfil))";

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

	public void dropTableProfil() {
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : dropTableProfil *********************************" );

		try {
			/*
			* Suppression de la table pilote.
			*/
			statement ="drop table Profil";

			result = store.executeSync(statement);
			displayResult(result, statement);
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid statement:\n" + e.getMessage());
		} catch (FaultException e) {
			System.out.println("Statement couldn't be executed, please retry: " + e);
		}
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
	* insertAProfilRow : Insère une nouvelle ligne dans la table Profil
	*/

	private void insertAProfilRow(int id, String photo, int idUser){
		TableAPI tableAPI = store.getTableAPI();
		StatementResult result = null;
		String statement = null;
		System.out.println("\n********************************** Dans : insertAProfilRow *********************************" );

		try {

			TableAPI tableH = store.getTableAPI();
			// The name you give to getTable() must be identical
			// to the name that you gave the table when you created
			// the table using the CREATE TABLE DDL statement.
			Table tableProfil = tableH.getTable("Profil");
			// Get a Row instance
			Row ProfilRow = tableProfil.createRow();
			// Now put all of the cells in the row.
			// This does NOT actually write the data to
			// the store.


			// Create one row
			ProfilRow.put("id", id);
			ProfilRow.put("photo", photo);
			ProfilRow.put("idUser", idUser);
			
			


			// Now write the table to the store.
			// "item" is the row's primary key. If we had not set that value,
			// this operation will throw an IllegalArgumentException.
			tableH.put(ProfilRow, null, null);

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
}


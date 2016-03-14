/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package initBDUser;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import session.GestionUserProfil;

/**
 *
 * @author oracle
 */
@Singleton
@Startup
@LocalBean
public class InitBDUser {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @EJB
    public GestionUserProfil gup;
    
    @PostConstruct
    public void init(){
        //gup.creerDefaultUsers();
    }
}

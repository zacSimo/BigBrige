/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;



import entities.Chanson;
import entities.Chanteur;
import entities.Profil;
import entities.Utilisateur;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.inject.Named;
import session.GestionUserProfil;

/**
 *
 * @author oracle
 */
@ManagedBean
@SessionScoped
@Named(value = "userMBean")
public class UserMBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    private String nomUser;
    private String prenomUser;
    private String email;
    private List<Utilisateur> users;
    private String nomGroupe;
    private String titreChanson;
    private String nomChanteur;
    private Chanteur chanteur;
    private Chanson chanson;
    
    @EJB
    private GestionUserProfil gup;
    
    public void insertUser(String nom, String prenom, String email, Profil profil){
        gup.insertUser(new Utilisateur(nom,prenom,email, profil));
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Utilisateur> getUsers() {
        users = gup.getListUSers();
        return users;
    }

    public void setUsers(List<Utilisateur> users) {
        this.users = users;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public String getTitreChanson() {
        return titreChanson;
    }

    public void setTitreChanson(String titreChanson) {
        this.titreChanson = titreChanson;
    }

    public String getNomChanteur() {
        return nomChanteur;
    }

    public void setNomChanteur(String nomChanteur) {
        this.nomChanteur = nomChanteur;
    }

    public GestionUserProfil getGup() {
        return gup;
    }

    public void setGup(GestionUserProfil gup) {
        this.gup = gup;
    }
    
    public void deleteUser(int id){
        gup.deleteUser(gup.getUserById(id));
    }
    
}

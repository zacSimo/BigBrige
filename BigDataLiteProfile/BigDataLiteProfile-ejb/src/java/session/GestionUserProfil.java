/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entities.Chanson;
import entities.Chanteur;
import entities.Groupe;
import entities.Profil;
import entities.Utilisateur;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author oracle
 */
@Stateless
@LocalBean
public class GestionUserProfil {

    @PersistenceContext
    private EntityManager em;

    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public GestionUserProfil() {
           
    }

    public void insertUser(Utilisateur user) {
        em.persist(user);
    }

    public Utilisateur getUserById(int id){
        return em.find(Utilisateur.class, id);
    }
    public void insertGroupe(Groupe groupe) {
        em.persist(groupe);
    }

    public Groupe getGroupeById(int id){
        return em.find(Groupe.class, id);
    }
    
    public Chanson getChansonById(int id){
        return em.find(Chanson.class, id);
    }
    
    public Chanteur getChanteurById(int id){
        return em.find(Chanteur.class, id);
    }
    public void insertChanteur(Chanteur chanteur) {
        em.persist(chanteur);
    }

    public void insertChanson(Chanson chanson) {
        em.persist(chanson);
    }

    public void editProfil(Profil profil) {
        em.persist(profil);
    }

    public void deleteUser(Utilisateur u){
        em.remove(u);
    }
    
    public void deleteGroupe(Groupe gp){
        em.remove(gp);
    }
    
    public void deleteProfil(Profil p){
        em.remove(p);
    }
    
    public void deleteChanson(Chanson chanson){
        em.remove(chanson);
    }
    
    public void deleteChanteur(Chanteur chanteur){
        em.remove(chanteur);
    }
    public List<Utilisateur> getListUSers() {
        Query q = em.createQuery("select u from Utilisateur u");
        return q.getResultList();
    }

    public List<Profil> getListProfils() {
        Query q = em.createQuery("select p from Profil p;");
        return q.getResultList();
    }

    public List<Groupe> getListGroupe() {
        Query q = em.createQuery("select g from Groupe p;");
        return q.getResultList();
    }

    public List<Chanteur> getListChanteurs() {
        Query q = em.createQuery("select c from Chanteur c;");
        return q.getResultList();
    }

    public List<Chanson> getListChanson() {
        Query q = em.createQuery("select c from Chanson c;");
        return q.getResultList();
    }
    
    
    public void creerDefaultUsers(){
     for(int i=1;i<=150;i++){
         Profil profil = new Profil();
//         profil.setId(i);
         profil.setPhoto("poto_profil_1");
         em.persist(profil);
         Utilisateur u = new Utilisateur("nomuser_"+i, "prenomuser_"+i, "nomuser_"+i+"."+"prenomuser_"+i+"@yahoo.fr", profil);
         u.setProfil(profil);
         em.persist(u);
        
         Chanteur chanteur = new Chanteur();
         chanteur.setDateNaiss(new Date("12/10/1999"));
         chanteur.setNomChanteur("Chanteur_"+i);
         em.persist(chanteur);
         
         for(int j=1;j<=10;j++){
             Chanson ch = new Chanson();
             ch.setChanteur(chanteur);
             ch.setDuree(Long.MIN_VALUE);
             ch.setGenre("POP"+j);
             ch.setTitreChansson("titre_"+j);
             em.persist(ch);
         }
         
     }
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;


/**
 *
 * @author oracle
 */
@Entity
public class Profil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String photo;
    
    @OneToOne(mappedBy = "profil")
    private Utilisateur user;
    
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Groupe> listeGroupe;
    
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Chanson> listChanson;
    
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Chanteur> listChanteur;
    
    public Profil() {
        this.listChanteur = new ArrayList<>();
        this.listChanson = new ArrayList<>();
        this.listeGroupe = new ArrayList<>();
    }
    
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Groupe> getListeGroupe() {
        return listeGroupe;
    }

    public void setListeGroupe(List<Groupe> listeGroupe) {
        this.listeGroupe = listeGroupe;
    }

    public List<Chanson> getListChanson() {
        return listChanson;
    }

    public void setListChanson(List<Chanson> listChanson) {
        this.listChanson = listChanson;
    }

    public List<Chanteur> getListChanteur() {
        return listChanteur;
    }

    public void setListChanteur(List<Chanteur> listChanteur) {
        this.listChanteur = listChanteur;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }
    
    
    public Integer getId() {
        return id;
    }

//    public void setId(Integer id) {
//        this.id = id;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profil)) {
            return false;
        }
        Profil other = (Profil) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.Profil[ id=" + id + " ]";
    }
    
}

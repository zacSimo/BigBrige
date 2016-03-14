/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author oracle
 */
@Entity
public class Chanteur implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nomChanteur;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNaiss;

    
    @OneToMany(mappedBy = "chanteur")
    private List<Chanson> listChanson;
    
    @ManyToMany
    private List<Profil> listProfil;

    public Chanteur(String nomChanteur, Date dateNaiss) {
        this.nomChanteur = nomChanteur;
        this.dateNaiss = dateNaiss;
    }

    public Chanteur() {
    }

    
    public Date getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(Date dateNaiss) {
        this.dateNaiss = dateNaiss;
    }
    
    public String getNomChanteur() {
        return nomChanteur;
    }

    public void setNomChanteur(String nomChanteur) {
        this.nomChanteur = nomChanteur;
    }

    public List<Chanson> getListChanson() {
        return listChanson;
    }

    public void setListChanson(List<Chanson> listChanson) {
        this.listChanson = listChanson;
    }
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Chanteur)) {
            return false;
        }
        Chanteur other = (Chanteur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Chanteur[ id=" + id + " ]";
    }
    
}

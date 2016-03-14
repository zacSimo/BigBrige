/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


/**
 *
 * @author oracle
 */
@Entity
public class Chanson implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String titreChansson;
    private String genre;
    //duree chanson en milliseconde
    private Long duree;
    
    @ManyToOne(fetch=FetchType.LAZY)
    private Chanteur chanteur;

    public Chanson() {
    }

    public Chanson(String titreChansson, String genre, Long duree) {
        this.titreChansson = titreChansson;
        this.genre = genre;
        this.duree = duree;
    }

    
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Long getDuree() {
        return duree;
    }

    public void setDuree(Long duree) {
        this.duree = duree;
    }

    public Chanteur getChanteur() {
        return chanteur;
    }

    public void setChanteur(Chanteur chanteur) {
        this.chanteur = chanteur;
    }

    
    public String getTitreChansson() {
        return titreChansson;
    }

    public void setTitreChansson(String titreChansson) {
        this.titreChansson = titreChansson;
    }


    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
}

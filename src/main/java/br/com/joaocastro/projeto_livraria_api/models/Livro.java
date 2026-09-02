package br.com.joaocastro.projeto_livraria_api.models;

import java.sql.Date;

public class Livro {
    private String name;
    private String description;
    private Date duration;
    private Date start_date;

    public Livro(String name, Date duration, Date start_date) {
        this.name = name;
        this.duration = duration;
        this.start_date = start_date;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }
}

package fromL2;

import java.io.*;

public class Company implements Serializable {
    int id;
    String name;
    String date;
    int ppl;
    int sales;
    public Company(){

    }

    public Company(String name, String date, int ppl, int sales) {
        this.name = name;
        this.date = date;
        this.ppl = ppl;
        this.sales = sales;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPpl() {
        return ppl;
    }

    public void setPpl(int ppl) {
        this.ppl = ppl;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", ppl=" + ppl +
                ", sales=" + sales +
                '}';
    }
}

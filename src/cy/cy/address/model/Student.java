package cy.cy.address.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private final StringProperty sno;
    private final StringProperty sname;
    private final StringProperty ssex;
    private final StringProperty classes;
    /**
     * 默认函数
     */
    public Student() {
        this(null, null,null,null);
    }

    /**
     * 构造初始函数
     *
     * @param sno
     * @param sname
     * @param ssex
     * @param classes
     */public Student(String sno, String sname,String ssex,String classes) {
        this.sno = new SimpleStringProperty(sno);
        this.sname = new SimpleStringProperty(sname);
        this.ssex = new SimpleStringProperty(ssex);
        this.classes = new SimpleStringProperty(classes);
    }

    public String getSno() {
        return sno.get();
    }

    public void setSno(String sno) {
        this.sno.set(sno);
    }

    public StringProperty snoProperty() {
        return sno;
    }

    public String getSname() { return sname.get(); }

    public void setSname(String sname) {
        this.sname.set(sname);
    }

    public StringProperty snameProperty() { return sname; }

    public String getSsex() {
        return ssex.get();
    }

    public void setSsex(String ssex) {
        this.ssex.set(ssex);
    }

    public StringProperty ssexProperty() {
        return ssex;
    }

    public String getClasses() {
        return classes.get();
    }

    public void setClasses(String classes) {
        this.classes.set(classes);
    }

    public StringProperty classesProperty() {
        return classes;
    }
}

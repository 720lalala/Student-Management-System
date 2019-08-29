package cy.cy.address.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {
    private final StringProperty cno;
    private final StringProperty cname;
    private final StringProperty classes;
    /**
     * 默认函数
     */
    public Course() {
        this(null, null,null);
    }

    /**
     * 构造初始函数
     *
     * @param cno
     * @param cname
     * @param classes
     */public Course(String cno, String cname,String classes) {
        this.cno = new SimpleStringProperty(cno);
        this.cname = new SimpleStringProperty(cname);
        this.classes = new SimpleStringProperty(classes);
    }

    public String getCno() {
        return cno.get();
    }

    public void setCno(String cno) {
        this.cno.set(cno);
    }

    public StringProperty cnoProperty() {
        return cno;
    }

    public String getCname() { return cname.get(); }

    public void setCname(String cname) {
        this.cname.set(cname);
    }

    public StringProperty cnameProperty() { return cname; }

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

package cy.cy.address.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Grade {
    private final StringProperty sno;
    private final StringProperty cno;
    private final StringProperty score;

    /**
     * 默认函数
     */
    public Grade() {
        this(null, null,null);
    }

    /**
     * 构造初始函数
     *
     * @param cno
     * @param sno
     * @param score
         */public Grade(String sno, String cno, String score) {
        this.cno = new SimpleStringProperty(cno);
        this.sno = new SimpleStringProperty(sno);
        this.score = new SimpleStringProperty(score);
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

    public String getSno() {
        return sno.get();
    }

    public void setSno(String sno) {
        this.sno.set(sno);
    }

    public StringProperty snoProperty() {
        return sno;
    }

    public String getScore() {
        return score.get();
    }

    public void setScore(String score) {
        this.score.set(score);
    }

    public StringProperty scoreProperty() {
        return score;
    }
}

package cy.cy.address.model;

import javafx.beans.property.*;

public class Login {
    //final修饰的变量就是一个常量，只能赋值一次
    private  StringProperty LoginName;
    private  StringProperty password;

    /**
     * 默认函数
     */
    public Login() {
        this(null, null);
    }

    /**
     * 构造初始函数
     *
     * @param LoginName
     * @param password
     */
    public Login(String LoginName, String password) {
        this.LoginName = new SimpleStringProperty(LoginName);
        this.password= new SimpleStringProperty(password);
    }

    public String getLoginName() {
        return LoginName.get();
    }

    public void setLoginName(String LoginName) {
        this.LoginName.set(LoginName);
    }

    public StringProperty loginNameProperty() {
        return LoginName;
    }

    public String getpassword() {
        return password.get();
    }

    public void setpassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }


}

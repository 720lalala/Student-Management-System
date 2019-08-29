package cy.cy.address.Controller;

import cy.cy.address.MainApp;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class mainController {

    private MainApp mainapp;
    @FXML
    private StudentInformationController StuManController;
    @FXML
    private GradeInformationController GraManController;
    @FXML
    private CourseInformationController CouManController;
    public StudentInformationController getStuManController() {
        return StuManController;
    }
    public CourseInformationController getCouManController() {
        return CouManController;
    }
    public GradeInformationController getGraManController() {
        return GraManController;
    }
    public void setGraManController(GradeInformationController GraManController ) {
        this.GraManController=GraManController;
    }
    /**
     * *由主应用程序调用以将参考返回给自己。
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainapp = mainApp;

    }    @FXML
    private void handleexit() {
        mainapp.showprimaryStage();

    }
    public void refleshGra()
    {
        System.out.println(456789);
        GraManController.refresh();
    }


}

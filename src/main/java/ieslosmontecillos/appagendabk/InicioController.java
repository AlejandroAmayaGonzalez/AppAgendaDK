package ieslosmontecillos.appagendabk;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import javax.swing.text.View;
import java.io.IOException;

public class InicioController {
    @FXML
    private View inicio;

    private DataUtil dataUtil;
    ObservableList olProv;
    ObservableList olPers;
    private Pane rootMain = new Pane();

    @FXML
    public void iniciaApp(Event event){
        try{
            FXMLLoader fxmlLoader = new
                    FXMLLoader(getClass().getResource("login.fxml"));
            Pane rootLoginView = fxmlLoader.load();

            rootMain.getChildren().add(rootLoginView);
            LoginController loginController =
                    fxmlLoader.getController();

            loginController.setDataUtil(dataUtil);
            loginController.setOlPers(olPers);
            loginController.setOlProv(olProv);
            loginController.setRootMain(rootMain);

        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    public void setRootMain(Pane rootMain) {
        this.rootMain = rootMain;
    }

    public void setDataUtil(DataUtil dataUtil) {
        this.dataUtil = dataUtil;
    }

    public void setOlProv(ObservableList olProv) {
        this.olProv = olProv;
    }

    public void setOlPers(ObservableList olPers) {
        this.olPers = olPers;
    }
}

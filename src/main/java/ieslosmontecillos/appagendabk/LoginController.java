package ieslosmontecillos.appagendabk;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class LoginController {
    @javafx.fxml.FXML
    private Button btnLogin;
    @javafx.fxml.FXML
    private Label LabelError;
    @javafx.fxml.FXML
    private TextField textFieldEmail;
    @javafx.fxml.FXML
    private TextField textFieldContrasena;

    private DataUtil dataUtil;
    ObservableList olProv;
    ObservableList olPers;
    private Pane rootMain;

    @javafx.fxml.FXML
    public void SignIn(ActionEvent actionEvent) {

        int res = dataUtil.findUser(textFieldEmail.getText(), textFieldContrasena.getText());

        if (res == 0) {
            // Usuario correcto y con vigencia
            try {
                FXMLLoader fxmlLoader = new
                        FXMLLoader(getClass().getResource("AgendaView.fxml"));
                Pane rootAgendaView = fxmlLoader.load();
                rootMain.getChildren().add(rootAgendaView);

                AgendaViewController agendaViewController = fxmlLoader.getController();
                agendaViewController.setDataUtil(dataUtil);
                agendaViewController.setOlProvincias(olProv);
                agendaViewController.setOlPersonas(olPers);
                agendaViewController.cargarTodasPersonas();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else if (res == -1) {
            // El usuario no existe
            LabelError.setText("Usuario y/o contraseña incorrecto. Inténtelo con otro usuario");
        }else if (res == -2) {
            // Usuario encontrado pero sin vigencia
            LabelError.setText("Usuario deshabilitado. Inténtelo con otro usuario");
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

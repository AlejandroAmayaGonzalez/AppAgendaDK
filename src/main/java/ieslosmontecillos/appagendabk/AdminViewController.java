package ieslosmontecillos.appagendabk;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminViewController {
    @javafx.fxml.FXML
    private TableColumn<User, String> columnaEmail;
    @javafx.fxml.FXML
    private TableColumn<User, Integer> columnaID;
    @javafx.fxml.FXML
    private TableColumn<User, String> columnaContrasena;
    @javafx.fxml.FXML
    private TableColumn<User, Boolean> columnaVigencia;
    @javafx.fxml.FXML
    private TableView<User> tableView;
    @javafx.fxml.FXML
    private Button btnGuardar;
    @javafx.fxml.FXML
    private TextField textFieldEmail;
    @javafx.fxml.FXML
    private TextField textFieldContrasena;

    private DataUtil dataUtil;
    private User usuarioSeleccionado;

    public void initialize() {
        columnaID.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        columnaContrasena.setCellValueFactory(new PropertyValueFactory<User, String>("clave"));
        columnaVigencia.setCellValueFactory(new PropertyValueFactory<User, Boolean>("vigencia"));

        dataUtil = new DataUtil();
        dataUtil.cargarUsuarios();

        tableView.setItems(FXCollections.observableArrayList(dataUtil.olUsers));

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable,oldValue,newValue)->{
                    usuarioSeleccionado = (User) newValue;
                    if (usuarioSeleccionado != null){
                        textFieldEmail.setText(usuarioSeleccionado.getEmail());
                        textFieldContrasena.setText(usuarioSeleccionado.getClave());
                    } else {
                        textFieldEmail.setText("");
                        textFieldContrasena.setText("");
                    }
                });
    }
}

package ieslosmontecillos.appagendabk;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AgendaViewController implements Initializable {
    private DataUtil dataUtil;
    private ObservableList<Provincia> olProvincias;
    private ObservableList<Persona> olPersonas;
    private Persona personaSeleccionada;

    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private TableColumn<Persona, String> columnNombre;
    @FXML
    private TableColumn<Persona, String> columnProvincia;
    @FXML
    private TableColumn<Persona, String> columnEmail;
    @FXML
    private TableColumn<Persona, String> columnApellidos;
    @FXML
    private TableView<Persona> tableViewAgenda;
    @FXML
    private AnchorPane rootAgendaView;

    public void setDataUtil(DataUtil dataUtil) {
        this.dataUtil = dataUtil;
    }

    public void setOlProvincias(ObservableList<Provincia> olProvincias) {
        this.olProvincias = olProvincias;
    }

    public void setOlPersonas(ObservableList<Persona> olPersonas) {
        this.olPersonas = olPersonas;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new
                PropertyValueFactory<>("apellidos"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnProvincia.setCellValueFactory(
                cellData-> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    if (cellData.getValue().getProvincia() != null) {
                        property.setValue(cellData.getValue().getProvincia().getNombre());
                    }
                    return property;
                });

        tableViewAgenda.getSelectionModel().selectedItemProperty().addListener(
                (observable,oldValue,newValue)->{
                    personaSeleccionada= (Persona) newValue;
                    if (personaSeleccionada != null){
                        textFieldNombre.setText(personaSeleccionada.getNombre());
                        textFieldApellidos.setText(personaSeleccionada.getApellidos());
                    } else {
                        textFieldNombre.setText("");
                        textFieldApellidos.setText("");
                    }
                });
    }

    public void cargarTodasPersonas(){
        tableViewAgenda.setItems(FXCollections.observableArrayList(olPersonas));
    }

    @FXML
    public void onActionButtonGuardar(ActionEvent actionEvent) {
        boolean errorFormato = false;

        // Recoger datos de pantalla
        if (!errorFormato) { // Los datos introducidos son correctos
            try {
                if (personaSeleccionada != null) {
                    personaSeleccionada.setNombre(textFieldNombre.getText());
                    personaSeleccionada.setApellidos(textFieldApellidos.getText());
                    dataUtil.actualizarPersona(personaSeleccionada);
                    int numFilaSeleccionada =
                            tableViewAgenda.getSelectionModel().getSelectedIndex();
                    tableViewAgenda.getItems().set(numFilaSeleccionada, personaSeleccionada);
                    TablePosition pos = new
                            TablePosition(tableViewAgenda, numFilaSeleccionada, null);
                    tableViewAgenda.getFocusModel().focus(pos);
                    tableViewAgenda.requestFocus();
                }
            } catch (Exception ex) { //Los datos introducidos no cumplen requisitos
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("No se han podido guardar los cambios. "
                                + "Compruebe que los datos cumplen los requisitos");
                        alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void ButtonEditar(ActionEvent actionEvent) {
        /*PersonaDetalleViewController personaDetalleViewController =
                (PersonaDetalleViewController) fxmlLoader.getController();
        personaDetalleViewController.setRootAgendaView(rootAgendaView);

        // Para el botón Editar
        personaDetalleViewController.setPersona(personaSeleccionada,false);

        //Intercambio de datos funcionales con el detalle
        personaDetalleViewController.setTableViewPrevio(tableViewAgenda);
        personaDetalleViewController.setDataUtil(dataUtil);*/
    }

    @FXML
    public void ButtonSuprimir(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText("¿Desea suprimir el siguiente registro?");
        alert.setContentText(personaSeleccionada.getNombre() + " "+ personaSeleccionada.getApellidos());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // Acciones a realizar si el usuario acepta
            dataUtil.eliminarPersona(personaSeleccionada);
            tableViewAgenda.getItems().remove(personaSeleccionada);
            tableViewAgenda.getFocusModel().focus(null);
            tableViewAgenda.requestFocus();
        } else {
            // Acciones a realizar si el usuario cancela
            int numFilaSeleccionada=
                    tableViewAgenda.getSelectionModel().getSelectedIndex();
            tableViewAgenda.getItems().set(numFilaSeleccionada,personaSeleccionada);
            TablePosition pos = new TablePosition(tableViewAgenda,
                    numFilaSeleccionada,null);
            tableViewAgenda.getFocusModel().focus(pos);
            tableViewAgenda.requestFocus();
        }
    }

    @FXML
    public void ButtonNuevo(ActionEvent actionEvent) {
        try{
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new
                    FXMLLoader(getClass().getResource("PersonaDetalleView.fxml"));
            Parent rootDetalleView=fxmlLoader.load();

            PersonaDetalleViewController personaDetalleViewController =
                    (PersonaDetalleViewController) fxmlLoader.getController();
            personaDetalleViewController.setRootAgendaView(rootAgendaView);

            // Para el botón Nuevo:
            personaSeleccionada = new Persona();
            personaDetalleViewController.setPersona(personaSeleccionada,true);

            //Intercambio de datos funcionales con el detalle
            personaDetalleViewController.setTableViewPrevio(tableViewAgenda);
            personaDetalleViewController.setDataUtil(dataUtil);

            // Ocultar la vista de la lista
            rootAgendaView.setVisible(false);
            //Añadir la vista detalle al StackPane principal para que se muestre
            StackPane rootMain =
                    (StackPane) rootAgendaView.getScene().getRoot();
            rootMain.getChildren().add(rootDetalleView);
        } catch (IOException ex){
            System.out.println("Error volcado"+ex);}
    }
}



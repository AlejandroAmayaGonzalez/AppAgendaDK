package ieslosmontecillos.appagendabk;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class PersonaDetalleViewController implements Initializable {
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldTelefono;
    @FXML
    private DatePicker datePickerFechaNacimiento;
    @FXML
    private RadioButton radioButtonViudo;
    @FXML
    private TextField textFieldNumHijos;
    @FXML
    private CheckBox checkBoxJubilado;
    @FXML
    private RadioButton radioButtonSoltero;
    @FXML
    private RadioButton radioButtonCasado;
    @FXML
    private TextField textFieldSalario;
    @FXML
    private ImageView imageViewFoto;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private ComboBox<Provincia> comboBoxProvincia;
    @FXML
    private TextField textFieldApellidos;

    private Pane rootAgendaView;
    private TableView tableViewPrevio;
    private Persona persona;
    private DataUtil dataUtil;
    private boolean nuevaPersona;
    @FXML
    private AnchorPane rootPersonaDetalleView;

    public void setDataUtil(DataUtil dataUtil) {
        this.dataUtil = dataUtil;
    }

    public void setTableViewPrevio(TableView tableViewPrevio){
        this.tableViewPrevio=tableViewPrevio;
    }

    public void setPersona(Persona persona, Boolean nuevaPersona){
        if (!nuevaPersona){
            this.persona= persona;
        } else {
            this.persona = new Persona();
        }
        this.nuevaPersona=nuevaPersona;
    }

    public void setRootAgendaView(Pane rootAgendaView){
        this.rootAgendaView = rootAgendaView;
    }

    private final IntegerProperty numHijos = new SimpleIntegerProperty();
    private final DoubleProperty salario = new SimpleDoubleProperty();
    private final IntegerProperty jubilado = new SimpleIntegerProperty();
    public static final String CASADO="CASADO";
    public static final String SOLTERO="SOLTERO";
    public static final String VIUDO="VIUDO";
    public static final String CARPETA_FOTOS="Fotos";

    public void mostrarDatos() throws ParseException {
        textFieldNombre.setText(persona.getNombre());
        textFieldApellidos.setText(persona.getApellidos());
        textFieldTelefono.setText(persona.getTelefono());
        textFieldEmail.setText(persona.getEmail());
        textFieldSalario.setText(String.valueOf(persona.getSalario()));
        textFieldNumHijos.setText(String.valueOf(persona.getNumHijos()));

        if (nuevaPersona){
            dataUtil.addPersona(persona);
        } else {
            dataUtil.actualizarPersona(persona);
        }

        if (persona.getNumHijos() != null){
            textFieldNumHijos.setText(persona.getNumHijos().toString());
        }
        if (persona.getSalario() != null){
            textFieldSalario.setText(persona.getSalario().toString());
        }
        if (persona.getJubilado() != null && persona.getJubilado() == 1) {
            checkBoxJubilado.setSelected(true);
        } else {
            checkBoxJubilado.setSelected(false);
        }

        if (persona.getEstadoCivil() != null){
            switch(persona.getEstadoCivil()){
                case CASADO:
                    radioButtonCasado.setSelected(true);
                    break;
                case SOLTERO:
                    radioButtonSoltero.setSelected(true);
                    break;
                case VIUDO:
                    radioButtonViudo.setSelected(true);
                    break;
            }
        }

        if (persona.getFechaNacimiento() != null){
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fecNac = formato.parse(persona.getFechaNacimiento());
            LocalDate fechaNac =
                    fecNac.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            datePickerFechaNacimiento.setValue(fechaNac);
        }

        comboBoxProvincia.setItems(dataUtil.getOlProvincias());
        if (persona.getProvincia() != null){
            comboBoxProvincia.setValue(persona.getProvincia());
        }

        comboBoxProvincia.setCellFactory(
                (ListView<Provincia> l)-> new ListCell<Provincia>(){
                    @Override
                    protected void updateItem(Provincia provincia, boolean empty){
                        super.updateItem(provincia, empty);
                        if (provincia == null || empty){
                            setText("");
                        } else {
                            setText(provincia.getCodigo()+"-"+provincia.getNombre());
                        }
                    }
                });

        comboBoxProvincia.setConverter(new StringConverter<Provincia>(){
            @Override
            public String toString(Provincia provincia){
                if (provincia == null){
                    return null;
                } else {
                    return provincia.getCodigo()+"-"+provincia.getNombre();
                }
            }
            @Override
            public Provincia fromString(String userId){
                return null;
            }
        });

        if (persona.getFoto() != null){
            String imageFileName=persona.getFoto();
            File file = new File(CARPETA_FOTOS+"/"+imageFileName);
            if (file.exists()){
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } else {
                Alert alert=new Alert(Alert.AlertType.INFORMATION,"No se encuentra la imagen en "+file.toURI().toString());
                        alert.showAndWait();
            }
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    public void ButtonCancelar(ActionEvent actionEvent) {
        StackPane rootMain =
                (StackPane) rootPersonaDetalleView.getScene().getRoot();
        rootMain.getChildren().remove(rootPersonaDetalleView);
        rootAgendaView.setVisible(true);

        int numFilaSeleccionada =
                tableViewPrevio.getSelectionModel().getSelectedIndex();
        TablePosition pos = new TablePosition(tableViewPrevio,
                numFilaSeleccionada,null);
        tableViewPrevio.getFocusModel().focus(pos);
        tableViewPrevio.requestFocus();
    }

    @FXML
    public void ButtonGuardar(ActionEvent actionEvent) {
        boolean errorFormato = false;

        // Recoger datos de pantalla
        if (!errorFormato) { // Los datos introducidos son correctos
            try {
                if (!textFieldNumHijos.getText().isEmpty()){
                    try {
                        persona.setNumHijos(Integer.valueOf(textFieldNumHijos.getText()));
                    } catch(NumberFormatException ex){
                        errorFormato = true;
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Número de hijos no válido");
                                alert.showAndWait();
                        textFieldNumHijos.requestFocus();
                    }
                }

                if (!textFieldSalario.getText().isEmpty()){
                    try {
                        Double dSalario =
                                Double.valueOf(Double.valueOf(textFieldSalario.getText()).doubleValue()
                                );
                        persona.setSalario(dSalario);
                    } catch(NumberFormatException ex) {
                        errorFormato = true;
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Salario no válido");
                                alert.showAndWait();
                        textFieldSalario.requestFocus();
                    }
                }

                if(checkBoxJubilado.isSelected()){
                    Integer jubilado = 1;
                    persona.setJubilado(jubilado);
                };

                if (radioButtonCasado.isSelected()){
                    persona.setEstadoCivil(CASADO);
                } else if (radioButtonSoltero.isSelected()){
                    persona.setEstadoCivil(SOLTERO);
                } else if (radioButtonViudo.isSelected()){
                    persona.setEstadoCivil(VIUDO);
                }

                if (datePickerFechaNacimiento.getValue() != null){
                    LocalDate localDate = datePickerFechaNacimiento.getValue();
                    ZonedDateTime zonedDateTime =
                            localDate.atStartOfDay(ZoneId.systemDefault());
                    Instant instant = zonedDateTime.toInstant();
                    Date date = Date.from(instant);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaComoCadena = sdf.format(date);
                    persona.setFechaNacimiento(fechaComoCadena);
                } else {
                    persona.setFechaNacimiento(null);
                }

                if (comboBoxProvincia.getValue() != null){
                    persona.setProvincia(comboBoxProvincia.getValue());
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,"Debe indicar una provincia");
                            alert.showAndWait();
                    errorFormato = true;
                }

            } catch (Exception ex) { //Los datos introducidos no cumplen requisitos
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("No se han podido guardar los cambios. " +
                        "Compruebe que los datos cumplen los requisitos");
                        alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            }
        }

    }

    @FXML
    public void ButtonExaminar(ActionEvent actionEvent) {
        File carpetaFotos = new File(CARPETA_FOTOS);
        if (!carpetaFotos.exists()){
            carpetaFotos.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (jpg, png)", "*.jpg",
                        "*.png"),
                new FileChooser.ExtensionFilter("Todos los archivos","*.*"));
        File file = fileChooser.showOpenDialog(
                rootPersonaDetalleView.getScene().getWindow());
        if (file != null){
            try {Files.copy(file.toPath(),new File(CARPETA_FOTOS+
                    "/"+file.getName()).toPath());
                persona.setFoto(file.getName());
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } catch (FileAlreadyExistsException ex){
                Alert alert = new Alert(Alert.AlertType.WARNING,"Nombre de archivo duplicado");
                alert.showAndWait();
            } catch (IOException ex){
                Alert alert = new Alert(Alert.AlertType.WARNING,"No se ha podido guardar la imagen");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void ButtonSuprimirFoto(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar supresión de imagen");
        alert.setHeaderText("¿Desea SUPRIMIR el archivo asociado a la imagen,\n"+
                "quitar la foto pero MANTENER el archivo, \no CANCELAR la operación?");
        alert.setContentText("Elija la opción deseada:");
        ButtonType buttonTypeEliminar = new ButtonType("Suprimir");
        ButtonType buttonTypeMantener = new ButtonType("Mantener");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar",
                ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeEliminar, buttonTypeMantener,
                buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeEliminar){
            String imageFileName = persona.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists()) {
                file.delete();
            }
            persona.setFoto(null);
            imageViewFoto.setImage(null);
        } else if (result.get() == buttonTypeMantener) {
            persona.setFoto(null);
            imageViewFoto.setImage(null);
        }
    }

    public ComboBox getComboBoxProvincia() {
        return comboBoxProvincia;
    }

    public void setComboBoxProvincia(ComboBox comboBoxProvincia) {
        this.comboBoxProvincia = comboBoxProvincia;
    }

    public TextField getTextFieldApellidos() {
        return textFieldApellidos;
    }

    public void setTextFieldApellidos(TextField textFieldApellidos) {
        this.textFieldApellidos = textFieldApellidos;
    }

    public TextField getTextFieldSalario() {
        return textFieldSalario;
    }

    public void setTextFieldSalario(TextField textFieldSalario) {
        this.textFieldSalario = textFieldSalario;
    }

    public ImageView getImageViewFoto() {
        return imageViewFoto;
    }

    public void setImageViewFoto(ImageView imageViewFoto) {
        this.imageViewFoto = imageViewFoto;
    }

    public TextField getTextFieldEmail() {
        return textFieldEmail;
    }

    public void setTextFieldEmail(TextField textFieldEmail) {
        this.textFieldEmail = textFieldEmail;
    }

    public RadioButton getRadioButtonCasado() {
        return radioButtonCasado;
    }

    public void setRadioButtonCasado(RadioButton radioButtonCasado) {
        this.radioButtonCasado = radioButtonCasado;
    }

    public RadioButton getRadioButtonSoltero() {
        return radioButtonSoltero;
    }

    public void setRadioButtonSoltero(RadioButton radioButtonSoltero) {
        this.radioButtonSoltero = radioButtonSoltero;
    }

    public CheckBox getCheckBoxJubilado() {
        return checkBoxJubilado;
    }

    public void setCheckBoxJubilado(CheckBox checkBoxJubilado) {
        this.checkBoxJubilado = checkBoxJubilado;
    }

    public TextField getTextFieldNumHijos() {
        return textFieldNumHijos;
    }

    public void setTextFieldNumHijos(TextField textFieldNumHijos) {
        this.textFieldNumHijos = textFieldNumHijos;
    }

    public RadioButton getRadioButtonViudo() {
        return radioButtonViudo;
    }

    public void setRadioButtonViudo(RadioButton radioButtonViudo) {
        this.radioButtonViudo = radioButtonViudo;
    }

    public DatePicker getDatePickerFechaNacimiento() {
        return datePickerFechaNacimiento;
    }

    public void setDatePickerFechaNacimiento(DatePicker datePickerFechaNacimiento) {
        this.datePickerFechaNacimiento = datePickerFechaNacimiento;
    }

    public TextField getTextFieldTelefono() {
        return textFieldTelefono;
    }

    public void setTextFieldTelefono(TextField textFieldTelefono) {
        this.textFieldTelefono = textFieldTelefono;
    }

    public TextField getTextFieldNombre() {
        return textFieldNombre;
    }

    public void setTextFieldNombre(TextField textFieldNombre) {
        this.textFieldNombre = textFieldNombre;
    }
}

package ieslosmontecillos.appagendabk;


import java.io.IOException;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

public class InicioView {
    private InicioController inicioController;

    public View getView() {
        try {
            FXMLLoader loader = new
                    FXMLLoader(InicioView.class.getResource("Inicio.fxml"));
            View view = loader.load(InicioView.class.getResourceAsStream("Inicio.fxml"));
            inicioController = loader.getController();
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
    public InicioController getInicioController() {
        return inicioController;
    }
}


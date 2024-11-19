module ieslosmontecillos.appagendabk {
    requires javafx.fxml;
    requires com.gluonhq.connect;
    requires java.json;
    requires java.xml.bind;
    requires java.desktop;
    requires com.gluonhq.charm.glisten;


    opens ieslosmontecillos.appagendabk to javafx.fxml;
    exports ieslosmontecillos.appagendabk;
}
package ieslosmontecillos.appagendabk;

import javafx.beans.property.*;

import javax.xml.bind.annotation.XmlElement;

public class User {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty clave = new SimpleStringProperty();
    private final BooleanProperty vigencia = new SimpleBooleanProperty();

    @XmlElement(name = "id")
    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    @XmlElement(name = "email")
    public String getEmail() { return email.get(); }
    public StringProperty emailProperty() { return email; }
    public void setEmail(String email) { this.email.set(email); }

    @XmlElement(name = "clave")
    public String getClave() { return clave.get(); }
    public StringProperty claveProperty() { return clave; }
    public void setClave(String clave) { this.clave.set(clave); }

    @XmlElement(name = "vigencia")
    public boolean getVigencia() { return vigencia.get(); }
    public BooleanProperty vigenciaProperty() { return vigencia; }
    public void setVigencia(boolean vigencia) { this.vigencia.set(vigencia); }
}

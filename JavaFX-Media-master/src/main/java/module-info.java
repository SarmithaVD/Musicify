module com.kensoftph.javafxmedia {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;

    opens com.kensoftph.javafxmedia to javafx.fxml;
    exports com.kensoftph.javafxmedia;
}
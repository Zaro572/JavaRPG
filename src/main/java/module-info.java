module com.example.javafx_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires annotations;

    opens assets.textures;
    opens assets.sounds;
    opens assets.music;
    opens com.example.javafx_project to javafx.fxml;
    exports com.example.javafx_project;
}
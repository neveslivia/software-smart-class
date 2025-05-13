module org.example.smart {
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
    requires java.sql;

    opens org.example.smart to javafx.fxml;
    exports org.example.smart;

    // Controllers
    exports org.example.smart.Controllers;
    opens org.example.smart.Controllers to javafx.fxml;

    exports org.example.smart.Controllers.Menu;
    opens org.example.smart.Controllers.Menu to javafx.fxml; // <-- ADICIONADO

    exports org.example.smart.Controllers.Remover;
    opens org.example.smart.Controllers.Remover to javafx.fxml;

    exports org.example.smart.Controllers.Cadastro;
    opens org.example.smart.Controllers.Cadastro to javafx.fxml;

    exports org.example.smart.Controllers.Transicao;
    opens org.example.smart.Controllers.Transicao to javafx.fxml;

    opens org.example.smart.Controllers.Atualizar to javafx.fxml; // já estava ok
}

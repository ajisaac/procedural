module co.aisaac.procedural {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;

	opens co.aisaac.procedural to javafx.fxml;
	exports co.aisaac.procedural;
}

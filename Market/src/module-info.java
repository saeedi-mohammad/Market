module Market {
	requires java.sql;
	requires org.apache.poi.poi;
	requires org.apache.poi.ooxml;
	requires javafx.controls;
	requires javafx.base;
	requires javafx.graphics;
	requires javafx.fxml;

	opens application;
	opens Calendar;
	opens Commons;
	opens Controllers;
	opens Dao;
	opens images;
	opens View;

}
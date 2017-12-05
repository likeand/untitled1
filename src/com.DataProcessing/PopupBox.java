package com.DataProcessing;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;

public class PopupBox
{
	static Stage window;

	static public void downloadPathnameAsker()
	{
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Pathname");
		window.setMinWidth(700);
		window.setMinHeight(500);

		Label l = new Label("Where do you want to put the file?");
		Label c = new Label("Caution: it has to be absolute pathname\n and not root directory.");
		TextField pInput = new TextField();
		pInput.setPromptText("absolute pathname");
		pInput.setMinWidth(300);
		pInput.setMaxWidth(300);
		Button confirm = new Button("Confirm");
		Button cancel = new Button("Cancel");
		Button open = new Button("Open");
		HBox hb = new HBox();
		hb.getChildren().addAll(cancel, confirm);
		hb.setSpacing(30);
		hb.setAlignment(Pos.CENTER);
		HBox hb1 = new HBox();
		hb1.getChildren().addAll(pInput, open);
		hb1.setSpacing(15);
		hb1.setAlignment(Pos.CENTER);
		VBox vb = new VBox();
		vb.getChildren().addAll(l, c, hb1, hb);
		vb.setSpacing(20);
		vb.setAlignment(Pos.CENTER);
		Scene scene = new Scene(vb);
		scene.getStylesheets().addAll("dms.css");
		window.setScene(scene);
		window.show();

		cancel.setOnAction(e -> {
			window.close();
		});
		confirm.setOnAction(e -> {
			window.close();
			gui.the_following_d(pInput.getText());
		});
		open.setOnAction(e -> {
			File from = gui.chooseDirectory("Where to put file.");
			pInput.setText(from.getAbsolutePath());
		});
	}
	static public void uploadPathnameAsker(User u)
	{
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Pathname");
		window.setMinWidth(700);
		window.setMaxWidth(700);
		window.setMinHeight(500);
		window.setMaxHeight(500);

		Label l = new Label("Where is the file to upload?");
		Label c = new Label("Caution: it has to be absolute pathname.");
		Label d = new Label("Your description:");
		TextField pInput = new TextField();
		pInput.setPromptText("absolute pathname");
		pInput.setMinWidth(300);
		pInput.setMaxWidth(300);
		TextField dInput = new TextField();
		dInput.setPromptText("your description");
		dInput.setMinWidth(300);
		dInput.setMaxWidth(300);


		Button confirm = new Button("Confirm");
		Button cancel = new Button("Cancel");
		Button open = new Button("Open");
		HBox hb = new HBox();
		hb.getChildren().addAll(cancel, confirm);
		hb.setSpacing(30);
		hb.setAlignment(Pos.CENTER);
		HBox hb1 = new HBox();
		hb1.getChildren().addAll(pInput, open);
		hb1.setSpacing(15);
		hb1.setAlignment(Pos.CENTER);
		VBox vb = new VBox();
		vb.getChildren().addAll(l, c, hb1, d, dInput, hb);
		vb.setSpacing(20);
		vb.setAlignment(Pos.CENTER);
		Scene scene = new Scene(vb);
		scene.getStylesheets().addAll("dms.css");
		window.setScene(scene);
		window.show();

		cancel.setOnAction(e -> {
			window.close();
		});
		confirm.setOnAction(e -> {
			window.close();
			gui.the_following_u(pInput.getText(), dInput.getText(), u);
			//gui.the_following_u(pInput.getText(), dInput.getText(), u);
		});
		open.setOnAction(e -> {
			File from = gui.chooseFile("Where is the file to upload.");
			if(from != null)
				pInput.setText(from.getAbsolutePath());
		});
	}
}

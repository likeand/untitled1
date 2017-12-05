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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import javax.xml.soap.Text;

public class gui extends Application
{
	static boolean nameErrorMsg_show_1;
	static boolean nameErrorMsg_show;
	static boolean passwordErrorMsg_show;
	static Stage window;
	static Label title;
	static GridPane gp, gp1;
	static TextField nameInput;
	static TextField passwordInput;
	static Label passwordErrorMsg;
	static Label nameErrorMsg;
	static Label nameErrorMsgForSignin;
	static HBox hb1;//for buttons
	static BorderPane bp;
	static VBox vb;
	static HBox hb;
	static Button login;
	static Button signin;
	static Button exit;
	static Label name;
	static Label password;
	static Scene scene;
	static TableView<User> tableView;
	static TableView<Documentary> fileTableView;
	public static void main(String[] args)
	{
		launch(args);
	}
	public void start(Stage primaryStage) throws Exception
	{
		/////some initialization of GUI
		window = primaryStage;
		window.setTitle("Document Management System");
		window.setHeight(400);
		window.setWidth(800);

		DataProcessing.insert("admin", "asdf", 1);
		try
		{
			DataProcessing.Initialize();
		}catch(FileNotFoundException e)
		{
			System.out.println(e.toString());
		}
		showTouristMenu();
	}

	static public void showTouristMenu()
	{

		title = new Label("Tourist's Menu");

		login = new Button("Log In");
		signin = new Button("Sign In");
		exit = new Button("Exit System");
		int typeCounter = 0;

		name = new Label("Name:");
		password = new Label ("Password:");
		if(nameInput == null)
			nameInput = new TextField();
		if(passwordInput == null)
			passwordInput = new TextField();
		nameInput.clear();
		passwordInput.clear();
		nameInput.setPromptText("Input your name");
		nameInput.setMinWidth(220);
		passwordInput.setMinWidth(220);
		passwordInput.setPromptText("Input your password");

		gp = new GridPane();
		gp.setHgap(15);
		gp.setVgap(15);
		gp.setPadding(new Insets(0, 10, 0, 10));

		gp.add(name, 0, 0);
		gp.add(password, 0, 1);
		gp.add(nameInput, 1, 0);
		gp.add(passwordInput, 1, 1);
		gp.setAlignment(Pos.CENTER);
		if(nameErrorMsg_show)
			gp.add(nameErrorMsg, 2,0);
		if(passwordErrorMsg_show)
			gp.add(passwordErrorMsg, 2, 1);



		//Hb1 for buttons
		hb1 = new HBox();
		hb1.setPadding(new Insets(13));
		hb1.setSpacing(6);
		hb1.getChildren().addAll(login, signin, exit);
		hb1.setAlignment(Pos.CENTER);
		hb1.setSpacing(15);
		//vb for middle line layout
		vb = new VBox();
		vb.getChildren().addAll(gp,hb1);
		vb.setAlignment(Pos.CENTER);
		//hb for title
		hb = new HBox();
		hb.getChildren().add(title);
		hb.setAlignment(Pos.CENTER);
		bp = new BorderPane();
		bp.setTop(hb);
		bp.setCenter(vb);
		scene = new Scene(bp);
		scene.getStylesheets().add("dms.css");
		title.setStyle("-fx-font-size: 25pt;");
		window.setScene(scene);
		window.show();


		login.setOnAction(e -> {
			window.close();
			gp.getChildren().removeAll(nameErrorMsg, passwordErrorMsg);
			nameErrorMsg_show = false;
			passwordErrorMsg_show = false;
			LogIn(nameInput.getText(), passwordInput.getText());

			nameInput.clear();
			passwordInput.clear();
		});
		signin.setOnAction(e -> {
			window.close();
			nameInput.clear();
			passwordInput.clear();
			nameErrorMsg_show = false;
			passwordErrorMsg_show = false;
			nameErrorMsg_show_1 = false;

			SignIn();
		});
		exit.setOnAction(e -> {window.close(); User.exitSystem();});
	}
	static public void nameError(String msg)
{
	nameErrorMsg = new Label(msg);
	nameErrorMsg.setStyle("-fx-text-fill: red;");
	nameErrorMsg_show = true;
}
	static public void passwordError(String msg)
	{
		passwordErrorMsg = new Label(msg);
		passwordErrorMsg.setStyle("-fx-text-fill: red;");
		passwordErrorMsg_show = true;
	}

	static public void nameErrorForSignin(String msg)
	{
		nameErrorMsgForSignin = new Label(msg);
		nameErrorMsgForSignin.setStyle("-fx-text-fill: red;");
		nameErrorMsg_show_1 = true;
	}




	static public void LogIn(String name, String password)
	{
		//System.out.println("in login " + DataProcessing.searchUser(name));
		if(!DataProcessing.searchUser(name))
		{
			nameError("Username not found");
			showTouristMenu();
		}
		else
		{
			User u = DataProcessing.search(name);
			if(u.getPassword().equals(password))
			{
				u.showMenu();
				u.setLoggedIn(true);
			}
			else
			{
				passwordError("Wrong Password");
				showTouristMenu();
			}
		}
	}
	static public void SignIn()
	{
		hb.getChildren().removeAll(title);
		title = new Label("Sign-in Procedure");
		title.setStyle("-fx-font-size: 25pt;");
		hb.getChildren().add(title);
		nameInput = new TextField();
		nameInput.setMinWidth(200);
		passwordInput = new TextField();
		passwordInput.setMinWidth(200);
		nameInput.setPromptText("set your name");
		passwordInput.setPromptText("set your password");
		passwordInput.textProperty().addListener( (v, o, n) -> passwordValidationTestor(n));

		ToggleGroup identity = new ToggleGroup();
		ToggleButton bButton = new ToggleButton("Browser");
		ToggleButton oButton = new ToggleButton("Operator");
		bButton.setToggleGroup(identity);
		oButton.setToggleGroup(identity);
		oButton.setSelected(true);
		oButton.setMinSize(150,25);
		bButton.setMinSize(150,25);
		bButton.setUserData("Browser");
		oButton.setUserData("Operator");
		bButton.setSelected(true);
		//System.out.println(oButton.getText());

		VBox vb_for_identity = new VBox();
		vb_for_identity.getChildren().addAll(bButton, oButton);
		vb_for_identity.setSpacing(12);

		Label ask = new Label("Type of account:");

		Button back = new Button("<Back");


		VBox vb = new VBox();
		HBox hb_buttons = new HBox();
		hb_buttons.getChildren().addAll(back, signin, exit);
		hb_buttons.setSpacing(15);
		hb_buttons.setAlignment(Pos.CENTER);

		HBox hb_for_identity = new HBox();
		hb_for_identity.getChildren().addAll(ask, vb_for_identity);
		hb_for_identity.setAlignment(Pos.CENTER);
		hb_for_identity.setSpacing(15);


		gp1 = new GridPane();
		gp1.setHgap(15);
		gp1.setVgap(15);
		gp1.setPadding(new Insets(0, 10, 0, 10));

		gp1.add(name, 0, 0);
		gp1.add(password, 0, 1);
		gp1.add(nameInput, 1, 0);
		gp1.add(passwordInput, 1, 1);
		gp1.setAlignment(Pos.CENTER);
		if(nameErrorMsg_show_1)
			gp1.add(nameErrorMsgForSignin, 2, 0);



		vb.getChildren().addAll(gp1, hb_for_identity, hb_buttons);
		vb.setAlignment(Pos.CENTER);
		vb.setSpacing(15);

		BorderPane bp = new BorderPane();
		bp.setTop(hb);
		bp.setCenter(vb);
		bp.setPadding(new Insets(0, 10, 0, 10));


		Scene local_scene = new Scene(bp);
		window.setScene(local_scene);
		local_scene.getStylesheets().add("dms.css");
		window.show();

		back.setOnAction(e -> {
			window.close();
			showTouristMenu();
		});
		signin.setOnAction(e -> {
			if(identity.getSelectedToggle() == null)
			{
				Alert _alert = new Alert(Alert.AlertType.WARNING,"You didn't choose any type.", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
						new ButtonType("OK", ButtonBar.ButtonData.YES));
				_alert.showAndWait();
				SignIn();
			}
			else
				signinClicked(nameInput.getText(), passwordInput.getText(), identity.getSelectedToggle());
		});


	}
	static void signinClicked(String name, String password, Toggle t)
	{
		if(name.equals(""))
			nameErrorForSignin("Empty input");
		else if (DataProcessing.searchUser(name))
		{
			nameErrorForSignin("Occupied, re-input");
		}
		else
		{
			if(t.getUserData().equals("Browser"))
				DataProcessing.insert(name, password, 3);
			else if(t.getUserData().equals("Operator"))
				DataProcessing.insert(name, password, 2);
			window.close();
			DataProcessing.saveData();
			DataProcessing.search(name).showMenu();
			return;
		}
		SignIn();
	}
	static boolean passwordValidationTestor(String n)
	{//n.matches("[a-z[0-9][A-Z]]{6,}")&& && n.matches("[0-9]+")
		if(n.matches("[a-zA-Z0-9]*[a-z]+[a-zA-Z0-9]*") && n.matches("[a-zA-Z0-9]*[0-9]+[a-zA-Z0-9]*") && n.length() >= 6)
		{
			passwordHint("good choice", "green");
			return true;
		}
		else if(n.length() == 0)
		{
			passwordHint("", "blue");
			 return false;
		}
		else
		{
			passwordHint(" too simple", "blue");
			return false;
		}
	}
	static void passwordHint(String msg, String color)
	{
		gp1.getChildren().removeAll(passwordErrorMsg);
		passwordErrorMsg = new Label(msg);
		passwordErrorMsg.setStyle("-fx-text-fill: "+ color +";");
		gp1.add(passwordErrorMsg, 2, 1);

	}
	static void showAdministratorMenu(User ad)
	{
		title = new Label("Administrator's Menu");
		//title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		//title.setLabelFor();
		window.setHeight(500);

		exit = new Button("Exit System");
		Button change_user_info = new Button("Change a User's Information");
		Button del_user = new Button("Delete User");
		Button add_user = new Button("Add a User");
		Button list_users = new Button("List All Users");
		Button change_password = new Button("Change Your Password");
		Button log_out = new Button("Log Out");

		exit.setMinWidth(300);
		change_password.setMinWidth(300);
		change_user_info.setMinWidth(300);
		del_user.setMinWidth(300);
		add_user.setMinWidth(300);
		list_users.setMinWidth(300);
		log_out.setMinWidth(300);

		//vb for middle line layout
		VBox vb1 = new VBox();
		vb1.getChildren().addAll(add_user, del_user, change_user_info, list_users, change_password, log_out, exit);
		vb1.setAlignment(Pos.CENTER);
		vb1.setSpacing(15);
		vb1.setAlignment(Pos.CENTER);
		//hb for title
		hb = new HBox();
		hb.getChildren().add(title);
		hb.setAlignment(Pos.CENTER);
		bp = new BorderPane();
		bp.setTop(hb);
		bp.setCenter(vb1);
		scene = new Scene(bp);
		scene.getStylesheets().add("dms.css");
		title.setStyle("-fx-font-size: 25pt;");
		window.setScene(scene);
		window.show();

		add_user.setOnAction(e -> {
			window.close();
			add_userClicked(ad);
		});
		del_user.setOnAction(e -> {
			window.close();
			del_userClicked(ad);
		});
		change_user_info.setOnAction(e -> {
			window.close();
			change_user_infoClicked(ad);
		});
		list_users.setOnAction(e -> {
			window.close();
			list_usersClicked(ad);
		});
		change_password.setOnAction(e -> {
			window.close();
			change_passwordClicked(ad);
		});
		log_out.setOnAction(e -> {
			window.close();
			showTouristMenu();
			ad.setLoggedIn(false);
		});
		exit.setOnAction(e -> {window.close(); User.exitSystem();});
	}
	static public void add_userClicked(User ad)
	{
		nameInput.clear();
		passwordInput.clear();

		hb.getChildren().removeAll(title);
		title = new Label("Add a user");
		title.setAlignment(Pos.TOP_LEFT);
		title.setStyle("-fx-font-size: 18pt;");
		hb.getChildren().add(title);

		nameInput.setPromptText("set user's name");
		passwordInput.setPromptText("set user's password");
		//passwordInput.textProperty().addListener( (v, o, n) -> passwordValidationTestor(n));

		Button back = new Button("<Back");

		ToggleGroup identity = new ToggleGroup();
		ToggleButton bButton = new ToggleButton("Browser");
		ToggleButton oButton = new ToggleButton("Operator");
		ToggleButton aButton = new ToggleButton("Administrator");
		bButton.setToggleGroup(identity);
		oButton.setToggleGroup(identity);
		aButton.setToggleGroup(identity);
		oButton.setSelected(true);
		oButton.setMinSize(150,25);
		bButton.setMinSize(150,25);
		aButton.setMinSize(150,25);
		bButton.setUserData("Browser");
		oButton.setUserData("Operator");
		aButton.setUserData("Administrator");
		//System.out.println(oButton.getText());

		VBox vb_for_identity = new VBox();
		vb_for_identity.getChildren().addAll(bButton, oButton, aButton);
		vb_for_identity.setSpacing(12);

		Label ask = new Label("Add as:");
		Button add = new Button("Add");
		add.setMinWidth(50);


		HBox hb_buttons = new HBox();
		hb_buttons.getChildren().addAll(back, add, exit);
		exit.setMinWidth(50);
		hb_buttons.setAlignment(Pos.CENTER);
		hb_buttons.setSpacing(15);

		HBox hb_for_identity = new HBox();
		hb_for_identity.getChildren().addAll(ask, vb_for_identity);
		hb_for_identity.setAlignment(Pos.CENTER);
		hb_for_identity.setSpacing(15);

		GridPane gp = new GridPane();
		gp.setHgap(15);
		gp.setVgap(15);
		gp.setPadding(new Insets(0, 10, 0, 10));

		gp.add(name,0,0);
		gp.add(password,0,1);
		gp.add(nameInput,1,0);
		gp.add(passwordInput,1,1);
		gp.setAlignment(Pos.CENTER);


		VBox vb_middle_line = new VBox();
		vb_middle_line.getChildren().addAll(gp, hb_for_identity, hb_buttons);
		vb_middle_line.setAlignment(Pos.CENTER);
		vb_middle_line.setSpacing(15);
		BorderPane local_bp = new BorderPane();
		local_bp.setTop(hb);
		local_bp.setCenter(vb_middle_line);
		local_bp.setPadding(new Insets(10, 10, 10, 10));
		Scene local_scene = new Scene(local_bp);
		window.setScene(local_scene);
		local_scene.getStylesheets().add("dms.css");
		window.setHeight(500);
		window.show();


		back.setOnAction(e -> {
			window.close();
			showAdministratorMenu(ad);
		});

		add.setOnAction(e -> {
			Alert _alert;
			if(DataProcessing.searchUser(nameInput.getText()))
			{
				_alert = new Alert(Alert.AlertType.ERROR,"Error, Name already exists.",new ButtonType("Cancel", ButtonBar.ButtonData.NO),
						new ButtonType("OK", ButtonBar.ButtonData.YES));

			}
			else
			{
				if (identity.getSelectedToggle().getUserData().equals("Administrator"))
					DataProcessing.insert(nameInput.getText(), passwordInput.getText(), 1);
				else if (identity.getSelectedToggle().getUserData().equals("Operator"))
					DataProcessing.insert(nameInput.getText(), passwordInput.getText(), 2);
				else if (identity.getSelectedToggle().getUserData().equals("Browser"))
					DataProcessing.insert(nameInput.getText(), passwordInput.getText(), 3);
				_alert = new Alert(Alert.AlertType.CONFIRMATION, "Addition Complete.", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
						new ButtonType("OK", ButtonBar.ButtonData.YES));
				DataProcessing.saveData();
			}
			_alert.showAndWait();

		});

	}

	static public void del_userClicked(User ad)
	{
		HBox hb_search_line = new HBox();
		HBox hb_bot_line = new HBox();
		Label regex = new Label("RegEx:");
		regex.setStyle("-fx-font-size: 17pt; -fx-font-weight: BOLD;");
		nameInput.clear();
		nameInput.setPromptText("search across names");
		Button search = new Button("Search");
		Button delete = new Button("Delete");
		Button back = new Button("<Back");

		search.setMinSize(70,30);
		hb_search_line.getChildren().addAll(regex, nameInput, search);
		hb_search_line.setAlignment(Pos.CENTER);
		hb_search_line.setSpacing(15);

		hb_bot_line.getChildren().addAll(back, delete);
		hb_bot_line.setSpacing(15);
		hb_bot_line.setAlignment(Pos.BASELINE_RIGHT);

		userTableViewInitialization();
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		VBox local_vb = new VBox();
		local_vb.setSpacing(15);
		local_vb.setPadding(new Insets(10, 10, 10, 10));
		local_vb.getChildren().addAll(hb_search_line, tableView, hb_bot_line);
		Scene scene = new Scene(local_vb);
		scene.getStylesheets().addAll("dms.css");
		window.setScene(scene);
		window.show();


		back.setOnAction(e -> {
			window.close();
			showAdministratorMenu(ad);
		});

		delete.setOnAction(e -> {
			ObservableList<User> user_selected, all_user;
			all_user = tableView.getItems();
			user_selected = tableView.getSelectionModel().getSelectedItems();
			if(user_selected.isEmpty())
				;
			else
			{
				user_selected.forEach(u ->
				{
					if(u.getRole() == 1)
					{

						Alert _alert = new Alert(Alert.AlertType.ERROR,"You can't delete an Administrator.", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
								new ButtonType("OK", ButtonBar.ButtonData.YES));
						_alert.showAndWait();
					}
					else
					{
						all_user.remove(u);
						DataProcessing.delete(u.getName());
					}
				});
				DataProcessing.saveData();
			}
		});

		search.setOnAction(e -> {
			tableView.setItems(AllUsers(nameInput.getText()));
		});
		//tableView = null;
	}

	static public void change_user_infoClicked(User ad)
	{
		HBox hb_search_line = new HBox();
		HBox hb_bot_line = new HBox();
		Label regex = new Label("RegEx:");
		regex.setStyle("-fx-font-size: 17pt; -fx-font-weight: BOLD;");
		nameInput.clear();
		passwordInput.clear();
		passwordInput.setPromptText("input password");
		nameInput.setPromptText("search across names");
		TextField roleInput = new TextField();
		roleInput.setPromptText("role: 1, 2 or 3");
		roleInput.setMinWidth(200);
		Label role = new Label("Role:");
		Button search = new Button("Search");
		Button change = new Button("Change");
		Button back = new Button("<Back");

		search.setMinSize(70,30);
		hb_search_line.getChildren().addAll(regex, nameInput, search);
		hb_search_line.setAlignment(Pos.CENTER);
		hb_search_line.setSpacing(15);

		hb_bot_line.getChildren().addAll(password, passwordInput, role, roleInput, change, back);
		hb_bot_line.setSpacing(15);
		hb_bot_line.setAlignment(Pos.BASELINE_RIGHT);

		tableView = new TableView<>();
		tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		userTableViewInitialization();

		VBox local_vb = new VBox();
		local_vb.setSpacing(15);
		local_vb.setPadding(new Insets(10, 10, 10, 10));
		local_vb.getChildren().addAll(hb_search_line, tableView, hb_bot_line);
		Scene scene = new Scene(local_vb);
		scene.getStylesheets().addAll("dms.css");
		window.setScene(scene);
		window.show();


		back.setOnAction(e -> {
			window.close();
			showAdministratorMenu(ad);
		});

		change.setOnAction(e -> {
			ObservableList<User> user_selected, all_user;
			all_user = tableView.getItems();
			user_selected = tableView.getSelectionModel().getSelectedItems();
			if(user_selected.isEmpty())
				;
			else
			{
				user_selected.forEach(u ->
				{
					Alert _alert;
					u.setPassword(passwordInput.getText());
					try{
						if(Integer.parseInt(roleInput.getText()) >= 1 && Integer.parseInt(roleInput.getText()) <= 3)
						{
							all_user.remove(u);
							u.setRole(Integer.parseInt(roleInput.getText()));
							DataProcessing.update(u.getName(), u.getPassword(), u.getRole());
							u = DataProcessing.search(u.getName());
							DataProcessing.saveData();
							all_user.add(u);
						}
						else
						{
							_alert = new Alert(Alert.AlertType.ERROR,"Error, Not a valid role.",new ButtonType("Cancel", ButtonBar.ButtonData.NO),
									new ButtonType("OK", ButtonBar.ButtonData.YES));
							_alert.showAndWait();
						}
					}catch(NumberFormatException ex)
					{
						_alert = new Alert(Alert.AlertType.ERROR,"Error, Not a valid role.",new ButtonType("Cancel", ButtonBar.ButtonData.NO),
								new ButtonType("OK", ButtonBar.ButtonData.YES));
						_alert.showAndWait();
					}

				});
			}
			window.show();
		});

		search.setOnAction(e -> {
			tableView.setItems(AllUsers(nameInput.getText()));
		});

	}

	static public void list_usersClicked(User ad)
	{
		Button back = new Button("<Back");

		tableView = new TableView<>();

		userTableViewInitialization();

		tableView.setItems(AllUsers(".*"));


		HBox hb = new HBox();
		hb.getChildren().addAll(back);
		back.setAlignment(Pos.BOTTOM_RIGHT);
		hb.setAlignment(Pos.BOTTOM_RIGHT);
		VBox vb = new VBox();
		vb.setSpacing(15);
		vb.getChildren().addAll(tableView, hb);
		vb.setPadding(new Insets(0, 10, 0, 10));
		Scene scene = new Scene(vb);
		scene.getStylesheets().addAll("dms.css");
		window.setScene(scene);
		window.show();


		back.setOnAction(e -> {
			window.close();
			showAdministratorMenu(ad);
		});

		//
		// tableView = null;
	}

	static public void change_passwordClicked(User ad)
	{
		Label oldl = new Label("Your old password:");
		Label newl = new Label("Your new password:");
		TextField oldp = new TextField();
		TextField passwordInput = new TextField();
		oldp.setMinWidth(250);
		passwordInput.setMinWidth(250);
		passwordInput.textProperty().addListener( (v, o, n) -> passwordValidationTestor(n));
		Button confirm = new Button("Confirm");
		Button back = new Button("<Back");

		HBox hb_buttons = new HBox();
		hb_buttons.getChildren().addAll(back, confirm);
		hb_buttons.setAlignment(Pos.CENTER);
		hb_buttons.setSpacing(25);

		GridPane gp = new GridPane();
		gp.add(oldl,0,0);
		gp.add(newl,0,1);
		gp.add(oldp,1,0);
		gp.add(passwordInput,1,1);
		gp.setVgap(20);
		gp.setHgap(15);
		gp.setAlignment(Pos.CENTER);

		VBox vb = new VBox();
		vb.getChildren().addAll(gp, hb_buttons);
		vb.setSpacing(30);
		vb.setAlignment(Pos.CENTER);

		Scene scene = new Scene(vb);
		scene.getStylesheets().add("dms.css");
		window.setScene(scene);
		window.show();


		confirm.setOnAction(e -> {
			Alert _alert;
			if(oldp.getText().equals(ad.getPassword()))
			{

				if (ad.getPassword().equals(passwordInput.getText()))
				{
					_alert = new Alert(Alert.AlertType.WARNING, "Same as old.", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
							new ButtonType("OK", ButtonBar.ButtonData.YES));
					_alert.showAndWait();
				}
				else
				{
					ad.setPassword(passwordInput.getText());
					_alert = new Alert(Alert.AlertType.CONFIRMATION, "Change saved.", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
							new ButtonType("OK", ButtonBar.ButtonData.YES));
					_alert.showAndWait();
					ad.showMenu();
				}
			}
			else
			{
				_alert = new Alert(Alert.AlertType.ERROR, "Wrong password", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
						new ButtonType("OK", ButtonBar.ButtonData.YES));
				_alert.showAndWait();
			}
		});
		back.setOnAction(e -> {
			window.close();
			ad.showMenu();
		});
	}
	static public ObservableList<User> AllUsers(String regex)
	{
		User u;
		ObservableList<User> users = FXCollections.observableArrayList();
		Enumeration<User> e = DataProcessing.getAllUser();
		while(e.hasMoreElements())
		{
			u = e.nextElement();
			if(u.getName().matches(regex))
				users.add(u);
		}
		return users;
	}
	static public void userTableViewInitialization()
	{
		tableView = new TableView<>();

		TableColumn<User, String> nameColumn = new TableColumn<>("Username");
		TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
		TableColumn<User, String> identityColumn = new TableColumn<>("Identity");
		TableColumn<User, String> stateColumn = new TableColumn<>("Log-in state");
		nameColumn.setMinWidth(100);
		passwordColumn.setMinWidth(200);
		identityColumn.setMinWidth(100);
		stateColumn.setMinWidth(200);

		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
		identityColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
		stateColumn.setCellValueFactory(new PropertyValueFactory<>("loggedIn"));

		tableView.getColumns().addAll(nameColumn, passwordColumn, identityColumn, stateColumn);
	}
	static public void showBrowserMenu(User br)
	{
		title = new Label("Browser's Menu");
		window.setHeight(500);

		exit = new Button("Exit System");
		Button display_all_files = new Button("Display All Files");
		Button download_files = new Button("Download Files");
		Button change_password = new Button("Change Your Password");
		Button log_out = new Button("Log Out");

		exit.setMinWidth(300);
		change_password.setMinWidth(300);
		display_all_files.setMinWidth(300);
		download_files.setMinWidth(300);
		log_out.setMinWidth(300);

		//vb for middle line layout
		VBox vb1 = new VBox();
		vb1.getChildren().addAll(display_all_files, download_files, change_password, log_out, exit);
		vb1.setAlignment(Pos.CENTER);
		vb1.setSpacing(15);
		vb1.setAlignment(Pos.CENTER);
		//hb for title
		hb = new HBox();
		hb.getChildren().add(title);
		hb.setAlignment(Pos.CENTER);
		bp = new BorderPane();
		bp.setTop(hb);
		bp.setCenter(vb1);
		scene = new Scene(bp);
		scene.getStylesheets().add("dms.css");
		title.setStyle("-fx-font-size: 25pt;");
		window.setScene(scene);
		window.show();


		download_files.setOnAction(e -> {
			window.close();
			download_filesClicked(br);

		});
		display_all_files.setOnAction(e -> {
			window.close();
			display_all_filesClicked(br);
		});
		change_password.setOnAction(e -> {
			window.close();
			change_passwordClicked(br);
		});
		log_out.setOnAction(e -> {
			window.close();
			showTouristMenu();
			br.setLoggedIn(false);
		});
		exit.setOnAction(e -> {window.close(); User.exitSystem();});
	}
	static public void download_filesClicked(User u)
	{
		window.setWidth(1100);
		HBox hb_search_line = new HBox();
		HBox hb_bot_line = new HBox();
		Label regex = new Label("RegEx:");
		regex.setStyle("-fx-font-size: 17pt; -fx-font-weight: BOLD;");
		nameInput.clear();
		nameInput.setPromptText("search ID's");
		Button search = new Button("Search");
		Button download = new Button("Download");
		Button back = new Button("<Back");
		search.setMinSize(70,30);
		hb_search_line.getChildren().addAll(regex, nameInput, search);
		hb_search_line.setAlignment(Pos.CENTER);
		hb_search_line.setSpacing(15);
		hb_bot_line.getChildren().addAll(download, back);
		hb_bot_line.setSpacing(15);
		hb_bot_line.setAlignment(Pos.BASELINE_RIGHT);

		tableView = new TableView<>();
		tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		fileTableView = new TableView<>();
		fileTableViewInitialization();

		VBox vb = new VBox();
		vb.setSpacing(15);
		vb.getChildren().addAll(hb_search_line, fileTableView, hb_bot_line);
		vb.setPadding(new Insets(01, 10, 0, 10));
		Scene scene = new Scene(vb);
		scene.getStylesheets().addAll("dms.css");
		window.setScene(scene);
		window.show();


		back.setOnAction(e -> {
			window.close();
			u.showMenu();
		});
		download.setOnAction(e -> {
			PopupBox.downloadPathnameAsker();
		});
		search.setOnAction(e -> {
			fileTableView.setItems(AllDocs(nameInput.getText()));
		});
	}
	static public void display_all_filesClicked(User u)
	{
		window.setWidth(1100);

		fileTableView = new TableView<>();
		fileTableViewInitialization();
		fileTableView.setItems(AllDocs(".*"));

		Button back = new Button("<Back");
		HBox hb = new HBox();
		hb.getChildren().addAll(back);
		back.setAlignment(Pos.BOTTOM_RIGHT);
		hb.setAlignment(Pos.BOTTOM_RIGHT);
		VBox vb = new VBox();
		vb.setSpacing(15);
		vb.getChildren().addAll(fileTableView, hb);
		vb.setPadding(new Insets(0, 10, 0, 10));
		Scene scene = new Scene(vb);
		scene.getStylesheets().addAll("dms.css");
		window.setScene(scene);
		window.show();


		back.setOnAction(e -> {
			window.close();
			u.showMenu();
		});
	}
	static public ObservableList<Documentary> AllDocs(String regex)
	{
		Documentary u;
		ObservableList<Documentary> docs = FXCollections.observableArrayList();
		Enumeration<Documentary> e = DataProcessing.getAllDocuments();
		while(e.hasMoreElements())
		{
			u = e.nextElement();
			System.out.println(u.getFileName());
			if(u.getID().matches(regex))
				docs.add(u);
		}
		return docs;
	}
	static public void fileTableViewInitialization()
	{
		TableColumn<Documentary, String> idColumn = new TableColumn<>("ID");
		TableColumn<Documentary, String> descriptionColumn = new TableColumn<>("Description");
		TableColumn<Documentary, String> creatorColumn = new TableColumn<>("Creator");
		TableColumn<Documentary, String> filenameColumn = new TableColumn<>("File Name");
		TableColumn<Documentary, Timestamp> timeStampColumn = new TableColumn<>("Timestamp");
		idColumn.setMinWidth(300);
		descriptionColumn.setMinWidth(200);
		creatorColumn.setMinWidth(100);
		filenameColumn.setMinWidth(200);
		timeStampColumn.setMinWidth(250);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator"));
		filenameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
		timeStampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

		fileTableView.getColumns().addAll(idColumn, descriptionColumn, creatorColumn, filenameColumn, timeStampColumn);
	}
	static public void the_following_d(String pathname)
	{

		Alert _alert;

		Documentary doc = fileTableView.getSelectionModel().getSelectedItem();
		File out = new File(pathname, doc.getFileName());
		if(!out.exists())
		{

			try
			{
				out.createNewFile();
			} catch (IOException ioe)
			{
				_alert = new Alert(Alert.AlertType.ERROR,"fail to create output file", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
						new ButtonType("OK", ButtonBar.ButtonData.YES));
				_alert.showAndWait();
			}
			File files = new File(DataProcessing.DB_pathname, DataProcessing.File_r_pathname);
			File inputFile = new File(files, doc.getFileName());

			FileOutputStream o = null;
			try
			{
				o = new FileOutputStream(out);
			} catch (FileNotFoundException ex)
			{
				_alert = new Alert(Alert.AlertType.ERROR,"OUTPUT file not found", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
						new ButtonType("OK", ButtonBar.ButtonData.YES));
				_alert.showAndWait();

			}
			FileInputStream i = null;
			try
			{
				i = new FileInputStream(inputFile);
			} catch (FileNotFoundException ex)
			{
				_alert = new Alert(Alert.AlertType.ERROR,"No INPUT file directory.", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
						new ButtonType("OK", ButtonBar.ButtonData.YES));
				_alert.showAndWait();
			}

			try
			{
				byte[] buf = new byte[1024 * 1024];
				int len;
				while ((len = i.read(buf)) != -1)
					o.write(buf, 0, len);
			} catch (IOException ex)
			{
				System.out.println(ex.toString());
			}
			_alert = new Alert(Alert.AlertType.CONFIRMATION,"Download completed.", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
					new ButtonType("OK", ButtonBar.ButtonData.YES));
			_alert.showAndWait();
		}else
		{
			_alert = new Alert(Alert.AlertType.ERROR,"Output file already exists", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
					new ButtonType("OK", ButtonBar.ButtonData.YES));
			_alert.showAndWait();
		}

	}
	static public void showOperatorMenu(User op)
	{
		title = new Label("Operator's Menu");
		window.setHeight(500);

		exit = new Button("Exit System");
		Button display_all_files = new Button("Display All Files");
		Button download_files = new Button("Download Files");
		Button change_password = new Button("Change Your Password");
		Button log_out = new Button("Log Out");
		Button upload_files = new Button("Upload Files");

		exit.setMinWidth(300);
		change_password.setMinWidth(300);
		display_all_files.setMinWidth(300);
		download_files.setMinWidth(300);
		log_out.setMinWidth(300);
		upload_files.setMinWidth(300);

		//vb for middle line layout
		VBox vb1 = new VBox();
		vb1.getChildren().addAll(upload_files, download_files, display_all_files, change_password, log_out, exit);
		vb1.setAlignment(Pos.CENTER);
		vb1.setSpacing(15);
		vb1.setAlignment(Pos.CENTER);
		//hb for title
		hb = new HBox();
		hb.getChildren().add(title);
		hb.setAlignment(Pos.CENTER);
		bp = new BorderPane();
		bp.setTop(hb);
		bp.setCenter(vb1);
		scene = new Scene(bp);
		scene.getStylesheets().add("dms.css");
		title.setStyle("-fx-font-size: 25pt;");
		window.setScene(scene);
		window.show();


		download_files.setOnAction(e -> {
			window.close();
			download_filesClicked(op);

		});
		display_all_files.setOnAction(e -> {
			window.close();
			display_all_filesClicked(op);
		});
		change_password.setOnAction(e -> {
			window.close();
			change_passwordClicked(op);
		});
		log_out.setOnAction(e -> {
			window.close();
			showTouristMenu();
			op.setLoggedIn(false);
		});
		upload_files.setOnAction(e -> {
			PopupBox.uploadPathnameAsker(op);
		});
		exit.setOnAction(e -> {window.close(); User.exitSystem();});
	}

	static public void the_following_u(String pathname, String des, User op)
	{

		File inputFile = new File(pathname);
		FileInputStream i = null;
		try
		{
			i = new FileInputStream(inputFile);
		}catch(FileNotFoundException ex)
		{
			System.out.println(ex.toString());
		}

		File db = new File(DataProcessing.DB_pathname);
		File files = new File(db, DataProcessing.File_r_pathname);
		File target = new File(files, inputFile.getName());

		while(target.exists())
		{
			String line = target.getName();
			String rx = ".*\\((\\d+)\\)\\..*";
			String rx1 = "\\((\\d+)\\)\\.";
			String rx2 = "(.*)\\..*";
			String rx3 = "(.*)\\.";
			String a;
			System.out.println(line);
			if(target.getName().matches(".+\\(\\d+\\)\\..*"))
			{
				Pattern p = Pattern.compile(rx);
				Matcher m = p.matcher(line);
				m.matches();
				m.groupCount();
				int x = Integer.parseInt(m.group(1)) + 1;
				a = line.replaceFirst(rx1, "("+ Integer.toString(x)+ ").");
			}
			else
			{
				Pattern p = Pattern.compile(rx2);
				Matcher m = p.matcher(line);
				m.matches();
				m.groupCount();
				String x = m.group(1);
				a = line.replaceFirst(rx3 ,x+"(1).");
			}

			File newFile = new File(target.getParent(), a);
			if(newFile.exists())
			{
				target = newFile;
			}
			else
			{
				System.out.println("a is " + a);
				target.renameTo(newFile);
				break;
			}

		}

		try
		{
			target.createNewFile();
		} catch (IOException ex)
		{
			System.out.println(ex.toString());
		}
		FileOutputStream o = null;
		try
		{
			o = new FileOutputStream(target);
		} catch (FileNotFoundException ex)
		{
			System.out.println(ex.toString());
		}

		try
		{
			byte[] buf = new byte[1024 * 1024];
			int len;
			while ((len = i.read(buf)) != -1)
				o.write(buf, 0, len);
			o.write(i.read());
			System.out.print(1);
		} catch (IOException ex)
		{
			System.out.println(ex.toString());
		}
		Alert _alert = new Alert(Alert.AlertType.CONFIRMATION,"Upload completed.", new ButtonType("Cancel", ButtonBar.ButtonData.NO),
				new ButtonType("OK", ButtonBar.ButtonData.YES));

		DataProcessing.insertDocument(target.getName() + "_" + op.getName(), op.getName(), new Timestamp(System.currentTimeMillis()), des, target.getName());
		DataProcessing.saveData();
		_alert.showAndWait();
	}

	static public File chooseFile(String title)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File selectedFile = fileChooser.showOpenDialog(window);
		return selectedFile;
	}
	static public File chooseDirectory(String title)
	{
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Open Resource Directory");
		File selectedFile = fileChooser.showDialog(window);
		return selectedFile;
	}


}

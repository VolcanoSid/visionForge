package application.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainController {
	
	

	@FXML
	private BorderPane mainBorderPane;

	@FXML
	private Button imageButton;

	@FXML
	private Button videoButton;

	public MainController() {
		System.out.println("MainController loaded!");
	}

	@FXML
	private void initialize() {
		// Optional: load image view by default
	}

	@FXML void loadImageView() {
		try {
			System.out.println("Loading image_view.fxml...");
			AnchorPane imageView = FXMLLoader.load(getClass().getResource("/fxml/image_view.fxml"));
			mainBorderPane.setCenter(imageView);
		} catch (IOException e) {
			System.err.println("Failed to load image_view.fxml");
			e.printStackTrace();
		}
	}

	@FXML void loadVideoView() {
		try {
			System.out.println("Loading video_view.fxml...");
			AnchorPane videoView = FXMLLoader.load(getClass().getResource("/fxml/video_view.fxml"));
			mainBorderPane.setCenter(videoView);
		} catch (IOException e) {
			System.err.println("Failed to load video_view.fxml");
			e.printStackTrace();
		}
	}
}

package com.vasylyna.travelplanningapplication.controllers;

import com.vasylyna.travelplanningapplication.database.CountryDAO;
import com.vasylyna.travelplanningapplication.database.entity.Country;
import com.vasylyna.travelplanningapplication.util.SceneLoaderUtil;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainTabController {
    private static MainTabController instance;

    @FXML
    private VBox mapVBox;
    @FXML
    private Label countryCountLabel;

    private Group mapGroup;
    private Set<String> visitedCountryCodes = new HashSet<>();

    public void initialize() {
        VBox.setMargin(mapVBox, new Insets(40, 0, 0, 0));
        createMap();
        highlightVisitedCountries();
        updateCountryCountLabel();
    }

    @FXML
    protected void onFinances() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/finances-tab/finances-tab-view.fxml",
                (Stage) mapVBox.getScene().getWindow());

    }

    @FXML
    protected void onExit() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/registration/registration-view.fxml",
                (Stage) mapVBox.getScene().getWindow());
    }

    @FXML
    protected void onWeather() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/weather-tab/weather-tab-view.fxml",
                (Stage) mapVBox.getScene().getWindow());
    }

    @FXML
    protected void onJourneys() {
        SceneLoaderUtil.loadScene("/com/vasylyna/travelplanningapplication/journeys-tab/journeys-tab-view.fxml",
                (Stage) mapVBox.getScene().getWindow());
    }

    private void createMap() {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                    getClass().getClassLoader().getResourceAsStream("world.svg"));

            NodeList paths = doc.getElementsByTagName("path");

            mapGroup = new Group();
            for (int i = 0; i < paths.getLength(); i++) {
                Element element = (Element) paths.item(i);
                String id = element.getAttribute("id");
                String d = element.getAttribute("d");

                SVGPath svgPath = new SVGPath();
                svgPath.setContent(d);
                svgPath.setId(id);
                svgPath.setFill(Color.LIGHTGRAY);
                svgPath.setStroke(Color.BLACK);

                mapGroup.getChildren().add(svgPath);
            }
            mapGroup.setScaleX(0.7);
            mapGroup.setScaleY(0.7);
            mapVBox.getChildren().add(mapGroup);
        } catch (Exception e) {}
    }

    private boolean isCountryVisited(String countryCode) {
        return visitedCountryCodes.contains(countryCode.toUpperCase());
    }


    private void highlightVisitedCountries() {
        List<Country> visitedCountries = CountryDAO.getAllCountries(LoginController.currentUserId, "visited_countries");
        visitedCountryCodes.clear();
        for (Country country : visitedCountries) {
            visitedCountryCodes.add(country.getCode().toUpperCase());
        }

        for(Node node : mapGroup.getChildren()) {
            SVGPath svgPath = (SVGPath) node;
            svgPath.setOnMouseEntered(e -> svgPath.setFill(Color.DARKGRAY));
            svgPath.setOnMouseExited(e -> svgPath.setFill(Color.LIGHTGRAY));
            if (isCountryVisited(svgPath.getId().toUpperCase())) {
                svgPath.setOnMouseExited(e -> svgPath.setFill(Color.web("#F5BB40")));
                svgPath.setOnMouseEntered(e -> svgPath.setFill(Color.web("D49A28")));
                svgPath.setFill(Color.web("#F5BB40"));
            }
        }
    }
    public void updateCountryCountLabel() {
        int visited = CountryDAO.countVisitedCountries();
        int total = CountryDAO.countAllCountriesInWorld();
        countryCountLabel.setText(visited + "/" + total);
    }
}
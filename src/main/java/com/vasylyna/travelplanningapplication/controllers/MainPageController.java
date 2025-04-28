package com.vasylyna.travelplanningapplication.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;

public class MainPageController {

    @FXML
    private VBox mapVBox;

    public void initialize() {
        VBox.setMargin(mapVBox, new Insets(40, 0, 0, 0));
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                    getClass().getClassLoader().getResourceAsStream("world.svg"));

            NodeList paths = doc.getElementsByTagName("path");

            Group mapGroup = new Group();
            for (int i = 0; i < paths.getLength(); i++) {
                Element element = (Element) paths.item(i);
                String id = element.getAttribute("id");
                String d = element.getAttribute("d");

                SVGPath svgPath = new SVGPath();
                svgPath.setContent(d);
                svgPath.setId(id);
                svgPath.setFill(Color.LIGHTGRAY);
                svgPath.setStroke(Color.BLACK);
                svgPath.setOnMouseEntered(e -> svgPath.setFill(Color.DARKGRAY));
                svgPath.setOnMouseExited(e -> svgPath.setFill(Color.LIGHTGRAY));

                mapGroup.getChildren().add(svgPath);
            }
            mapGroup.setScaleX(0.7);
            mapGroup.setScaleY(0.7);
            mapVBox.getChildren().add(mapGroup);
        } catch (Exception e) {}
    }
}

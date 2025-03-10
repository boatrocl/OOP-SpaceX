package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StarBackground {
	int numberOfStars = 100;
	Random random = new Random();
	Pane gamePane;
	private List<Circle> stars;
	
	public StarBackground(Pane gamePane) {
		this.gamePane = gamePane;
		this.stars = new ArrayList<>();
		this.random = new Random();
		
		addStars();
	}
	
	
	private void addStars() {
		int i;
		for(i=0; i<numberOfStars; i++) {
			double x = random.nextDouble() * gamePane.getWidth();
			double y = random.nextDouble() * gamePane.getHeight();
			double radius = random.nextDouble() * 2 + 1;
			double speed;
			
			if(i < numberOfStars / 3) {
				speed = random.nextDouble()	* 0.5 + 0.5;
			} else if (i < (2 * numberOfStars) / 3) {
				speed = random.nextDouble() * 1 + 1;
 			} else {
 				speed = random.nextDouble() * 2 + 2;
 			}
			
			Circle star = new Circle(x, y, radius);
			star.setFill(Color.WHITE);
			star.setUserData(speed);
			stars.add(star);
			gamePane.getChildren().add(star);
		}
	}
	
	public void moveStars() {
		for(Circle star : stars) {
			double speed = (double) star.getUserData();
			star.setCenterY(star.getCenterY() + speed);
			
			if(star.getCenterY() > gamePane.getHeight()) {
				star.setCenterY(0);
				star.setCenterX(random.nextDouble() * gamePane.getWidth());
			}
		}
	}

}

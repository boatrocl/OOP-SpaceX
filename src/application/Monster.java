package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Monster {
	private static final List<Monster> monsters = new ArrayList<>();
	private ImageView sprite;
	private double speed;
	private static double speedMultiplier = 1.0;
	
	public Monster(Pane gamePane, boolean isBoss) {
		Image monsterImage = new Image(getClass().getResourceAsStream("/resources/monster.png"));
		sprite = new ImageView(monsterImage);
		sprite.setFitWidth(isBoss ? 128 : 32);
		sprite.setFitHeight(isBoss ? 128 : 32);
		sprite.setX(Math.random() * (gamePane.getWidth() - sprite.getFitWidth()));
		sprite.setY(0);
		gamePane.getChildren().add(sprite);
		monsters.add(this);
		
		this.speed = 4.0 * speedMultiplier;
	}
	
	public static void updateAll(Pane gamePane, Player player) {
		speedMultiplier = 1.0 + ((double) player.getScore() / 100);
		
		for(Monster monster : new ArrayList<>(monsters)) {
			monster.sprite.setY(monster.sprite.getY() + monster.speed);
			if(monster.sprite.getY() > gamePane.getHeight()) {
				gamePane.getChildren().remove(monster.sprite);
				monsters.remove(monster);
			}
		}
	}
	
	public static boolean checkCollision(ImageView playerSprite) {
		for(Monster monster : monsters) {
			if(monster.sprite.getBoundsInParent().intersects(playerSprite.getBoundsInParent())) {
				return true;
			}
		}
		return false;
	}
	
	public static void remove(Monster monster, Pane gamePane) {
		gamePane.getChildren().remove(monster.sprite);
		monsters.remove(monster);
	}
	
	public ImageView getSprite() {
		return sprite;
	}
	public static List<Monster> getMonsters(){
		return monsters;
	}
	public static void clearMonsters() {
		monsters.clear();
	}
	public static double getMonsterSpeedMult() {
		return speedMultiplier;
	}
}

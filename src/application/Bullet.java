package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Bullet {
	private static final List<Bullet> bullets = new ArrayList<>();
	private ImageView sprite;
	private Player player;
	
	public Bullet(Pane gamePane, double x, double y, Player player) {
		this.player = player;
		
		Image bulletImage = new Image(getClass().getResourceAsStream("/resources/bullet.png"));
		sprite = new ImageView(bulletImage);
		sprite.setFitWidth(10);
		sprite.setFitHeight(20);
		sprite.setX(x);
		sprite.setY(y);
		gamePane.getChildren().add(sprite);
		bullets.add(this);
	}
	
	public static void updateAll(Pane gamePane, Boss boss) {
		for(Bullet bullet : new ArrayList<>(bullets)) {
			bullet.sprite.setY(bullet.sprite.getY() - 5);
			if(bullet.sprite.getY() < 0) {
				gamePane.getChildren().remove(bullet.sprite);
				bullets.remove(bullet);
			} else {
				if(boss != null && bullet.sprite.getBoundsInParent().intersects(boss.getSprite().getBoundsInParent())) {
					gamePane.getChildren().remove(bullet.sprite);
					bullets.remove(bullet);
					boss.takeDamage(1);
				}
				
				for(Monster monster : new ArrayList<>(Monster.getMonsters())) {
					if(bullet.sprite.getBoundsInParent().intersects(monster.getSprite().getBoundsInParent())) {
						gamePane.getChildren().remove(bullet.sprite);
						bullets.remove(bullet);
						Monster.remove(monster, gamePane);
						
						bullet.player.increaseScore(1);
						break;
					}
				}
			}
		}
	}
	
	public static List<Bullet> getBullets(){
		return bullets;
	}
	public static void clearBullets() {
		bullets.clear();
	}
}

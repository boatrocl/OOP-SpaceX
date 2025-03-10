package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class BossBullet {
	private static final List<BossBullet> bullets = new ArrayList<>();
	private ImageView sprite;
	
	public BossBullet(Pane gamePane, double x, double y) {
		Image bulletImage = new Image(getClass().getResourceAsStream("/resources/boss_bullet.png"));
		sprite = new ImageView(bulletImage);
		sprite.setFitWidth(15);
		sprite.setFitHeight(30);
		sprite.setX(x);
		sprite.setY(y);
		gamePane.getChildren().add(sprite);
		bullets.add(this);
	}
	
	public static void updateAll(Pane gamePane, Player player) {
		for(BossBullet bullet : new ArrayList<>(bullets)) {
			bullet.sprite.setY(bullet.sprite.getY() + 4);
			if(bullet.sprite.getY() > gamePane.getHeight()) {
				gamePane.getChildren().remove(bullet.sprite);
				bullets.remove(bullet);
			} else if(bullet.sprite.getBoundsInParent().intersects(player.getSprite().getBoundsInParent())) {
				gamePane.getChildren().remove(bullet.sprite);
				bullets.remove(bullet);
				player.setDefeated(true);
			}
		}
	}
	
	public static boolean checkCollision(ImageView playerSprite) {
		for(BossBullet bullet : bullets) {
			if(bullet.sprite.getBoundsInParent().intersects(playerSprite.getBoundsInParent())) {
				return true;
			}
		}
		return false;
	}
	
	public static List<BossBullet> getBossBullets() {
		return bullets;
	}
	public static void clearBullets() {
		bullets.clear();
	}
}

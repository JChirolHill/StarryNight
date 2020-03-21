package FinalProject;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {
    public static final Point PADDLE_DIMENS = new Point(50, 10);
    public static final float FROM_BOTTOM = 50.0f;
    private static final float VELOCITY = 10.0f;

    private Game game;

    public Paddle(Game game) {
        this.game = game;
        this.setX(Game.SCENE_WIDTH / 2 - PADDLE_DIMENS.x / 2);
        this.setY(Game.SCENE_HEIGHT - FROM_BOTTOM);
        this.setWidth(PADDLE_DIMENS.x);
        this.setHeight(PADDLE_DIMENS.y);
        this.setFill(new ImagePattern(new Image("FinalProject/hobbiton.jpg")));
    }

    public void handleMouseMove(Point event) {
        if(event.x < PADDLE_DIMENS.x / 2) { // prevent going off the edge
            this.setX(0);
        }
        else if(event.x > Game.SCENE_WIDTH - PADDLE_DIMENS.x / 2) { // prevent going off the edge
            this.setX(Game.SCENE_WIDTH - PADDLE_DIMENS.x);
        }
        else { // move normally
            this.setX(event.x - PADDLE_DIMENS.x / 2);
        }
    }

//    public void handleKeyMove(float xDir) {
//        System.out.println(xDir * VELOCITY);
//        this.setX(this.getX() + xDir * VELOCITY);
//
//        // prevent going off the edge
//        if(this.getX() < 0) {
//            this.setX(0);
//        }
//        else if(this.getX() > Game.SCENE_WIDTH - PADDLE_DIMENS.x) {
//            this.setX(Game.SCENE_WIDTH - PADDLE_DIMENS.x);
//        }
//    }

    public boolean didCollide(Sun s) {
        // if sun above the paddle, cannot collide
        if(s.getCenterY() + s.getRadius() < Game.SCENE_HEIGHT - FROM_BOTTOM) {
            return false;
        }

        // check if there is an intersection between the ball and wall
        if(s.getCenterX() + s.getRadius() < this.getX()
                || s.getCenterX() - s.getRadius() > this.getX() + PADDLE_DIMENS.x) {
            return false;
        }
        return true;
    }
}

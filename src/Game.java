import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class Game extends Application {
    public static final float SCENE_WIDTH = 800.0f;
    public static final float SCENE_HEIGHT = 700.0f;
    private static final String MUSIC = "Assets/betterdays.mp3";

    private StackPane superRoot;
    private Pane root;
    private Pane cloudRoot;
    private TransitionBackground transBackground;
    private GameOverMenu gameOverMenu;
    private Text textScore;
    private Sun sun;
    public Paddle paddle;
    private Driver driver;
    private int score;
    private ArrayList<Star> stars;
    public ArrayList<Cloud> clouds;
    public boolean gameOver;
    private AudioClip sound;

    public void init() {
        score = 0;
        gameOver = false;
        stars = new ArrayList<>();
        clouds = new ArrayList<>();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // set up stage
        stage.setTitle("Starry Night");;
        superRoot = new StackPane();
        Scene scene = new Scene(superRoot, SCENE_WIDTH, SCENE_HEIGHT);
        setUpStage(stage);
        stage.setScene(scene);
        stage.show();

        // set up and play music
        sound = ResourceLoader.loadAudioClip(MUSIC);
        sound.setCycleCount(AudioClip.INDEFINITE);
        sound.play();

        // create backgrounds
        transBackground = new TransitionBackground(this);
        superRoot.getChildren().add(transBackground);

        // create root responsible for game functions
        root = new Pane();
        superRoot.getChildren().add(root);

        // create pane for clouds
        cloudRoot = new Pane();
        superRoot.getChildren().add(cloudRoot);

        // create game over menu
        gameOverMenu = new GameOverMenu(this, scene);
        superRoot.getChildren().add(gameOverMenu);

        // create score text in top right corner
        textScore = new Text();
        textScore.setFill(Color.WHITE);
        textScore.setX(SCENE_WIDTH - 200);
        textScore.setY(50);
        textScore.setFont(Font.loadFont(getClass().getResourceAsStream("Assets/ShakeItOff.ttf"), 20));
        updateScore(0);
        root.getChildren().add(textScore);

        // create the sun
        sun = new Sun(this);
        root.getChildren().add(sun);

        // create paddle
        paddle = new Paddle(this);
        root.getChildren().add(paddle);

        // move listener for mouse to move paddle
        cloudRoot.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!gameOver) {
                    paddle.handleMouseMove(new Point(event.getX(), event.getY()));
                }
            }
        });

        // move listener for keyboard left and right or A and D
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                    paddle.handleKeyMove(-1.0f);
                }
                else if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                    paddle.handleKeyMove(1.0f);
                }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                    paddle.handleKeyMove(0);
                }
                else if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                    paddle.handleKeyMove(0);
                }
            }
        });

        // set up the animation for sun
        driver = new Driver(this);
        driver.start();
    }

    // prevent stretching of edges
    private void setUpStage(Stage stage) {
        stage.setMinWidth(SCENE_WIDTH);
        stage.setMaxWidth(SCENE_WIDTH);
        stage.setMinHeight(SCENE_HEIGHT);
        stage.setMaxHeight(SCENE_HEIGHT);
    }

    public Sun getSun() {
        return sun;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void createStar(Point start, Point dest, boolean chirality) {
        Star s = new Star(start, dest, chirality);
        stars.add(s);
        root.getChildren().add(s);
    }

    public void createCloud() {
        Cloud c = new Cloud(this);
        clouds.add(c);
        cloudRoot.getChildren().add(c);
    }

    public void removeCloud(Cloud c) {
        clouds.remove(c);
        cloudRoot.getChildren().remove(c);
    }

    public void handleGameOver() {
        // hide score display
        textScore.setVisible(false);

        // make stars shine animation and fade to night
        transBackground.transitionToForeground();
    }

    public void handleStartOver() {
        // hide game over menu
        gameOverMenu.hide();

        // remove all clouds
        cloudRoot.getChildren().removeAll(clouds);
        clouds.clear();

        // remove all stars
        root.getChildren().removeAll(stars);
        stars.clear();

        // reset necessary elements
        sun.reset();
        transBackground.reset();
        textScore.setVisible(true);
        gameOver = false;

        // reset score
        score = 0;
    }

    public void handleBackgroundTransitionOver() {
        // show game over menu
        gameOverMenu.show(score);
    }

    // increase score by amt and update the display
    public void updateScore(int amt) {
        score += amt;
        textScore.setText(String.format("Score: %d", score));
    }

    public int getNumStars() {
        return this.stars.size();
    }
}

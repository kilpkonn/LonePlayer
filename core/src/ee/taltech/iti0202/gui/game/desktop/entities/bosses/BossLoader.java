package ee.taltech.iti0202.gui.game.desktop.entities.bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.joints.BossHelper;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.magmaworm.MagmaWorm;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.magmaworm.MagmaWormProperties;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.plantworm.PlantWorm;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.plantworm.PlantWormProperties;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman.SnowMan;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman.SnowManProperties;
import ee.taltech.iti0202.gui.game.desktop.states.Play;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BOSS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;

public class BossLoader {

    private Play play;
    private SpriteBatch spriteBatch;
    private float scale = 1f;
    private Vector2 tempPosition;
    private aurelienribon.bodyeditor.BodyEditorLoader bossLoader;

    public BossLoader(Play play, SpriteBatch spriteBatch) {
        this.play = play;
        this.spriteBatch = spriteBatch;
    }

    public void createBosses(Vector2 position, String type, boolean decider, int size) {

        /////////////////////////////////////////////////////////////////////////
        //                                                                     //
        //   TYPE 1: MAGMA BOSS, can flu through walls n shit                  //
        //   TYPE 2: COLOSSEOS, net.dermetfan.gdx.physics.box2d.Breakable      //
        //   TYPE 3: idk                                                       //
        //                                                                     //
        /////////////////////////////////////////////////////////////////////////

        scale = decider ? 1f : 0.5f;
        tempPosition = position;
        bossLoader = new aurelienribon.bodyeditor.BodyEditorLoader(Gdx.files.internal(PATH + "bosses" + type + ".json"));

        switch (type) {
            case "1":
                Array<Boss> tempArray = new Array<>();
                initSnakePart(MagmaWorm.Part.HEAD, this.scale, tempArray);
                tempPosition.y -= 60 * this.scale / PPM;

                for (int i = 0; i < size; i++) {
                    if (i == size - 1) {
                        initSnakePart(MagmaWorm.Part.TAIL, this.scale, tempArray);
                    } else {
                        initSnakePart(MagmaWorm.Part.BODY, this.scale, tempArray);
                    }
                    //createRopeJointBetweenLinks(tempArray, -1f);
                    BossHelper.createDistanceJointBetweenLinks(tempArray, 0.40f, this.scale, play.getWorld());
                    BossHelper.createDistanceJointBetweenLinks(tempArray, 0.50f, this.scale, play.getWorld());
                    BossHelper.createDistanceJointBetweenLinks(tempArray, 0.60f, this.scale, play.getWorld());
                    //createRopeJointGEOCordBetweenLinksPlantWorm(tempArray, 0.50f);

                }
                BossHelper.filterTextures(tempArray);

                play.getMagmabossArray().add(tempArray);
                break;

            case "2":
                play.setTakingTurnsBase(10 - size);
                play.setPlantBossSize(size);
                Array<Array<Boss>> tempArray2 = new Array<>();
                for (int i = 0; i < size; i++) {
                    tempArray2.add(new Array<Boss>());
                }
                initPlantPart(tempArray2, PlantWorm.Part.FLOWER_HEAD, 0, 100, 100);
                for (int i = 1; i < size; i++) {
                    tempArray2.get(i).add(tempArray2.get(0).get(0));
                }
                tempPosition.x += 50 / PPM;
                tempPosition.y -= 50 / PPM;

                int vine = size * 5;
                for (int i = 0; i < vine; i++) {
                    for (int j = 0; j < size; j++) {
                        if (i == vine - 1) {
                            initPlantPart(tempArray2, PlantWorm.Part.CLAW_HEAD, j, 100, 100);
                        } else {
                            initPlantPart(tempArray2, PlantWorm.Part.BODY, j, 50, 50);
                        }
                        if (i == 0 || i == vine - 1) {
                            BossHelper.createRopeJointGEOCordBetweenLinksPlantWorm(tempArray2.get(j), i, play.getWorld());
                        } else {
                            BossHelper.createDistanceJointBetweenLinks(tempArray2.get(j), 0.4f, this.scale, play.getWorld());
                            BossHelper.createDistanceJointBetweenLinks(tempArray2.get(j), 0.5f, this.scale, play.getWorld());
                            BossHelper.createDistanceJointBetweenLinks(tempArray2.get(j), 0.6f, this.scale, play.getWorld());
                        }
                    }
                    tempPosition.y -= 10 / PPM;
                }

                BossHelper.filterTextures(tempArray2.get(0));
                for (int i = 1; i < size; i++) {
                    tempArray2.get(i).reverse();
                }

                for (Array<Boss> bosses : tempArray2)
                    play.getPlantbossArray().add(bosses);
                break;

            case "3":
                SnowManProperties alias = new SnowManProperties(play.getBdef(), play.getFdef(), tempPosition);
                Body body = play.getWorld().createBody(alias.getBdef());
                body.createFixture(alias.getFdef());
                bossLoader.attachFixture(body, "snowman", alias.getFdef(), 1f * size);
                Boss boss = SnowMan.builder().body(body).spriteBatch(spriteBatch).type(type).play(play).xOffset(0).yOffset(0).build();
                boss.getBody().setUserData(BOSS);
                for (Fixture fixture : boss.getBody().getFixtureList()) fixture.setUserData(BOSS);
                play.getSnowManArray().add(boss);
                break;
        }

    }

    private void initPlantPart(Array<Array<Boss>> tempArray2, PlantWorm.Part part, int size, float x, float y) {
        PlantWormProperties alias = new PlantWormProperties(play.getBdef(), play.getFdef(), tempPosition);
        Body body = play.getWorld().createBody(alias.getBdef());
        body.createFixture(alias.getFdef());
        bossLoader.attachFixture(body, part.toString(), alias.getFdef(), part.equals(PlantWorm.Part.BODY) ? 1f : 2f);
        Boss boss = PlantWorm.builder().body(body).yOffset(y).xOffset(x).size(size).type(BOSS).part(part).play(play).spriteBatch(spriteBatch).build();
        for (Fixture fixture : boss.getBody().getFixtureList())
            fixture.setUserData(part.equals(PlantWorm.Part.CLAW_HEAD) ? BOSS + BOSS : BOSS);
        boss.getBody().setUserData(part.equals(PlantWorm.Part.CLAW_HEAD) ? BOSS + BOSS : BOSS);
        tempArray2.get(size).add(boss);
    }

    private void initSnakePart(MagmaWorm.Part part, float size, Array<Boss> tempArray) {
        MagmaWormProperties alias = new MagmaWormProperties(play.getBdef(), play.getFdef(), tempPosition);
        Body body = play.getWorld().createBody(alias.getBdef());
        body.createFixture(alias.getFdef());
        bossLoader.attachFixture(body, part.toString() + size, alias.getFdef(), this.scale);
        Boss boss = MagmaWorm.builder().body(body).spriteBatch(spriteBatch).type(BOSS).play(play).part(part).size(size).xOffset(50 * this.scale).yOffset(50 * this.scale).build();
        boss.getBody().setUserData(BOSS);
        for (Fixture fixture : boss.getBody().getFixtureList()) fixture.setUserData(BOSS);
        tempArray.add(boss);
        play.getTempPlayerLocation().y -= 50 * this.scale / PPM;
    }
}

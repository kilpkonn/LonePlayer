package ee.taltech.iti0202.gui.game.desktop.entities.bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import java.util.List;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.joints.BossHelper;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.magmaworm.MagmaWorm;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.magmaworm.MagmaWormProperties;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.plantworm.PlantWorm;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.plantworm.PlantWormProperties;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman.SnowMan;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman.SnowManProperties;
import ee.taltech.iti0202.gui.game.desktop.states.Play;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.BossData;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BOSS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;

public class BossLoader {

    private Play play;
    private SpriteBatch spriteBatch;
    private Vector2 tempPosition;
    private aurelienribon.bodyeditor.BodyEditorLoader bossLoader;
    private FixtureDef fdef;
    private BodyDef bdef;

    public BossLoader(Play play, SpriteBatch spriteBatch, FixtureDef fdef, BodyDef bdef) {
        this.play = play;
        this.spriteBatch = spriteBatch;
        this.fdef = fdef;
        this.bdef = bdef;
    }

    public void createAllBosses(List<BossData> bosses) {
        for (BossData boss : bosses) {
            createBosses(new Vector2(boss.locationX, boss.locationY), boss.type, boss.decider, boss.size, true);
        }
    }

    public void createBosses(Vector2 position, String type, boolean decider, int size) {
        createBosses(position, type, decider, size, false);
    }

    public void createBosses(Vector2 position, String type, boolean decider, int size, boolean reload) {

        /////////////////////////////////////////////////////////////////////////
        //                                                                     //
        //   TYPE 1: MAGMA BOSS, can flu through walls n shit                  //
        //   TYPE 2: COLOSSEOS, net.dermetfan.gdx.physics.box2d.Breakable      //
        //   TYPE 3: idk                                                       //
        //                                                                     //
        /////////////////////////////////////////////////////////////////////////

        float scale = decider ? 1f : 0.5f;
        tempPosition = position;
        bossLoader = new aurelienribon.bodyeditor.BodyEditorLoader(Gdx.files.internal(PATH + "bosses" + type + ".json"));

        switch (type) {
            case "1":
                Array<Boss> tempArray = new Array<>();
                initSnakePart(MagmaWorm.Part.HEAD, scale, tempArray, decider);
                if (!reload)
                    tempPosition.y -= 60 * scale / PPM;

                for (int i = 0; i < size; i++) {
                    if (i == size - 1) {
                        initSnakePart(MagmaWorm.Part.TAIL, scale, tempArray, decider);
                    } else {
                        initSnakePart(MagmaWorm.Part.BODY, scale, tempArray, decider);
                    }
                    //createRopeJointBetweenLinks(tempArray, -1f);
                    BossHelper.createDistanceJointBetweenLinks(tempArray, 0.40f, scale, play.getWorld());
                    BossHelper.createDistanceJointBetweenLinks(tempArray, 0.50f, scale, play.getWorld());
                    BossHelper.createDistanceJointBetweenLinks(tempArray, 0.60f, scale, play.getWorld());
                    //createRopeJointGEOCordBetweenLinksPlantWorm(tempArray, 0.50f);

                }
                BossHelper.filterTextures(tempArray);

                play.getMagmaBossArray().add(tempArray);
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
                if (!reload) {
                    tempPosition.x += 50 / PPM;
                    tempPosition.y -= 50 / PPM;
                }

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
                            BossHelper.createDistanceJointBetweenLinks(tempArray2.get(j), 0.4f, scale, play.getWorld());
                            BossHelper.createDistanceJointBetweenLinks(tempArray2.get(j), 0.5f, scale, play.getWorld());
                            BossHelper.createDistanceJointBetweenLinks(tempArray2.get(j), 0.6f, scale, play.getWorld());
                        }
                    }
                    tempPosition.y -= 10 / PPM;
                }

                BossHelper.filterTextures(tempArray2.get(0));
                for (int i = 1; i < size; i++) {
                    tempArray2.get(i).reverse();
                }

                for (Array<Boss> bosses : tempArray2)
                    play.getPlantBossArray().add(bosses);
                break;

            case "3":
                SnowManProperties alias = new SnowManProperties(this.bdef, this.fdef, tempPosition);
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
        PlantWormProperties alias = new PlantWormProperties(this.bdef, this.fdef, tempPosition);
        Body body = play.getWorld().createBody(alias.getBdef());
        body.createFixture(alias.getFdef());
        bossLoader.attachFixture(body, part.toString(), alias.getFdef(), part.equals(PlantWorm.Part.BODY) ? 1f : 2f);
        Boss boss = PlantWorm.builder().body(body).spriteBatch(spriteBatch).type(BOSS).play(play).part(part).xOffset(x).yOffset(y).time(0).build();
        for (Fixture fixture : boss.getBody().getFixtureList())
            fixture.setUserData(part.equals(PlantWorm.Part.CLAW_HEAD) ? BOSS + BOSS : BOSS);
        boss.getBody().setUserData(part.equals(PlantWorm.Part.CLAW_HEAD) ? BOSS + BOSS : BOSS);
        tempArray2.get(size).add(boss);
    }

    private void initSnakePart(MagmaWorm.Part part, float size, Array<Boss> tempArray, boolean decider) {
        MagmaWormProperties alias = new MagmaWormProperties(this.bdef, this.fdef, tempPosition);
        Body body = play.getWorld().createBody(alias.getBdef());
        body.createFixture(alias.getFdef());
        bossLoader.attachFixture(body, part.toString() + size, alias.getFdef(), size);
        Boss boss = MagmaWorm.builder().body(body).spriteBatch(spriteBatch).type(BOSS).play(play).part(part).size(size).xOffset(50 * size).yOffset(50 * size).build();
        boss.setDecider(decider);
        boss.getBody().setUserData(BOSS);
        for (Fixture fixture : boss.getBody().getFixtureList()) fixture.setUserData(BOSS);
        tempArray.add(boss);
        play.getTempPlayerLocation().y -= 50 * size / PPM;
    }
}

package ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.logic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.MyContactListener;
import lombok.Data;

@Data
public abstract class BossLogic {
    protected Array<Array<Boss>> BossBossArray = new Array<>();
    protected Array<Boss> bossArray = new Array<>();
    protected String logic;
    protected double health = 250;
    protected double totalHealth = 1;
    protected float speed = 3f;
    protected MyContactListener cl;
    protected boolean update = true;

    public void update(float dt) {
        if (update) {
            for (Boss boss : bossArray) {
                boss.update(dt);
            }
        }
    }

    public void render(SpriteBatch sb) {
        for (Boss boss : bossArray) {
            boss.render(sb);
        }
    }

    public void updateHP(float dt) {
        for (Boss boss : bossArray) {
            if (cl.getCollidedBullets().containsValue(boss.getBody())) {
                health -= 1;
            }
        }

        for (Array<Boss> bossArray : BossBossArray) {
            for (Boss boss : bossArray) {
                if (cl.getCollidedBullets().containsValue(boss.getBody())) {
                    health -= 1;
                }
            }
        }
    }

    public void updateDeath(float dt) {
        if (update) {
            update = false;
            if (bossArray.size > 0) {
                bossArray.get(0).getBody().getFixtureList().get(0).setDensity(1);
                for (Boss boss : bossArray) {
                    boss.getBody().setGravityScale(1);
                    boss.getBody().setSleepingAllowed(true);
                    for (Fixture fixture : boss.getBody().getFixtureList()) {
                        Filter filter = new Filter();
                        filter.maskBits = 0;
                        filter.categoryBits = 0;
                        fixture.setFilterData(filter);
                        fixture.refilter();
                    }
                }
            }

            if (BossBossArray.size > 0) {
                BossBossArray.get(0).get(0).getBody().getFixtureList().get(0).setDensity(1);
                for (Array<Boss> bossArray : BossBossArray) {
                    for (Boss boss : bossArray) {
                        boss.getBody().setSleepingAllowed(true);
                        boss.getBody().setGravityScale(1);
                        for (Fixture fixture : boss.getBody().getFixtureList()) {
                            Filter filter = new Filter();
                            filter.maskBits = 0;
                            filter.categoryBits = 0;
                            fixture.setFilterData(filter);
                            fixture.refilter();
                        }
                    }
                }
            }
        }

        for (Boss boss : bossArray) {
            boss.update(dt);
        }
    }
}

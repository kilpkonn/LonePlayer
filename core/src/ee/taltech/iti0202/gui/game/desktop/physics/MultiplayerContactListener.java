package ee.taltech.iti0202.gui.game.desktop.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;


public class MultiplayerContactListener implements ContactListener {

    public Map<Integer, PlayerBody.PlayerBodyData> players;
    public Set<Integer> weaponsToRemove = new HashSet<>();
    public Set<Integer> bulletsToRemove = new HashSet<>();

    public MultiplayerContactListener(Map<Integer, PlayerBody.PlayerBodyData> players) {
        this.players = players;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Object oa = fa.getUserData();
        Object ob = fb.getUserData();


        // detect bullet collision
        bulletDetection(fa, fb);

        // set wall jump
        setWallJump(oa, ob, 1);

        groundDetection(oa, ob);

        weaponPickup(oa, ob);

        // detection happens when player goes outside of initial game border
        dmgDetection(oa, ob, fa.getBody(), fb.getBody());

        borderDetection(oa, ob);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Object oa = fa.getUserData();
        Object ob = fb.getUserData();

        //if (oa != null && ob != null) {

        setWallJump(oa, ob, 0);

        PlayerBody.PlayerBodyData player;
        if (oa instanceof PlayerBody.PlayerFoot) {
            player = players.get(((PlayerBody.PlayerFoot) oa).id);

            player.onGround = false;
            player.doubleJump = true;
            // wallJump = 0;
            player.dash = true;
        }
        if (ob instanceof PlayerBody.PlayerFoot) {
            player = players.get(((PlayerBody.PlayerFoot) ob).id);

            player.onGround = false;
            player.doubleJump = true;
            // wallJump = 0;
            player.dash = true;
        }
        //}
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Object oa = fa.getUserData();
        Object ob = fb.getUserData();

        if (oa instanceof WeaponBody.WeaponBodyData) {
            if (!((WeaponBody.WeaponBodyData) oa).dropped) {
                contact.setEnabled(false);
            }
        }

        if (ob instanceof WeaponBody.WeaponBodyData) {
            if (!((WeaponBody.WeaponBodyData) ob).dropped) {
                contact.setEnabled(false);
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void groundDetection(Object oa, Object ob) {
        if (oa instanceof PlayerBody.PlayerFoot) {
            players.get(((BodyData) oa).id).onGround = true;
        }
        if (ob instanceof PlayerBody.PlayerFoot) {
            players.get(((BodyData) ob).id).onGround = true;
        }
    }

    private void borderDetection(Object oa, Object ob) {
        if (oa == null || ob == null) return;
        if (oa.equals("barrier")) {
            if (ob instanceof WeaponBody.WeaponBodyData) {
                weaponsToRemove.add(((WeaponBody.WeaponBodyData) ob).id);
            } else if (ob instanceof BulletBody.BulletBodyData) {
                bulletsToRemove.add(((BulletBody.BulletBodyData) ob).id);
            }
        }

        if (ob.equals("barrier")) {
            if (oa instanceof WeaponBody.WeaponBodyData) {
                weaponsToRemove.add(((WeaponBody.WeaponBodyData) oa).id);
            } else if (oa instanceof BulletBody.BulletBodyData) {
                bulletsToRemove.add(((BulletBody.BulletBodyData) oa).id);
            }
        }
    }

    private void weaponPickup(Object oa, Object ob) {
        if (oa != null && PlayerBody.class.equals(oa.getClass().getEnclosingClass()) && ob instanceof WeaponBody.WeaponBodyData) {
            PlayerBody.PlayerBodyData data = players.get(((BodyData) oa).id);
            WeaponBody.WeaponBodyData weaponBodyData = (WeaponBody.WeaponBodyData) ob;
            if (weaponBodyData.dropped) {
                for (int i = 0; i < data.weapons.length; i++) {
                    if (data.weapons[i] == null) {
                        data.weapons[i] = weaponBodyData;
                        weaponBodyData.dropped = false;
                        break;
                    }
                }
            }
        }

        if (ob != null && PlayerBody.class.equals(ob.getClass().getEnclosingClass()) && oa instanceof WeaponBody.WeaponBodyData) {
            PlayerBody.PlayerBodyData data = players.get(((BodyData) ob).id);
            WeaponBody.WeaponBodyData weaponBodyData = (WeaponBody.WeaponBodyData) oa;
            if (weaponBodyData.dropped) {
                for (int i = 0; i < data.weapons.length; i++) {
                    if (data.weapons[i] == null) {
                        data.weapons[i] = weaponBodyData;
                        weaponBodyData.dropped = false;
                        break;
                    }
                }
            }
        }
    }

    private void bulletDetection(Object oa, Object ob) {
        if (oa instanceof BulletBody.BulletBodyData) {
            ((BulletBody.BulletBodyData) oa).isHit = true;
        }
        if (ob instanceof BulletBody.BulletBodyData) {
            ((BulletBody.BulletBodyData) ob).isHit = true;
        }
    }

    private void dmgDetection(Object oa, Object ob, Body ba, Body bb) {
        if (oa == null || ob == null) {
            return;
        }

        if (oa instanceof PlayerBody.PlayerBodyData || oa instanceof PlayerBody.PlayerFoot) {  //TODO: Rewrite this
            PlayerBody.PlayerBodyData player = players.get(((BodyData) oa).id);
            if (ob.equals("barrier")) {
                player.health = 0;
            } else if (ob.equals("hitboxes")) {
                if (Math.abs(ba.getLinearVelocity().y) > B2DVars.DMG_ON_LANDING_SPEED) {
                    player.health -= Math.abs(ba.getLinearVelocity().y / B2DVars.DMG_ON_LANDING_SPEED * B2DVars.DMG_MULTIPLIER);
                }
            }
        }

        if (ob instanceof PlayerBody.PlayerBodyData || ob instanceof PlayerBody.PlayerFoot) {
            PlayerBody.PlayerBodyData player = players.get(((BodyData) ob).id);
            if (oa.equals("barrier")) {
                player.health = 0;
            } else if (oa.equals("hitboxes")) {
                if (Math.abs(bb.getLinearVelocity().y) > B2DVars.DMG_ON_LANDING_SPEED) {
                    player.health -= Math.abs(bb.getLinearVelocity().y / B2DVars.DMG_ON_LANDING_SPEED * B2DVars.DMG_MULTIPLIER);
                }
            }
        }
    }

    private void setWallJump(Object oa, Object ob, int i) {
        PlayerBody.PlayerBodyData playerA;
        PlayerBody.PlayerBodyData playerB;

        if (oa instanceof PlayerBody.PlayerRightSide) {
            playerA = players.get(((PlayerBody.PlayerRightSide) oa).id);
            playerA.wallJump = -1 * i;
        } else if (oa instanceof PlayerBody.PlayerLeftSide) {
            playerA = players.get(((PlayerBody.PlayerLeftSide) oa).id);
            playerA.wallJump = i;
        }

        if (ob instanceof PlayerBody.PlayerRightSide) {
            playerB = players.get(((PlayerBody.PlayerRightSide) ob).id);
            playerB.wallJump = -1 * i;
        } else if (ob instanceof PlayerBody.PlayerLeftSide) {
            playerB = players.get(((PlayerBody.PlayerLeftSide) ob).id);
            playerB.wallJump = i;
        }
    }

}

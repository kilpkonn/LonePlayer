package ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.bleeding;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Bleeding {
    public static void fixBleeding(TextureRegion region) {
        float fix = 0.01f;

        float x = region.getRegionX();
        float y = region.getRegionY();
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float invTexWidth = 1f / region.getTexture().getWidth();
        float invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion(
                (x + fix) * invTexWidth,
                (y + fix) * invTexHeight,
                (x + width - fix) * invTexWidth,
                (y + height - fix) * invTexHeight); // Trims

        /// region.getTexture().setFilter(Texture.TextureFilter.Linear,
        // Texture.TextureFilter.Linear);
        // //linear no good
        // region.getTexture().getTextureData().useMipMaps();
    }

    public static void fixBleeding(TextureRegion[][] region) {
        for (TextureRegion[] array : region) {
            for (TextureRegion texture : array) {
                fixBleeding(texture);
            }
        }
    }
}

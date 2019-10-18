package ben_mkiv.rendertoolkit.common.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.function.Function;

public class ModelOBJ {
    protected OBJModel model;

    public HashMap<String, IBakedModel> modelParts = new HashMap<>();
    public HashMap<String, OBJModel.OBJState> IModelParts = new HashMap<>();

    public ModelOBJ(ResourceLocation objFile, boolean bakeModelGroups){
        try {
            System.out.println("loading object model from "+objFile.toString());
            model = (OBJModel) OBJLoader.INSTANCE.loadModel(objFile);
        } catch(Exception ex){
            System.out.println("couldnt load object model from "+objFile.toString());
            return;
        }

        // load and bake
        if(bakeModelGroups)
            loadModelGroups();
    }

    static public void renderPart(IBakedModel part){
        if(part == null)
            return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buff = tessellator.getBuffer();
        buff.begin(GL11.GL_QUADS, Attributes.DEFAULT_BAKED_FORMAT);

        for (BakedQuad bakedQuad : part.getQuads(null, null, 0))
            LightUtil.renderQuadColor(buff, bakedQuad, -1);

        tessellator.draw();
    }


    public void loadModelGroups() {
        for (String groupName : model.getMatLib().getGroups().keySet()) {
            IModelParts.put(groupName, getPartIModelState(groupName));
            modelParts.put(groupName, getPartBakedModel(groupName));
        }
    }

    public IBakedModel getPartBakedModel(String groupName){
        if(!IModelParts.containsKey(groupName))
            IModelParts.put(groupName, getPartIModelState(groupName));

        return model.bake(IModelParts.get(groupName), Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
    }

    public OBJModel.OBJState getPartIModelState(String groupName){
        return new OBJModel.OBJState(ImmutableList.of(groupName), false, model.getDefaultState());
    }

    private static Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
        public TextureAtlasSprite apply(ResourceLocation location) {
            return DummyAtlasTexture.instance;
        }
    };

    private static class DummyAtlasTexture extends TextureAtlasSprite {
        public static DummyAtlasTexture instance = new DummyAtlasTexture();

        DummyAtlasTexture() {
            super("dummy");
        }
    }
}

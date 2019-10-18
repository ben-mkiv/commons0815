package ben_mkiv.rendertoolkit.common.entity;

import ben_mkiv.rendertoolkit.bvhParser.Node;
import ben_mkiv.rendertoolkit.bvhParser.Skeleton;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;

import javax.vecmath.Vector4f;
import java.util.HashMap;

abstract public class ModelOBJBVH extends ModelOBJ {

    public HashMap<String, BonedPart> parts = new HashMap<>();

    public ModelOBJBVH(ResourceLocation objFile, ResourceLocation bvhFile){
        super(objFile, false);

        Skeleton skeleton;

        try {
            System.out.println("loading bvh skeleton nodes from "+bvhFile.toString());
            skeleton = new Skeleton(bvhFile);
        } catch(Exception ex){
            System.out.println("couldnt load bvh data from "+bvhFile.toString());
            return;
        }

        if(skeleton.getRootNode() == null){
            System.out.println("missing bvh root");
            return;
        }

        // apply bone offset to their groups
        for(OBJModel.Group group : model.getMatLib().getGroups().values()){
            String name = group.getName();

            Node bone = null;
            for(Node node : skeleton.getRootNode().getChildrens()) {
                if (node.getName().equals(name)) {
                    bone = node;
                    break;
                }
            }

            if(bone == null)
                continue;

            Vector4f offset = new Vector4f(bone.getOffset());

            for(OBJModel.Face face : group.getFaces()){
                for(OBJModel.Vertex v : face.getVertices()){
                    Vector4f tmpV = v.getPos();
                    tmpV.sub(offset);
                    v.setPos(tmpV);
                }
            }
        }

        // load and bake
        loadModelGroups();

        // cache data
        for (Node node : skeleton.getRootNode().getChildrens()) {
            String name = node.getName();
            if(modelParts.containsKey(name)){
                parts.put(name, new BonedPart(modelParts.get(name), IModelParts.get(name), node));
                System.out.println("added boned part: "+node.getName());
            }
        }
    }

    static public void renderPart(BonedPart part){
        GlStateManager.pushMatrix();
        GlStateManager.translate(part.getPosition().x, part.getPosition().y, part.getPosition().z);
        GlStateManager.rotate(part.getRotation());

        renderPart(part.getModel());

        GlStateManager.popMatrix();
    }


}

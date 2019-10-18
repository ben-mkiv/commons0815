package ben_mkiv.rendertoolkit.common.entity;

import ben_mkiv.rendertoolkit.bvhParser.Node;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.util.vector.Quaternion;

public class BonedPart {
    Node bone;
    OBJModel.OBJState iModel;
    IBakedModel model;
    public Quaternion rotation = new Quaternion();
    public Vec3d position;

    public BonedPart(IBakedModel iBakedModel, OBJModel.OBJState iModelState, Node boneNode){
        this.bone = boneNode;
        this.iModel = iModelState;
        this.model = iBakedModel;
        this.position = new Vec3d(boneNode.getOffset().x, boneNode.getOffset().y, boneNode.getOffset().z);
    }

    public Quaternion getRotation(){
        return rotation;
    }

    public Vec3d getPosition(){
        return position;
    }

    public Node getBone(){
        return bone;
    }

    public IBakedModel getModel(){
        return model;
    }
}

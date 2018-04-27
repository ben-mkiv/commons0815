package ben_mkiv.commons0815.utils;


public class CollisionUtil {
    /*
    //from theo @ http://dev.theomader.com/transform-bounding-boxes/
    public static AxisAlignedBB transformBoundingBox(ModelHandle modle, AxisAlignedBB boundingBox, Matrix4 m) {
        float xa = m.Right * boundingBox.minX;
        float xb = m.Right * boundingBox.maxX;

        float ya = m.Up * boundingBox.minX;
        float yb = m.Up * boundingBox.maxX;

        float za = m.Backward * boundingBox.minZ;
        float zb = m.Backward * boundingBox.maxZ;

        Vec3d pos1 = (xa, xb) + Vector3.Min(ya, yb) + Vector3.Min(za, zb) + m.Translation,
        Vec3d pos2 = Vector3.Max(xa, xb) + Vector3.Max(ya, yb) + Vector3.Max(za, zb) + m.Translation

        return new AxisAlignedBB(pos1, pos2);

        return null;
    }*/
}

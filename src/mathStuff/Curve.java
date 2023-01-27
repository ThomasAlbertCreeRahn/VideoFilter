package mathStuff;

import java.awt.geom.Point2D;

public interface Curve {
    Point2D.Double valueAt(double t);

}

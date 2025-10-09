package road.core;

/**
 *
 * P2d -- modelling a point in a 2D space
 * 
 */
public record P2d(double x, double y) {

    public P2d sum(final V2d v){
        return new P2d(this.x +v.x(), this.y +v.y());
    }

    public V2d sub(final P2d v){
        return new V2d(this.x -v.x, this.y -v.y);
    }

    public String toString(){
        return "P2d("+ this.x +","+ this.y +")";
    }

    public static double len(final P2d p0, final P2d p1) {
    	final double dx = p0.x - p1.x;
    	final double dy = p0.y - p1.y;
        return Math.sqrt(dx*dx+dy*dy);

    }

}

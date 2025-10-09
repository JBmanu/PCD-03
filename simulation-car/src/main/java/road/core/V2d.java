package road.core;

/**
 *
 * V2d represents a vector in a 2d space
 * 
 */
public record V2d(double x, double y) {

    public static V2d makeV2d(final P2d from, final P2d to) {
    	return new V2d(to.x() - from.x(), to.y() - from.y());
    }

    public V2d sum(final V2d v){
        return new V2d(this.x +v.x, this.y +v.y);
    }

    public double abs(){
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public V2d getNormalized(){
        final double module= Math.sqrt(this.x * this.x + this.y * this.y);
        return new V2d(this.x /module, this.y /module);
    }

    public V2d mul(final double fact){
        return new V2d(this.x *fact, this.y *fact);
    }

    public String toString(){
        return "V2d("+ this.x +","+ this.y +")";
    }
}

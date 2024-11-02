public class ComplexNum {

    public double x, y, r, theta;
    public static final ComplexNum ONE = new ComplexNum(1, 0, false), ZERO = new ComplexNum(0, 0, false);

    public ComplexNum(double a, double b, boolean polar){
        if(polar) {
            this.r = a;
            this.theta = b;
            updateRect();
        } else {
            this.x = a;
            this.y = b;
            updatePolar();
        }
    }

    private void updateRect() {
        this.x = r * Math.cos(theta);
        this.y = r * Math.sin(theta);
    }

    private void updatePolar() {
        this.r = Math.sqrt(x*x+y*y);
        this.theta = Math.acos(x/r);
        if(y < 0) theta = 2 * Math.PI - theta;
    }

    public void add(ComplexNum n) {
        this.x += n.x;
        this.y += n.y;
        updatePolar();
    }

    public void subtract(ComplexNum n) {
        this.x -= n.x;
        this.y -= n.y;
        updatePolar();
    }

    public ComplexNum multiply(ComplexNum n) {
        this.r *= n.r;
        this.theta += n.theta;
        updateRect();
        return this;
    }

    public ComplexNum divide(ComplexNum n) {
        if(n.equals(ZERO)) return null;
        this.r /= n.r;
        this.theta -= n.theta;
        updateRect();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        ComplexNum n = (ComplexNum) o;
        return (this.x == n.x && this.y == n.y);
    }

//    public ComplexNum power(double p) {
//        if(this.equals(ZERO)) return ((p > 0) ? ZERO : null);
//        if(this.equals(ONE)) return ONE;
//        double[] polarValues = this.toPolar();
//        return toRect(Math.pow(polarValues[0], p), polarValues[1] * p);
//    }

//    public String toString(boolean polar) {
//        if(!polar) {
//            if(x == 0) {
//                if(y == 0) return "0";
//                if(y == 1) return "i";
//                if(y == -1) return "-i";
//                return y + "i";
//            } else {
//                if(y == 0) {
//                    return "" + x;
//                } else {
//                    return "" + x + ((y > 0) ? " + " + ((y == 1) ? "" : y) : " - " + ((y == -1) ? "" : (-y))) + "i";
//                }
//            }
//        } else {
//            if(r == 0) return "0";
//        }
//    }

}

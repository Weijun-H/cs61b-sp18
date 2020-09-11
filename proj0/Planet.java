public class Planet{
    public double xxPos, yyPos, xxVel, yyVel, mass;
    public String imgFileName;
    public static double G = 6.67 * Math.pow(10, -11);
    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;  
    }
    public Planet(Planet p){
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel; 
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p){
        double x_2 = Math.pow((xxPos - p.xxPos), 2);
        double y_2 = Math.pow((yyPos - p.yyPos), 2);
        return (Math.pow((x_2 + y_2),0.5));
    }
    
    public double calcForceExertedBy(Planet p){
        return (G * mass * p.mass / (calcDistance(p) * calcDistance(p)));
    }
    public static void main(String[] args) {
        
    }
}
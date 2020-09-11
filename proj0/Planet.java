public class Planet{
    public double xxPos, yyPos, xxVel, yyVel, mass;
    public String imgFileName;
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
    public static void main(String[] args) {
        
    }
}
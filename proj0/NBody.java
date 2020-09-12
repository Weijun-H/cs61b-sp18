public class NBody {
    public static double readRadius(String FileName){
        In in = new In(FileName);
        double Radius = in.readDouble();
        Radius = in.readDouble();
        return Radius;
    }
    
    public static Planet[] readPlanets(String FileName){
        In in = new In(FileName);
        int index = 0;
        int n = in.readInt();
        Planet[] Names = new Planet[n];
        double Radius = in.readDouble();
        while(n-- > 0){
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String imgFileName = in.readString();
            Names[index] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
            index++;
        }
        return Names;
    }
}

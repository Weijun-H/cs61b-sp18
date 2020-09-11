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
        while(!in.isEmpty()){
            Names[index].xxPos = in.readDouble();
            Names[index].yyPos = in.readDouble();
            Names[index].xxVel = in.readDouble();
            Names[index].yyVel = in.readDouble();
            Names[index].mass = in.readDouble();
 
            Names[index].imgFileName = in.readString();
            index++;
        }
        return Names;
    }
}

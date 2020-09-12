public class NBody {
    public static double readRadius(String FileName){
        In in = new In(FileName);
        double Radius = in.readDouble();
        Radius = in.readDouble();
        return Radius;
    }
    
    public static int readN(String FileName){
        In in = new In(FileName);
        int n = in.readInt(); 
        return n;
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
        
    public static void main(String[] args) {
        double T, dt;
        T = Double.parseDouble(args[0]);
        dt = Double.parseDouble(args[1]);
        String FileName = args[2];
        double Radius = readRadius(FileName);
        Planet[] Ps = readPlanets(FileName);

        StdDraw.setScale(-Radius, Radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");

        for(Planet p : Ps){
            p.draw();
        }

        int n = readN(FileName);
        StdDraw.enableDoubleBuffering();

        double t = 0;
        while(t < T){
            double[] xForces = new double[n];
            double[] yForces = new double[n];
            for (int i = 0; i < n; i++) {
                xForces[i] = Ps[i].calcNetForceExertedByX(Ps);
                yForces[i] = Ps[i].calcNetForceExertedByY(Ps);
            }

            for (int i = 0; i < n; i++) {
                Ps[i].update(dt, xForces[i], yForces[i]);
            }
    
            StdDraw.picture(0, 0, "images/starfield.jpg");
     
            for(Planet p : Ps){
                p.draw();
            }
            StdDraw.show();

            StdDraw.pause(10);
            t += dt;
        }
        StdDraw.show();

    }
}

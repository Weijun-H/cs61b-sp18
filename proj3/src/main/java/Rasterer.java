import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private class QueryBox {
        private double ullon, ullat, lrlon, lrlat;
        private double w, h;

        public QueryBox(double ullat, double ullon, double lrlat, double lrlon) {
            this.ullon = ullon;
            this.ullat = ullat;
            this.lrlat = lrlat;
            this.lrlon = lrlon;
        }

        public void setW(double w) {
            this.w = w;
        }

        public void setH(double h) {
            this.h = h;
        }

        public double calculateLonDPP() {
            return (this.lrlon - this.ullon) / this.w;
        }
    }

    private class Area {
        private double ullon, ullat, lrlon, lrlat;
        /* status[0] --> left
         * status[1] --> up
         * status[2] --> right
         * status[3] --> low */
        public boolean[] status = new boolean[4];
        public int leftIndex, upIndex, rightIndex, lowIndex;
        public Area(double ullat, double ullon, double lrlat, double lrlon) {
            this.ullon = ullon;
            this.ullat = ullat;
            this.lrlat = lrlat;
            this.lrlon = lrlon;

            for (int i = 0; i < 4; i++) {
                status[i] = false;
            }

            leftIndex = upIndex = 0;
            rightIndex = lowIndex = pow2(depth) - 1;
        }

        @Override
        public String toString() {
            return "Area{" +
                    "ullon=" + ullon +
                    ", ullat=" + ullat +
                    ", lrlon=" + lrlon +
                    ", lrlat=" + lrlat +
                    ", status=" + Arrays.toString(status) +
                    ", leftIndex=" + leftIndex +
                    ", upIndex=" + upIndex +
                    ", rightIndex=" + rightIndex +
                    ", lowIndex=" + lowIndex +
                    '}';
        }
    }

    private int depth;
    private double LonDPP;
    private double rasterUllon, rasterUllat, rasterLrlon, rasterLrlat;
    private int ulx, uly, lrx, lry;
    private String[][] filenames;
    public Rasterer() {
        // YOUR CODE HERE
    }

    private int pow2(int n){
        int base = 2;
        int tmp = 1;
        for (int i = 0; i < n; i++) {
            tmp *= base;
        }
        return tmp;
    }

    private void setDepth (QueryBox queryBox) {
        double qLonDPP = queryBox.calculateLonDPP();
        for (int i = 0; i <= 7; i++){
            double LonDpp = calculateLonDPP(i);
//            System.out.println("qLonDpp: " + qLonDPP);
//            System.out.println("LonDPP: " + LonDpp);
            if (qLonDPP > LonDpp){
                this.depth = i;

                return ;
            }
        }
        this.depth = 7;
    }

    private double calculateLonDPP (int dep) {
        double LonDpp = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / ((pow2(dep)) * MapServer.TILE_SIZE);
        return LonDpp;
    }

    private Area getArea(QueryBox queryBox){
        Area tmp = new Area(MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON, MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON);

        double ullon = MapServer.ROOT_ULLON;
        double ullat = MapServer.ROOT_ULLAT;

        double lonPT = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / pow2(this.depth);
        double latPT = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / pow2(this.depth);
        for (int i = 0; i < pow2(this.depth); i++) {
            // Calculate leftIndex
            if (!tmp.status[0]) {
                if (ullon + lonPT > queryBox.ullon && ullon <= queryBox.ullon) {
                    tmp.status[0] = true;
                    tmp.leftIndex = i;
                    tmp.ullon = ullon;
                }
            }
            // Calculate upIndex
            if (!tmp.status[1]) {
                if (ullat - latPT < queryBox.ullat && ullat > queryBox.ullat) {
                    tmp.status[1] = true;
                    tmp.upIndex = i;
                    tmp.ullat = ullat;
                }
            }
            // Calculate rightIndex
            if (!tmp.status[2]) {
                if (ullon + lonPT > queryBox.lrlon && ullon <= queryBox.lrlon) {
                    tmp.status[2] = true;
                    tmp.rightIndex = i;
                    tmp.lrlon = ullon + lonPT;
                }
            }
            // Calculate lowIndex
            if (!tmp.status[3]) {
                if (ullat - latPT <= queryBox.lrlat && ullat > queryBox.lrlat) {
                    tmp.status[3] = true;
                    tmp.lowIndex = i;
                    tmp.lrlat = ullat - latPT;
                }
            }
            ullon += lonPT;
            ullat -= latPT;
        }
        return tmp;
    }
    private void getFilenames(Area stage){
        filenames = new String[lry - uly + 1][lrx - ulx + 1];
        for (int i = 0; i <= lry - uly; i++) {
            for (int j = 0; j <= lrx - ulx; j++) {
                filenames[i][j] = "d" + depth + "_x" + (ulx + j)
                        + "_y" + (uly + i) + ".png";
//                System.out.print(filenames[i][j] + " ");
            }
//            System.out.println("");
        }
    }

    private void setArea(Area stage){
        this.ulx = stage.leftIndex;
        this.uly = stage.upIndex;
        this.lrx = stage.rightIndex;
        this.lry = stage.lowIndex;
        this.rasterLrlat = stage.lrlat;
        this.rasterLrlon = stage.lrlon;
        this.rasterUllat = stage.ullat;
        this.rasterUllon = stage.ullon;
    }

    private void solver(QueryBox queryBox) {
        setDepth(queryBox);
        this.LonDPP = calculateLonDPP(this.depth);
//        System.out.println(getArea(queryBox));
        Area stage = getArea(queryBox);
        setArea(stage);
        getFilenames(stage);
//        System.out.println("depth: "+ this.depth);
//        System.out.println("pow2: "+ pow2(this.depth));

    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);

        Map<String, Object> results = new HashMap<>();
        double ullat = params.get("ullat");
        double ullon = params.get("ullon");
        double lrlat = params.get("lrlat");
        double lrlon = params.get("lrlon");
        QueryBox queryBox = new QueryBox(ullat, ullon, lrlat, lrlon);
        queryBox.setH(params.get("h"));
        queryBox.setW(params.get("w"));
        solver(queryBox);
        results.put("render_grid", filenames);
        results.put("raster_ul_lon", rasterUllon);
        results.put("raster_ul_lat", rasterUllat);
        results.put("raster_lr_lon", rasterLrlon);
        results.put("raster_lr_lat", rasterLrlat);
        results.put("depth", depth);
        results.put("query_success", true);
//        System.out.println(results);
//        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
//                           + "your browser.");
        return results;
    }

}

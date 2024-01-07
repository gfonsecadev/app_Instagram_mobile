package com.example.instagram.tools;
import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubFilter;

/**
 * @autor Varun em 01/07/15.
 */
public class   SampleFilters {

    private  SampleFilters () {
    }

    public static Filter sepia(){
        Filter filter=new Filter();
        filter.addSubFilter(new SaturationSubFilter(.0f));
        filter.addSubFilter(new BrightnessSubFilter(35));
        filter.addSubFilter(new ContrastSubFilter(1.1f));
        filter.addSubFilter(new ColorOverlaySubFilter(100,.8f,0.4f,0.14f));

        return filter;
    }

    public static Filter x(){
        Filter filter=new Filter();
        filter.addSubFilter(new SaturationSubFilter(1.45f));
        filter.addSubFilter(new BrightnessSubFilter(-15));
        filter.addSubFilter(new ContrastSubFilter(1.1f));
        filter.addSubFilter(new ColorOverlaySubFilter(100,.8f,0.4f,0.14f));

        return filter;
    }

    public static Filter z(){
        Filter filter=new Filter();
        filter.addSubFilter(new SaturationSubFilter(0.45f));
        filter.addSubFilter(new BrightnessSubFilter(-10));
        filter.addSubFilter(new ContrastSubFilter(1.25f));


        return filter;
    }

    public static Filter q(){
        Filter filter=new Filter();
        filter.addSubFilter(new SaturationSubFilter(0.05f));
        filter.addSubFilter(new BrightnessSubFilter(15));
        filter.addSubFilter(new ContrastSubFilter(2f));



        return filter;
    }

    public static Filter OVER(){
        Filter filter=new Filter();
        filter.addSubFilter(new ColorOverlaySubFilter(80,.9f,0.4f,0.14f));

        return filter;
    }

    public static Filter gray(){
        Filter filter=new Filter();
        filter.addSubFilter(new SaturationSubFilter(0));

        return filter;
    }

    public  static  Filter  y () {
        Point [] rgbKnots ;
        Point [] redKnots ;
        Point [] greenKnots ;
        Point [] blueKnots ;

        rgbKnots = new  Point [ 5 ];
        rgbKnots [ 0 ] = new  Point ( 0 , 0 );
        rgbKnots [ 1 ] = new  Point ( 80 , 43 );
        rgbKnots [ 2 ] = new  Point ( 149 , 102 );
        rgbKnots [ 3 ] = new  Point ( 201 , 173 );
        rgbKnots [ 4 ] = new  Point ( 255 , 255 );

        redKnots = new  Point [ 5 ];
        redKnots [ 0 ] = new  Point ( 0 , 0 );
        redKnots [ 1 ] = new  Point ( 125 , 147 );
        redKnots [ 2 ] = new  Point ( 177 , 199 );
        redKnots [ 3 ] = new  Point ( 213 , 228 );
        redKnots [ 4 ] = new  Point ( 255 , 255 );


        greenKnots = new  Point [ 6 ];
        greenKnots [ 0 ] = new  Point ( 0 , 0 );
        greenKnots [ 1 ] = new  Point ( 57 , 76 );
        greenKnots [ 2 ] = new  Point ( 103 , 130 );
        greenKnots [ 3 ] = new  Point ( 167 , 192 );
        greenKnots [ 4 ] = new  Point ( 211 , 229 );
        greenKnots [ 5 ] = new  Point ( 255 , 255 );


        blueKnots = new  Point [ 7 ];
        blueKnots [ 0 ] = new  Point ( 0 , 0 );
        blueKnots [ 1 ] = new  Point ( 38 , 62 );
        blueKnots [ 2 ] = new  Point ( 75 , 112 );
        blueKnots [ 3 ] = new  Point ( 116 , 158 );
        blueKnots [ 4 ] = new  Point ( 171 , 204 );
        blueKnots [ 5 ] = new  Point ( 212 , 233 );
        blueKnots [ 6 ] = new  Point ( 255 , 255 );

        Filter  filtro = new Filter ();
        filtro . addSubFilter ( new  ToneCurveSubFilter ( rgbKnots , redKnots , greenKnots , blueKnots ));
        filtro . addSubFilter ( new BrightnessSubFilter( 30 ));
        filtro . addSubFilter ( new ContrastSubFilter( 1.27f ));

        return filtro ;
    }



    public  static Filter getStarLitFilter () {
        Point [] rgbKnots ;
        rgbKnots = new Point[ 8 ];
        rgbKnots [ 0 ] = new  Point ( 0 , 0 );
        rgbKnots [ 1 ] = new  Point ( 34 , 6 );
        rgbKnots [ 2 ] = new  Point ( 69 , 23 );
        rgbKnots [ 3 ] = new  Point ( 100 , 58 );
        rgbKnots [ 4 ] = new  Point ( 150 , 154 );
        rgbKnots [ 5 ] = new  Point ( 176 , 196 );
        rgbKnots [ 6 ] = new  Point ( 207 , 233 );
        rgbKnots [ 7 ] = new  Point ( 255 , 255 );
        Filter  filtro = new Filter ();
        filtro . addSubFilter ( new ToneCurveSubFilter( rgbKnots , null , null , null ));
        return filtro;
    }

    public  static  Filter  getBlueMessFilter () {
        Point [] redKnots ;
        redKnots = new  Point [ 8 ];
        redKnots [ 0 ] = new  Point ( 0 , 0 );
        redKnots [ 1 ] = new  Point ( 86 , 34 );
        redKnots [ 2 ] = new  Point ( 117 , 41 );
        redKnots [ 3 ] = new  Point ( 146 , 80 );
        redKnots [ 4 ] = new  Point ( 170 , 151 );
        redKnots [ 5 ] = new  Point ( 200 , 214 );
        redKnots [ 6 ] = new  Point ( 225 , 242 );
        redKnots [ 7 ] = new  Point ( 255 , 255 );
        Filter  filtro = new Filter ();
        filtro . addSubFilter ( new  ToneCurveSubFilter ( null , redKnots , null , null ));
        filtro . addSubFilter ( new BrightnessSubFilter( 30 ));
        filtro . addSubFilter ( new ContrastSubFilter( 1f ));
        return filtro;
    }

    public  static  Filter  getAweStruckVibeFilter () {
        Point [] rgbKnots ;
        Point [] redKnots ;
        Point [] greenKnots ;
        Point [] blueKnots ;

        rgbKnots = new  Point [ 5 ];
        rgbKnots [ 0 ] = new  Point ( 0 , 0 );
        rgbKnots [ 1 ] = new  Point ( 80 , 43 );
        rgbKnots [ 2 ] = new  Point ( 149 , 102 );
        rgbKnots [ 3 ] = new  Point ( 201 , 173 );
        rgbKnots [ 4 ] = new  Point ( 255 , 255 );

        redKnots = new  Point [ 5 ];
        redKnots [ 0 ] = new  Point ( 0 , 0 );
        redKnots [ 1 ] = new  Point ( 125 , 147 );
        redKnots [ 2 ] = new  Point ( 177 , 199 );
        redKnots [ 3 ] = new  Point ( 213 , 228 );
        redKnots [ 4 ] = new  Point ( 255 , 255 );


        greenKnots = new  Point [ 6 ];
        greenKnots [ 0 ] = new  Point ( 0 , 0 );
        greenKnots [ 1 ] = new  Point ( 57 , 76 );
        greenKnots [ 2 ] = new  Point ( 103 , 130 );
        greenKnots [ 3 ] = new  Point ( 167 , 192 );
        greenKnots [ 4 ] = new  Point ( 211 , 229 );
        greenKnots [ 5 ] = new  Point ( 255 , 255 );


        blueKnots = new  Point [ 7 ];
        blueKnots [ 0 ] = new  Point ( 0 , 0 );
        blueKnots [ 1 ] = new  Point ( 38 , 62 );
        blueKnots [ 2 ] = new  Point ( 75 , 112 );
        blueKnots [ 3 ] = new  Point ( 116 , 158 );
        blueKnots [ 4 ] = new  Point ( 171 , 204 );
        blueKnots [ 5 ] = new  Point ( 212 , 233 );
        blueKnots [ 6 ] = new  Point ( 255 , 255 );

        Filter  filtro = new Filter ();
        filtro . addSubFilter ( new  ToneCurveSubFilter ( rgbKnots , redKnots , greenKnots , blueKnots ));
        return filtro ;
    }

    public  static  Filter  getLimeStutterFilter () {
        Point [] blueKnots ;
        blueKnots = new  Point [ 3 ];
        blueKnots [ 0 ] = new  Point ( 0 , 0 );
        blueKnots [ 1 ] = new  Point ( 165 , 114 );
        blueKnots [ 2 ] = new  Point ( 255 , 255 );
        // Verifica se a saída é nula ou não.
        Filter  filtro = new Filter();
        filtro . addSubFilter ( new  ToneCurveSubFilter ( null , null , null , blueKnots ));
        return filtro ;
    }

    public  static  Filter  getNightWhisperFilter () {
        Point [] rgbKnots ;
        Point [] redKnots ;
        Point [] greenKnots ;
        Point [] blueKnots ;

        rgbKnots = new  Point [ 3 ];
        rgbKnots [ 0 ] = new  Point ( 0 , 0 );
        rgbKnots [ 1 ] = new  Point ( 174 , 109 );
        rgbKnots [ 2 ] = new  Point ( 255 , 255 );

        redKnots = new  Point [ 4 ];
        redKnots [ 0 ] = new  Point ( 0 , 0 );
        redKnots [ 1 ] = new  Point ( 70 , 114 );
        redKnots [ 2 ] = new  Point ( 157 , 145 );
        redKnots [ 3 ] = new  Point ( 255 , 255 );

        greenKnots = new  Point [ 3 ];
        greenKnots [ 0 ] = new  Point ( 0 , 0 );
        greenKnots [ 1 ] = new  Point ( 109 , 138 );
        greenKnots [ 2 ] = new  Point ( 255 , 255 );

        blueKnots = new  Point [ 3 ];
        blueKnots [ 0 ] = new  Point ( 0 , 0 );
        blueKnots [ 1 ] = new  Point ( 113 , 152 );
        blueKnots [ 2 ] = new  Point ( 255 , 255 );

        Filter  filtro = new Filter ();
        filtro . addSubFilter ( new  ToneCurveSubFilter ( rgbKnots , redKnots , greenKnots , blueKnots ));
        return filtro ;
    }
}
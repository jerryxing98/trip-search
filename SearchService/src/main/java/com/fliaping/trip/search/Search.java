package com.fliaping.trip.search;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by Payne on 4/2/16.
 * url参数意义
 *    keyword          关键词                               null
 *    point            坐标点                               null
 *    distance         距离                                 100KM
 *    bound_type       界限方式{geofilt,bbox,linestring,polygon}   null
 *    boundary         范围                                 null
 *    sort_order       排序方式 {score,distance}             null
 *    sight_type       景点类型                              null
 *    place_i          行政区划                              null
 *    page             页数                                  1
 *    rows             每页记录条数                           10
 *    query_type       查询方式{near,key}                     near
 */
public class Search extends HttpServlet{
    private Map<String, String[]> urlMap;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DoQuery query = new DoQuery(req,resp);
        String query_type = req.getParameter(UrlP.query_type.name());

        if (!Setting.contains(query_type, UrlP.query_type)) query_type = QueryType.near.get(); //没有定义的query_type方法,设置默认


        if (QueryType.key.is(query_type)){    //关键词查询
            query.keyQuery();
        }else if (QueryType.map.is(query_type)){  //地图查询
            query.mapQuery();
        }else {      //附近查询
            query.nearQuery();
        }


    }


    private String urlCode(){
        String result = "";
        if(availability("keyword") )  result+="1"; else result+="0";
        if(availability("point"))  result+="1"; else result+="0";
        if(availability("bound_type") )  result+="1"; else result+="0";
        if(availability("boundary") )  result+="1"; else result+="0";
        if(availability("sort_order") )  result+="1"; else result+="0";
        if(availability("sight_type") )  result+="1"; else result+="0";
        return result;
    }

    /**
     * 初步判断参数是否有效
     * @param param 参数key
     * @return true or false
     */
    private boolean availability(String param){
        String[] value = urlMap.get(param);
        if(null != value && value.length>0 && "" != value[0]) return true;
        else return false;
    }

}

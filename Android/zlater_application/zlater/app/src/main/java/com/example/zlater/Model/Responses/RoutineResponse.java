package com.example.zlater.Model.Responses;

import com.example.zlater.Model.Routine;

import java.util.ArrayList;

/**
 * Created by Hades on 03,November,2019
 **/
public class RoutineResponse extends BaseResponse {
    private ArrayList<Routine> Response;
    private Routine Object;
    public RoutineResponse(int status, String message) {
        super(status, message);
    }

    public ArrayList<Routine> getResponse() {
        return Response;
    }

    public void setResponse(ArrayList<Routine> response) {
        Response = response;
    }

    public Routine getObject() {
        return Object;
    }

    public void setObject(Routine object) {
        Object = object;
    }
}

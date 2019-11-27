package com.example.zlater.Model.Responses;

import com.example.zlater.Model.Exercise;

import java.util.ArrayList;

public class ExerciseResponse extends BaseResponse {
    private ArrayList<Exercise> Response;
    private Exercise data;
    public ExerciseResponse(int status, String message) {
        super(status, message);
    }

    public ArrayList<Exercise> getResponse() {
        return Response;
    }

    public void setResponse(ArrayList<Exercise> response) {
        Response = response;
    }

    public Exercise getData() {
        return data;
    }

    public void setData(Exercise data) {
        this.data = data;
    }
}

package com.example.zlater.Model.Responses;

import com.example.zlater.Model.Diet;

import java.util.ArrayList;

public class DietsResponse extends BaseResponse {
    private ArrayList<Diet> Response;
    public DietsResponse(int status, String message) {
        super(status, message);
    }

    public ArrayList<Diet> getResponse() {
        return Response;
    }

    public void setResponse(ArrayList<Diet> response) {
        Response = response;
    }
}

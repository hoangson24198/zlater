package com.example.zlater.Model.Responses;

import com.example.zlater.Model.BodyParts;

import java.util.ArrayList;

public class BodypartResponse extends BaseResponse {
    private ArrayList<BodyParts> Response;
    private BodyParts Object;
    public BodypartResponse(int status, String message) {
        super(status, message);
    }

    public ArrayList<BodyParts> getResponse() {
        return Response;
    }

    public void setResponse(ArrayList<BodyParts> response) {
        Response = response;
    }

    public BodyParts getObject() {
        return Object;
    }

    public void setObject(BodyParts object) {
        Object = object;
    }
}

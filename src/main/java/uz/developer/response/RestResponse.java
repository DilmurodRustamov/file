package uz.developer.response;

public interface RestResponse {

    BaseRestResponse SUCCESS = new BaseRestResponse("operating is success",0);
    BaseRestResponse ERROR = new BaseRestResponse("operating is not success",-100);
}

package jjfactory.simpleapi.global.config.retrofit;

import jjfactory.simpleapi.business.delivery.dto.req.DeliveryEndReq;
import jjfactory.simpleapi.business.delivery.dto.req.DeliveryStartReq;
import jjfactory.simpleapi.business.insurance.dto.req.EndorsementReq;
import jjfactory.simpleapi.business.insurance.dto.req.UnderWritingReq;
import jjfactory.simpleapi.business.insurance.dto.res.RiderCountRes;
import jjfactory.simpleapi.business.insurance.dto.req.RiderWithdrawReq;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

public interface RetrofitApi {
    @POST("/example")
    Call<RiderCountRes> underWritingRetrofit(@Body List<UnderWritingReq> dto);

//    @POST("/gogofnd/kbInsOnline.do")
//    Call<KbApi4thRes> kbApi4Retrofit(@Body KbApi4thReq dto);

    @POST("/example")
    Call<RiderCountRes> endorsementRetrofit(@Body List<EndorsementReq> dto);

    @POST("/example")
    Call<RiderCountRes> withdrawRetrofit(@Body List<RiderWithdrawReq> requests);

    @POST("/example")
    Call<String> drivingStart(@Body DeliveryStartReq dto);

    @POST("/example")
    Call<String> drivingEnd(@Body DeliveryEndReq dto);
//
//    @POST("/example")
//    Call<com.gogofnd.kb.domain.delivery.dto.insure.res.CountDto> kbApi12Retrofit(@Body List<DeliveryInsureHistoryReq> dto);
}

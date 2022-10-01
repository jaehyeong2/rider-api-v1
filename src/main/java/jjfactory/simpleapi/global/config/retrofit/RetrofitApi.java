package jjfactory.simpleapi.global.config.retrofit;

import jjfactory.simpleapi.business.delivery.dto.DeliveryRes;
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
    @POST("/rider/underWrite")
    Call<RiderCountRes> underWritingRetrofit(@Body List<UnderWritingReq> dto);

    @POST("/rider/endorsement")
    Call<RiderCountRes> endorsementRetrofit(@Body List<EndorsementReq> dto);

    @POST("/rider/withdraw")
    Call<RiderCountRes> withdrawRetrofit(@Body List<RiderWithdrawReq> requests);

    @POST("/delivery")
    Call<String> totalDeliveriesToday(@Body List<DeliveryRes> requests);
    @POST("/delivery/start")
    Call<String> drivingStart(@Body DeliveryStartReq dto);

    @POST("/delivery/end")
    Call<String> drivingEnd(@Body DeliveryEndReq dto);
}

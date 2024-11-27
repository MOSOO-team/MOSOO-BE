package com.team2.mosoo_backend.payment.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.io.IOException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class PaymentController {

    private final IamportClient iamportClient;

    public PaymentController(IamportClient iamportClient) {
        this.iamportClient = new IamportClient("5561133411260273", "5sw3JWEyzRDwt4G0Op58u2MeUcbcc7ODoLz3bshwnDjBDvRmrm9AVoCM6KaA33lEvgNeUj162Xt593gh");
    }


    @ResponseBody
    @RequestMapping("/verify/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(imp_uid);
    }

}

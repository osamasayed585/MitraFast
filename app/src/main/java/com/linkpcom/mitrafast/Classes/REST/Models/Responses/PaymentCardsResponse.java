package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.PaymentCard;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentCardsResponse extends BaseResponse {
    private List<PaymentCard> data;

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            names.add(data.get(i).getCredit_card_name());
        }
        return names;
    }
}
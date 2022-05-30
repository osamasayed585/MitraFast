package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.TicketType;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TicketTypesResponse extends BaseResponse {
    private List<TicketType> data;

    public List<String> getTicketTypeNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            names.add(data.get(i).getName());
        }
        return names;
    }
}
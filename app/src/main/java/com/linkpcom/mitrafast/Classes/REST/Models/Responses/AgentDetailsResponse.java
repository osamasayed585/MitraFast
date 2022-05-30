package com.linkpcom.mitrafast.Classes.REST.Models.Responses;


import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Agent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AgentDetailsResponse extends BaseResponse {
    private Agent data;
}

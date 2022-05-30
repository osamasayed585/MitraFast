package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class AgentNode {
    private String driverId;

    public AgentNode() {
    }
}

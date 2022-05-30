package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FinancialMovement {
    private double final_cost;
    private List<Order> orders;
}
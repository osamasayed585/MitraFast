package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyProfile {

    private int id;
    private String company_name;
    private String tax_id_number;
    private String address;
}

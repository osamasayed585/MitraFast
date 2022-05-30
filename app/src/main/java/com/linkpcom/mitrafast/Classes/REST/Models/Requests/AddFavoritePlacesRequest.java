package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class AddFavoritePlacesRequest {
    private String name;
    private String lat;
    private String lng;
    private String address;
    private String Locality;
    private String SubLocality;
    private String AdminArea;


}

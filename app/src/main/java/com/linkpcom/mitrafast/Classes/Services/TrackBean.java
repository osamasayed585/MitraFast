package com.linkpcom.mitrafast.Classes.Services;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@Builder
public class TrackBean {
    public int id;
    public String status_id;
    public String order_id;
    public String time;
    public String lat_lng;
    public float b;
    public String g;
    public float distance;

}

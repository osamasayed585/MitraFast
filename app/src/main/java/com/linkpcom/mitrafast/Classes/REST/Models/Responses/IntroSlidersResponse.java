package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.IntroSlider;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntroSlidersResponse extends BaseResponse{
    private List<IntroSlider> data;
}

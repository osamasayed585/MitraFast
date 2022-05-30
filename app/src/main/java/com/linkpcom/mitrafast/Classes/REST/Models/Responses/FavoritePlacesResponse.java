package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.FavoritePlace;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FavoritePlacesResponse extends BaseResponse {
    List<FavoritePlace> data;
}

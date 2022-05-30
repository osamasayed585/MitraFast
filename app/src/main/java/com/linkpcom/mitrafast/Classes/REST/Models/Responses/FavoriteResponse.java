package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.FavoritesShops;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.ObjectFavoritesShops;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteResponse extends BaseResponse {

    private List<ObjectFavoritesShops> data;
}
